package com.primaelectronic.pos.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Data
@Document(collection = "bill")
public class Bill {
    @Id
    private long id;

    private String nama;
    private String nominal;
    private String ket;
    private String pic;
    private LocalDate createdAt; // created_at
    private String createdBy; // created_by
    private int status;
    private int nominalMinus; // nominalminus
    private LocalDate dueDate; // duedate

    @Transient
    public static final String SEQUENCE_NAME = "bill_sequence";
}
