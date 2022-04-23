package com.anudeep.onlineshop.model;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

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

}
