package com.primaelectronic.pos.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "invoice")
public class Invoice {
    @Id
    private String id; // MongoDB default ID or generated

    private String databarang; // JSON string of items
    private String nota; // HTML content
    private double money;
    private int toko;
    private LocalDateTime tgl;
    private String username;
    private String payment;
}
