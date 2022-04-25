package com.anudeep.onlineshop.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * This Category class is used to represent
 * a Category entity from the database table 'CATEGORY'.
 */
@Entity
@Data
public class Category implements Serializable {

    @Id
    @Column(name = "CATEGORY_ID")
    private Long id;

    @Column(name = "CATEGORY_NAME")
    private String name;

    @Column(name = "CATEGORY_TYPE")
    @NaturalId
    private int categoryType;

    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "UPDATE_TIME")
    private Date updateTime;
  
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_TYPE",referencedColumnName = "CATEGORY_TYPE")  
    private List<Product> products;
    
    @Transient
    private Integer productsCount;
    
   
}
