package com.anudeep.onlineshop.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;

import com.anudeep.onlineshop.model.Product;

/**
 * The methods that must be implemented. Typically,
 * by a class of the same name with an 'Impl' suffix.
 *
 * Used for abstraction.
 */
public interface ProductService {

    ResponseEntity<List<Product>> getAll(HttpSession session,HttpServletRequest request); 

    ResponseEntity<Optional<Product>> getOne(String productId);

    ResponseEntity<List<Product>> getAllByCategory(Integer categoryType);

    ResponseEntity<List<Product>> getAllContainingSearchString(String searchStr);

    public ResponseEntity<String>  save(Product productToSave, HttpSession session,HttpServletRequest request);

    ResponseEntity<String> update(Product productToUpdate, HttpSession session,HttpServletRequest request);

    ResponseEntity<String> delete(String productId, HttpSession session,HttpServletRequest request);

}
