CREATE TABLE product_category (id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
                                category_name VARCHAR(255),
                                PRIMARY KEY (id))