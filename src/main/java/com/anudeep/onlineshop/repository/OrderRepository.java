package com.anudeep.onlineshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anudeep.onlineshop.model.Order;
import com.anudeep.onlineshop.model.User;

public interface OrderRepository extends JpaRepository<Order, String> {
    Optional<Order> findById(String id);
    List<Order> findAllByUser(User user);
}
