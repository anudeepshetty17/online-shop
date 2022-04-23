package com.anudeep.onlineshop.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anudeep.onlineshop.model.CartItem;
import com.anudeep.onlineshop.model.Order;
import com.anudeep.onlineshop.service.ShoppingCartService;

@CrossOrigin
@RestController
@RequestMapping("/shopping-cart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping(value = {"", "/"})
    public ResponseEntity<List<CartItem>> fetchCartItems(HttpSession session,HttpServletRequest request) {
        return shoppingCartService.getCart(session,request);
    }

    @PostMapping(value = {"/add-to-cart", "/add-to-cart/"})
    public ResponseEntity<List<CartItem>> addCartItem(@RequestBody CartItem cartItemToSave, HttpSession session,HttpServletRequest request) {
        return shoppingCartService.saveItem(cartItemToSave, session,request);
    }
    
    @PostMapping(value = {"/update-cart-item/{action}/", "/update-cart-item/{action}"})
    public ResponseEntity<CartItem> updateCartItem(@RequestBody CartItem cartItemToSave, HttpSession session,@PathVariable("action") String action,HttpServletRequest request) {
        return shoppingCartService.updateCartItem(cartItemToSave,action,session,request);
    }

    @DeleteMapping(value = {"/remove-from-cart/{id}", "/remove-from-cart/{id}"})
    public ResponseEntity<Boolean> removeCartItem(@PathVariable("id") String productId, HttpSession session,HttpServletRequest request) {
        return shoppingCartService.removeItem(productId, session,request);
    }

    @GetMapping(value = {"/totalQuantity", "/totalQuantity/"})
    public ResponseEntity<Integer> fetchTotalQuantity(HttpSession session,HttpServletRequest request) {
        return shoppingCartService.getTotalQuantity(session,request);
    }
    @GetMapping(value = {"/placeorder", "/placeorder/"})
    public ResponseEntity<Order> placeOrder(HttpSession session,HttpServletRequest request) {
        return shoppingCartService.placeOrder(session,request);
    }

    @GetMapping(value = {"/totalPrice", "/totalPrice/"})
    public ResponseEntity<BigDecimal> fetchTotalPrice(HttpSession session,HttpServletRequest request) {
        return shoppingCartService.getTotalPrice(session,request);
    }
    
}
