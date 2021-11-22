package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.TestUtils.createItem;
import static com.example.demo.TestUtils.createUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void submitTest() {
        User user = createUser();
        Item item = createItem();
        Cart cart = user.getCart();

        List<Item> items = new ArrayList<>();
        items.add(item);
        cart.setItems(items);

        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("testUsername")).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit("testUsername");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder userOrder = response.getBody();
        assertNotNull(userOrder);
        assertNotNull(userOrder.getItems());
        assertNotNull(userOrder.getTotal());
        assertNotNull(userOrder.getUser());
    }

    @Test
    public void getOrdersForUserTest() {
        User user = createUser();
        Item item = createItem();
        Cart cart = user.getCart();

        List<Item> items = new ArrayList<>();
        items.add(item);
        cart.setItems(items);

        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("testUsername")).thenReturn(user);
        orderController.submit("testUsername");
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("testUsername");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<UserOrder> userOrders = response.getBody();
        assertNotNull(userOrders);
        assertEquals(0, userOrders.size());
    }































}
