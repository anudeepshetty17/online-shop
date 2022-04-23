package com.anudeep.onlineshop.repository;

import com.anudeep.onlineshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User deleteByEmail(String email);

}
