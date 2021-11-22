package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static com.example.demo.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class CartControllerTest {

    private CartController cartController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void addToCartTest() {
        User user = createUser();
        Item item = createItem();
        Cart cart = user.getCart();

        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("testUsername")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = createModifyCartRequest("testUsername", 1L, 1);
        ResponseEntity<Cart> response = cartController.addToCart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart responseCart = response.getBody();
        assertNotNull(responseCart);

        List<Item> items = responseCart.getItems();
        assertNotNull(items);

        assertEquals("testUsername", responseCart.getUser().getUsername());
        verify(cartRepository, times(1)).save(responseCart);
    }

    @Test
    public void addToCartExceptionTest() {
        User user = createUser();
        Item item = createItem();
        Cart cart = user.getCart();

        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("testUsername")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = createModifyCartRequest("testUsername", 2L, 1);
        ResponseEntity<Cart> response = cartController.addToCart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromCartTest() {
        User user = createUser();
        Item item = createItem();
        Cart cart = user.getCart();

        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("testUsername")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = createModifyCartRequest("testUsername", 1L, 1);
        ResponseEntity<Cart> response = cartController.removeFromCart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart responseCart = response.getBody();

        assertNotNull(response);
        List<Item> items = responseCart.getItems();
        assertNotNull(items);
        assertEquals(0, items.size());
        assertEquals("testUsername", responseCart.getUser().getUsername());

        verify(cartRepository, times(1)).save(responseCart);
    }

    @Test
    public void removeFromCartExceptionTest() {
        User user = createUser();
        Item item = createItem();
        Cart cart = user.getCart();

        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("testUsername")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = createModifyCartRequest("testUsername", 2L, 1);
        ResponseEntity<Cart> response = cartController.removeFromCart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

}
