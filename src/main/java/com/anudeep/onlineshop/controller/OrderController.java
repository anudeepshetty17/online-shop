package com.anudeep.onlineshop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anudeep.onlineshop.model.Order;
import com.anudeep.onlineshop.model.OrderDetail;
import com.anudeep.onlineshop.service.OrderService;

@CrossOrigin
@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
    private OrderService orderService;

    /**
     * fetch-all-orders endpoint
     *
     * NOTE: Used only for debugging or admins; this
     * is not used in the angular project
     *
     * @param session The http session.
     * @return A list of all the orders.
     */
	@GetMapping(value = {"", "/"})
    public ResponseEntity<List<Order>> fetchAllOrders(HttpSession session,HttpServletRequest request) {
        return orderService.getAll(session,request);
    }
	@GetMapping("/orderDetail/{id}")
	public ResponseEntity<List<OrderDetail>> fetchProduct(@PathVariable("id") String orderId) {
        return orderService.getOrderDetails(orderId);
    }
}