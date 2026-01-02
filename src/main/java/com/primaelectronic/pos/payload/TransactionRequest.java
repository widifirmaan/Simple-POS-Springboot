package com.primaelectronic.pos.payload;

import lombok.Data;
import java.util.List;

@Data
public class TransactionRequest {
    private String task;
    private List<TransactionItemDTO> dataBarang;
    private String dataHTML;
    private double dataTrans; // Amount
    private String dataPayment;
}
