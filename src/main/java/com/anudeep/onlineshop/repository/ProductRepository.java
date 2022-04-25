package com.anudeep.onlineshop.repository;

import com.anudeep.onlineshop.model.Category;
import com.anudeep.onlineshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {

    Optional<Product> findById(String id);

    Product findByName(String name);
    
    List<Product> findAllByNameAndIdNot(String name,String id);

    List<Product> findAllByCategory(Category categoryType);

    Long deleteByCategory(Category categoryType);
    
    List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String nameSearch,
                                                                                  String descriptionSearch);

}
