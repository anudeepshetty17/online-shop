package com.anudeep.onlineshop.model;

import com.sun.istack.NotNull;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * This Product class is used to represent
 * a Product entity from the database table 'PRODUCTS'.
 */
@Entity
@Table(name = "products")
@Data
public class Product implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "PRODUCT_ID")
    private String id;

    @Column(name = "CATEGORY_TYPE", nullable = false)
    @ColumnDefault("0")
    private Integer categoryType;

    @Column(name = "CREATE_TIME")
    @CreationTimestamp
    private Date createTime;

    @Column(name = "PRODUCT_DESCRIPTION")
    private String description;

    @Column(name = "PRODUCT_ICON")
    private String imageUrl;

    @Column(name = "PRODUCT_NAME")
    @NotNull
    private String name;

    @Column(name = "PRODUCT_PRICE")
    @NotNull
    private BigDecimal price;

    @Column(name = "PRODUCT_STATUS")
    private int status;

    @Column(name = "PRODUCT_STOCK")
    @NotNull
    private int stock;

    @Column(name = "UPDATE_TIME")
    @UpdateTimestamp
    private Date updateTime;

}
