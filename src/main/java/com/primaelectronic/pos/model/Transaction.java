package com.primaelectronic.pos.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "transaction")
public class Transaction {
    @Id
    private long id;

    private LocalDateTime date;
    private int amount;
    private String product;
    private String userId; // user_id
    private int toko;
    private String payment;

    @Transient
    public static final String SEQUENCE_NAME = "transaction_sequence";
}
