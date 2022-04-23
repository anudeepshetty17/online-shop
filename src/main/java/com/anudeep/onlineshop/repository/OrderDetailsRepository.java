package com.anudeep.onlineshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import com.anudeep.onlineshop.model.Order;
import com.anudeep.onlineshop.model.OrderDetail;

public interface OrderDetailsRepository extends JpaRepository<OrderDetail, String> {
    Optional<OrderDetail> findById(String id);

    List<OrderDetail> findAllByOrder(Order order);
}
