package com.anudeep.onlineshop.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anudeep.onlineshop.model.Product;
import com.anudeep.onlineshop.service.CategoryService;
import com.anudeep.onlineshop.service.ProductService;

@CrossOrigin
@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	
	@Autowired
	private CategoryService categoryService;

	/**
	 * fetch-all-products endpoint
	 *
	 * NOTE: Used only for debugging or admins; this is not used in the angular
	 * project
	 *
	 * @param session The http session.
	 * @return A list of all the products.
	 */
	@GetMapping(value = { "/admin/allProducts", "/admin/allProducts/" })
	public ResponseEntity<List<Product>> fetchAllProducts(HttpSession session,HttpServletRequest request) {
		return productService.getAll(session,request);
	}

	/**
	 * fetch-one-product endpoint
	 * 
	 * @param productId The ID of the product to fetch.
	 * @return The product with given ID.
	 */
	@GetMapping("/product/{id}")
	public ResponseEntity<Optional<Product>> fetchProduct(@PathVariable("id") String productId) {
		return productService.getOne(productId);
	}

	/**
	 * fetch-all-by-category endpoint
	 * 
	 * @param categoryType The category type to filter with when fetching all
	 *                     products.
	 * @return A filtered-by-category-type list of products.
	 */
	@GetMapping("/search/category/{categoryType}")
	public ResponseEntity<List<Product>> fetchAllProductsByCategory(
			@PathVariable("categoryType") Integer categoryType) {
		return productService.getAllByCategory(categoryType);
	}

	/**
	 * fetch-all-containing-search-string endpoint
	 * 
	 * @param searchStr The search string to use to find the product(s).
	 * @return The product(s) containing the given search string.
	 */
	@GetMapping("/search/product/{searchStr}")
	public ResponseEntity<List<Product>> fetchAllProductsContainingSearchString(
			@PathVariable("searchStr") String searchStr) {
		return productService.getAllContainingSearchString(searchStr);
	}

	/**
	 * add-product endpoint
	 * 
	 * @param productToAdd The product to add.
	 * @param session      The http session.
	 * @return A confirmation or denial message.
	 */
	@PostMapping(value = { "/add", "/add/" })
	public ResponseEntity<String> addProduct(@RequestBody Product productToAdd, HttpSession session,HttpServletRequest request) {
		
		return productService.save(productToAdd, session,request);
	}

	/**
	 * edit-product endpoint
	 * 
	 * @param productToUpdate The product to update.
	 * @param session         The http session.
	 * @return A confirmation or denial message.
	 */
	@PutMapping(value = { "/edit", "/edit/" })
	public ResponseEntity<String> editProduct(@RequestBody Product productToUpdate, HttpSession session,HttpServletRequest request) {
		return productService.update(productToUpdate, session,request);
	}

	/**
	 * delete-product endpoint
	 * 
	 * @param productId The product ID of the product to delete.
	 * @param session   The http session.
	 * @return A confirmation or denial message.
	 */
	@DeleteMapping(value = { "/delete/{id}", "/delete/{id}/" })
	public ResponseEntity<String> deleteProduct(@PathVariable("id") String productId, HttpSession session,HttpServletRequest request) {
		return productService.delete(productId, session,request);
	}

}
