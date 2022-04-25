package com.anudeep.onlineshop.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author anude
 *
 */
@Entity
@Table(name = "CART_ITEMS")
@Getter
@Setter
public class CartItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "QUANTITY")
    private int quantity;

}
