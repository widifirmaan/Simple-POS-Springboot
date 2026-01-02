package com.primaelectronic.pos.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "stock")
public class Stock {
    @Id
    private long id;

    private String name;
    private int qty;
    private double priceInit; // priceinit
    private double priceFinal; // pricefinal
    private int store;

    private LocalDateTime createdAt; // created_at
    private LocalDateTime modifiedAt; // modified_at
    private String modifiedBy; // modified_by
    private String pic;

    @Transient
    public static final String SEQUENCE_NAME = "stock_sequence";
}
