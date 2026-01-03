package com.primaelectronic.pos.service;

import com.primaelectronic.pos.model.Bill;
import java.util.List;

public interface BillService {
    List<Bill> getAllBills();

    Bill createBill(Bill bill);

    Bill updateBill(long id, Bill details);

    void deleteBill(long id);

    List<Bill> getBillsByName(String name);
}
