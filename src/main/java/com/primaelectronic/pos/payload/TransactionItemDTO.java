package com.primaelectronic.pos.payload;

import lombok.Data;

@Data
public class TransactionItemDTO {
    private String idbarang;
    private int qty;
}
