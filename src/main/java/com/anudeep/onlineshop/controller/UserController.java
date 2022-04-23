package com.anudeep.onlineshop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anudeep.onlineshop.dto.LoginForm;
import com.anudeep.onlineshop.model.User;
import com.anudeep.onlineshop.service.UserService;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
	
	

    @Autowired
    private UserService userService;
  
    /**
     * fetch-all-users endpoint
     *
     * NOTE: Used only for debugging or admins; this
     * is not used in the angular project
     * @return
     */
    @GetMapping(value = {"", "/"})
    public ResponseEntity<List<User>> fetchAllUsers(HttpSession session,HttpServletRequest request) {
        return userService.getAll(session,request);
    }

    /**
     * fetch-one-user endpoint
     * @param email
     * @return
     */
    @GetMapping(value = {"/user/{email}", "/user/{email}/"})
    public ResponseEntity<User> fetchUser(@PathVariable("email") String email, HttpSession session,HttpServletRequest request) {
        return userService.getOne(email, session,request);
    }

    @GetMapping(value = {"/user/activeSession", "/user/activeSession/"})
    public ResponseEntity<User> fetchSessionUser(HttpSession session,HttpServletRequest request) {
        return userService.getSessionUser(session,request);
    }

    /**
     * register-user endpoint
     * @param user
     * @return
     */
    @PostMapping(value = {"/register", "/register/"})
    public ResponseEntity<User> registerUser(@RequestBody User user, HttpSession session,HttpServletRequest request) {
        return userService.register(user, session,request);
    }

    /**
     * update-user endpoint
     * @param user
     * @return
     */
    @PutMapping(value = {"/user/update", "/user/update/"})
    public ResponseEntity<User> updateUser(@RequestBody User user, HttpSession session,HttpServletRequest request) {
        return userService.update(user, session,request);
    }

    @DeleteMapping(value = {"/user/delete/{id}", "/user/delete/{id}/"})
    public ResponseEntity<User> deleteUser(@PathVariable("id") String email, HttpSession session,HttpServletRequest request) {
        return userService.delete(email, session,request);
    }

    /**
     * login-existing-user endpoint
     * @param credentials
     * @param session
     * @return An http status response and user details to verify/deny login attempt.
     */
    @PostMapping(value = {"/user/login", "/user/login/"})
    public ResponseEntity<User> login(@RequestBody final LoginForm credentials, HttpSession session,HttpServletRequest request) {
        return userService.login(credentials, session,request);
    }

    /**
     * logout-existing-session endpoint
     * @param session
     * @return An http status response to verify session termination.
     */
    @GetMapping(value = {"/user/logout", "/user/logout/"})
    public ResponseEntity<String> logout(HttpSession session,HttpServletRequest request) {
        return userService.logout(session,request);
    }

    /**
     * is-logged-in verification endpoint
     * @param session
     * @return
     */
    @GetMapping(value = {"user/loggedIn"})
    public boolean isLoggedIn(HttpSession session,HttpServletRequest request) {
        
        return !ObjectUtils.isEmpty(request.getHeader("Authorization"));
    }

    /**
     * login-test endpoint; only used for debugging
     * @param request
     * @return A message log.
     */
    @GetMapping(value = {"/user/home", "/user/home/"})
    public String testHome(HttpServletRequest request) {

        // the message to return
        String message = "not logged in";

        // is there an active session with a user?
        if (request.getSession() != null && request.getSession().getAttribute("user") != null) {
            // yes, there is; modify the message to say so
            message = request.getSession().getAttribute("user").toString();
        }

        return message;
    }
      
    @GetMapping("/activateUser/{otp}") 
    public ResponseEntity<String> activateUser(@PathVariable("otp") int otp) {
        
    	String msg = userService.activateUser(otp);
    	if(msg.contains("Success")) {
    		return new ResponseEntity(msg, HttpStatus.OK);
    	}
        else if(msg.contains("Invalid")) {
        	return new ResponseEntity(msg, HttpStatus.CONFLICT);
        }else {
        	return new ResponseEntity(msg, HttpStatus.FOUND);
        }
    	
    }
 // Add an User
 	@PostMapping(value = "/createUser", consumes = MediaType.APPLICATION_JSON_VALUE)
 	public ResponseEntity<String> createUser(@RequestBody @Valid final User s, HttpServletRequest request) {
 		try {
 			String msg = userService.createUser(s,request);
 			if(msg.contains("Success")) {
 				return new ResponseEntity("An email has been sent with activation link, please check", HttpStatus.OK);
 			}
 			else
 				return new ResponseEntity(msg, HttpStatus.CONFLICT);
 		} catch (Exception e) {
 			e.printStackTrace();
 			return new ResponseEntity("Error in creatung User\n" + e.getMessage(),
 					HttpStatus.INTERNAL_SERVER_ERROR);
 		}
 	}	


}
