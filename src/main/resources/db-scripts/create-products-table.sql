CREATE TABLE products (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    sku VARCHAR(255),
    name VARCHAR(255),
    description VARCHAR(255),
    unit_price DECIMAL(13,2),
    image_url VARCHAR(255),
    active BOOLEAN DEFAULT TRUE,
    units_in_stock INT,
    date_created DATE,
    last_updated DATE,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES product_category(id)
)