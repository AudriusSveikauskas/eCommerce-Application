package com.example.demo.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	private static final Logger log = LoggerFactory.getLogger(OrderController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	
	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {

			/** Logging */
			log.error("Type: {} | Status: {} | Source: {} | Username: {} | Description: {}",
					"ERROR", 404, "api/order/submit/" + user.getUsername(), user.getUsername(), "User not found.");

			return ResponseEntity.notFound().build();
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);

		/** Logging */
		log.info("Type: {} | Status: {} | Source: {} | Username: {} | Description: {}",
				"INFO", 200, "api/order/submit/" + user.getUsername(), user.getUsername(), "Order created successfully, (total = $" + order.getTotal() + ").");

		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {

			/** Logging */
			log.error("Type: {} | Status: {} | Source: {} | Username: {} | Description: {}",
					"ERROR", 404, "api/order/history/" + user.getUsername(), user.getUsername(), "User not found.");

			return ResponseEntity.notFound().build();
		}

		/** Logging */
		log.info("Type: {} | Status: {} | Source: {} | Username: {} | Description: {}",
				"INFO", 200, "api/order/history/" + user.getUsername(), user.getUsername(), "User order history printed successfully.");

		return ResponseEntity.ok(orderRepository.findByUser(user));
	}
}
