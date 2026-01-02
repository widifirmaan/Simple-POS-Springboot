package com.primaelectronic.pos.controller;

import com.primaelectronic.pos.model.Bill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private com.primaelectronic.pos.service.BillService billService;

    @GetMapping
    public List<Bill> getAllBills() {
        return billService.getAllBills();
    }

    @PostMapping
    public Bill createBill(@RequestBody Bill bill) {
        return billService.createBill(bill);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bill> updateBill(@PathVariable long id, @RequestBody Bill details) {
        Bill updatedBill = billService.updateBill(id, details);
        if (updatedBill != null) {
            return ResponseEntity.ok(updatedBill);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBill(@PathVariable long id) {
        billService.deleteBill(id);
        return ResponseEntity.ok().build();
    }
}
