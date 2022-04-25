package com.anudeep.onlineshop.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;

import com.anudeep.onlineshop.model.Category;

/**
 * The methods that must be implemented. Typically,
 * by a class of the same name with an 'Impl' suffix.
 *
 * Used for abstraction.
 */
public interface CategoryService {

    ResponseEntity<List<Category>> getAll();

    ResponseEntity<String> add(Category categoryToAdd, HttpSession session,HttpServletRequest request);

    ResponseEntity<String> update(Category categoryToUpdate, HttpSession session,HttpServletRequest request);

    ResponseEntity<String> delete(Long categoryId, HttpSession session,HttpServletRequest request);
    
    ResponseEntity<Optional<Category>> getOne(String productId);

	ResponseEntity<String> save(Category categoryToSave, HttpSession session,HttpServletRequest request);

	Category getbyType(Integer type);
    
    

}
