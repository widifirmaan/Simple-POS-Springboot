package com.primaelectronic.pos.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "user")
public class User {
    @Id
    private long id;

    private String username;
    private String password;
    private int access;
    private int store;

    @Transient
    public static final String SEQUENCE_NAME = "user_sequence";
}
