package com.primaelectronic.pos.service;

import com.primaelectronic.pos.model.Bill;
import com.primaelectronic.pos.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Override
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    @Override
    public Bill createBill(Bill bill) {
        bill.setId(sequenceGeneratorService.generateSequence(Bill.SEQUENCE_NAME));
        if (bill.getCreatedAt() == null) {
            bill.setCreatedAt(LocalDate.now());
        }
        return billRepository.save(bill);
    }

    @Override
    public Bill updateBill(long id, Bill details) {
        return billRepository.findById(id).map(bill -> {
            bill.setNama(details.getNama());
            bill.setNominal(details.getNominal());
            bill.setKet(details.getKet());
            bill.setStatus(details.getStatus());
            bill.setDueDate(details.getDueDate());
            bill.setPic(details.getPic());
            bill.setNominalMinus(details.getNominalMinus());
            return billRepository.save(bill);
        }).orElse(null);
    }

    @Override
    public void deleteBill(long id) {
        billRepository.deleteById(id);
    }
}
