package com.anudeep.onlineshop.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;

import com.anudeep.onlineshop.dto.LoginForm;
import com.anudeep.onlineshop.model.User;

/**
 * The methods that must be implemented. Typically,
 * by a class of the same name with an 'Impl' suffix.
 *
 * Used for abstraction.
 */
public interface UserService {

    ResponseEntity<List<User>> getAll(HttpSession session,HttpServletRequest request); 

    ResponseEntity<User> getOne(String email, HttpSession session,HttpServletRequest request);

    ResponseEntity<User> getSessionUser(HttpSession session, HttpServletRequest request);

    ResponseEntity<User> register(User userToRegister, HttpSession session,HttpServletRequest request);

    ResponseEntity<User> update(User userToUpdate, HttpSession session,HttpServletRequest request);

    ResponseEntity<User> delete(String email, HttpSession session,HttpServletRequest request);

    ResponseEntity<User> login(LoginForm credentials, HttpSession session,HttpServletRequest request);

    ResponseEntity<String> logout(HttpSession session,HttpServletRequest request); 
    
    
    String createUser(User user,HttpServletRequest  request);

	String activateUser(int otp);

}
