package com.anudeep.onlineshop.dto;

import lombok.Data;

/**
 * This data transfer object is used only
 * for transferring emails and passwords
 * between services.
 *
 * I.e.
 * When a LoginForm object is passed as a parameter,
 * it's fields can be used to search for a user in the
 * db with those values.
 *
 * E.g. User existingUser = findByEmail(<LoginFormObject>.getEmail())
 */
@Data
public class LoginForm {

    private String email;

    private String password;

}
