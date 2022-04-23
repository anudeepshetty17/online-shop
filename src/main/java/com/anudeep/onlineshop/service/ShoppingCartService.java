package com.anudeep.onlineshop.service;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;

import com.anudeep.onlineshop.model.CartItem;
import com.anudeep.onlineshop.model.Order;

public interface ShoppingCartService {

    ResponseEntity<List<CartItem>> getCart(HttpSession session,HttpServletRequest request); 

    ResponseEntity<List<CartItem>> saveItem(CartItem cartItemToSave, HttpSession session,HttpServletRequest request);

    ResponseEntity<Boolean> removeItem(String productId, HttpSession session,HttpServletRequest request);

    ResponseEntity<Integer> getTotalQuantity(HttpSession session,HttpServletRequest request); 

    ResponseEntity<BigDecimal> getTotalPrice(HttpSession session,HttpServletRequest request); 

	ResponseEntity<Order> placeOrder(HttpSession session,HttpServletRequest request); 

	ResponseEntity<CartItem> updateCartItem(CartItem cartItemToSave, String action, HttpSession session,HttpServletRequest request);

}
