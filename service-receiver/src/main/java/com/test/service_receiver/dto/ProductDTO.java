package com.test.service_receiver.dto;

import java.sql.Timestamp;

/**
 * Класс, представляющий объект продукта.
 * Используется для передачи данных между сервисами.
 */
public class ProductDTO {
    private int id;
    private String name;
    private Double price;
    private Timestamp timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
