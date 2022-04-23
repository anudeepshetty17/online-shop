package com.anudeep.onlineshop.model;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;

/**
 * This User class is used to represent
 * a user entity from the database table 'USERS'.
 */
@Entity
@Table(name = "users")
@Data
public class User implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    private Long id;

    @Column(name = "active")
    private boolean userActive;

    @NaturalId
    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "address1")
    private String address1;

    @Column(name = "address2")
    private String address2;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "zip")
    private String zip;

    @Column(name = "country")
    private String country;

    @Column(name = "phone")
    private String phone;

    @Column(name = "role")
    private String role;

    @Column(name = "password")
    private  String password;

    public User() {
        super();
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
