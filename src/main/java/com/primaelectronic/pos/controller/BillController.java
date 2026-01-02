package com.primaelectronic.pos.controller;

import com.primaelectronic.pos.model.Bill;
import com.primaelectronic.pos.repository.BillRepository;
import com.primaelectronic.pos.service.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @GetMapping
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    @PostMapping
    public Bill createBill(@RequestBody Bill bill) {
        bill.setId(sequenceGeneratorService.generateSequence(Bill.SEQUENCE_NAME));
        if (bill.getCreatedAt() == null) {
            bill.setCreatedAt(LocalDate.now());
        }
        return billRepository.save(bill);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bill> updateBill(@PathVariable long id, @RequestBody Bill details) {
        return billRepository.findById(id)
                .map(bill -> {
                    bill.setNama(details.getNama());
                    bill.setNominal(details.getNominal());
                    bill.setKet(details.getKet());
                    bill.setStatus(details.getStatus());
                    bill.setDueDate(details.getDueDate());
                    bill.setPic(details.getPic());
                    bill.setNominalMinus(details.getNominalMinus());
                    return ResponseEntity.ok(billRepository.save(bill));
                }).orElse(ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBill(@PathVariable long id) {
        return billRepository.findById(id)
                .map(bill -> {
                    billRepository.delete(bill);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
