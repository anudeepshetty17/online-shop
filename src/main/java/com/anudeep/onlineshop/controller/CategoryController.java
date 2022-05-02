package com.anudeep.onlineshop.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anudeep.onlineshop.model.Category;
import com.anudeep.onlineshop.service.CategoryService;

@CrossOrigin
@RestController
// url suffix for all mappings
@RequestMapping("/categories")
@Secured(value = { "ROLE_ADMIN" }) 

public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * fetch-all-categories endpoint
     * @return
     */
    @GetMapping(value = {"", "/"})
    public ResponseEntity<List<Category>> fetchAllCategories() { return categoryService.getAll(); }

    /**
     * add-category endpoint
     * @param categoryToAdd
     * @return
     */
    @PostMapping(value = {"/add", "/add/"})
    public ResponseEntity<String> addCategory(@RequestBody Category categoryToAdd, HttpSession session,HttpServletRequest request) {
        return categoryService.save(categoryToAdd, session,request);
    }

    /**
     * edit-category endpoint
     * @param categoryToUpdate
     * @return
     */
    @PutMapping(value = {"/edit", "/edit/"})
    public ResponseEntity<String> editCategory(@RequestBody Category categoryToUpdate, HttpSession session,HttpServletRequest request) {
        return categoryService.add(categoryToUpdate, session,request);
    }
    
    /**
     * fetch-one-product endpoint
     * @param categoryId The ID of the category to fetch.
     * @return The category with given ID.
     */
    @GetMapping("/category/{id}")
    public ResponseEntity<Optional<Category>> fetchCategory(@PathVariable("id") String categoryId) {
        return categoryService.getOne(categoryId);
    }

    /**
     * delete-category endpoint
     * @param categoryId
     * @return
     */
    @DeleteMapping(value = {"/delete/{id}", "/delete/{id}/"})
    public ResponseEntity<String> deleteCategory(@PathVariable("id") Long categoryId, HttpSession session,HttpServletRequest request) {
        return categoryService.delete(categoryId, session,request);
    }

}
