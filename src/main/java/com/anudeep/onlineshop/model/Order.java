package com.anudeep.onlineshop.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.sun.istack.NotNull;

import lombok.Data;

/**
 * This Product class is used to represent a Product entity from the database
 * table 'ORDERS'.
 */
@Entity
@Table(name = "ORDERS")
@Data
public class Order implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ORDER_ID")
	private String id;

	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private User user;

	@Column(name = "ORDER_AMOUNT")
	@NotNull
	private BigDecimal amount;
	
	@Column(name = "CREATE_TIME")
	@CreationTimestamp
	private Date createTime;

	@Column(name = "UPDATE_TIME")
	@UpdateTimestamp
	private Date updateTime;

	@Column(name = "ORDER_STATUS")
	private int orderStatus;
	
	@Column(name = "NUM_OF_ITEMS")
	private int totalItems;
}
