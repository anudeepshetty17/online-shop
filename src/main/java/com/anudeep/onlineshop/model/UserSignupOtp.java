package com.anudeep.onlineshop.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * This User class is used to represent
 * a user entity from the database table 'USERS'.
 */
@Entity
@Table(name = "user_signup_otp")
@Data
public class UserSignupOtp implements Serializable {

    @Id
    @Column(name = "emailid",unique=true)
    private String emailId;

    
    private int otp;
    public UserSignupOtp() {
    }

   

    public UserSignupOtp(String email, int otp) {
        this.emailId = email;
        this.otp = otp;
    }

}
