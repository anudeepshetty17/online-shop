package com.anudeep.onlineshop.repository;

import com.anudeep.onlineshop.model.CartItem;
import com.anudeep.onlineshop.model.Product;
import com.anudeep.onlineshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUser(User user);

    CartItem findByProductAndUser(Product product, User user);

    CartItem findByProductIdAndUser(String productId, User user);

    Boolean existsByProductAndUser(Product product, User user);

    Boolean existsByProductIdAndUser(String productId, User user);

    Long deleteByProductIdAndUser(String productId, User user);

}
