package com.anudeep.onlineshop.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;

import com.anudeep.onlineshop.model.Order;
import com.anudeep.onlineshop.model.OrderDetail;

/**
 * The methods that must be implemented. Typically,
 * by a class of the same name with an 'Impl' suffix.
 *
 * Used for abstraction.
 */
public interface OrderService {

    ResponseEntity<List<Order>> getAll(HttpSession session,HttpServletRequest request); 
  

	ResponseEntity<List<OrderDetail>> getOrderDetails(String orderId);

}
