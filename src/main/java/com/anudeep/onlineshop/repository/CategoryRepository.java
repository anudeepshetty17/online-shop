package com.anudeep.onlineshop.repository;

import com.anudeep.onlineshop.model.Category;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository used by the CategoryServiceImpl
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

	Category findByCategoryType(Integer categoryTypeToFind);

	List<Category> findByOrderByCategoryTypeAsc();

	Category findById(String id);

	Category findByName(String trim);

	List<Category> findAllByNameAndIdNot(String trim, Long id);

}
