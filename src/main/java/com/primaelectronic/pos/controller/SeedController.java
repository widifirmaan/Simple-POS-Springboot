package com.primaelectronic.pos.controller;

import com.primaelectronic.pos.model.Stock;
import com.primaelectronic.pos.model.User;
import com.primaelectronic.pos.repository.StockRepository;
import com.primaelectronic.pos.repository.UserRepository;
import com.primaelectronic.pos.service.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/seed")
public class SeedController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @GetMapping
    public String seedData() {
        StringBuilder result = new StringBuilder();

        if (userRepository.findByUsername("hamam").isEmpty()) {
            User admin = new User();
            admin.setId(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME));
            admin.setUsername("hamam");
            admin.setPassword("123");
            admin.setAccess(1); // Assuming 1 is admin
            admin.setStore(1);
            userRepository.save(admin);
            result.append("Admin user created (hamam/123). <br/>");
        } else {
            User admin = userRepository.findByUsername("hamam").get();
            admin.setPassword("123");
            userRepository.save(admin);
            result.append("Admin password reset to 123. <br/>");
        }

        if (userRepository.findByUsername("kasir").isEmpty()) {
            User kasir = new User();
            kasir.setId(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME));
            kasir.setUsername("kasir");
            kasir.setPassword("123");
            kasir.setAccess(2); // Assuming 2 is cashier
            kasir.setStore(1);
            userRepository.save(kasir);
            result.append("Kasir user created (kasir/123). <br/>");
        } else {
            User kasir = userRepository.findByUsername("kasir").get();
            kasir.setPassword("123");
            userRepository.save(kasir);
            result.append("Kasir password reset to 123. <br/>");
        }

        // Seed Stocks
        if (stockRepository.count() == 0) {
            createStock("Samsung Galaxy S23", 10, 12000000, 13000000, 1);
            createStock("iPhone 15 Pro", 5, 18000000, 19500000, 1);
            createStock("Xiaomi Redmi Note 12", 20, 2500000, 2800000, 1);
            createStock("Laptop Asus ROG", 3, 15000000, 16500000, 1);
            result.append("Dummy stocks created. <br/>");
        } else {
            result.append("Stocks already exist. <br/>");
        }

        return result.toString();
    }

    private void createStock(String name, int qty, double priceInit, double priceFinal, int store) {
        Stock stock = new Stock();
        stock.setId(sequenceGeneratorService.generateSequence(Stock.SEQUENCE_NAME));
        stock.setName(name);
        stock.setQty(qty);
        stock.setPriceInit(priceInit);
        stock.setPriceFinal(priceFinal);
        stock.setStore(store);
        stock.setCreatedAt(LocalDateTime.now());
        stock.setModifiedAt(LocalDateTime.now());
        stockRepository.save(stock);
    }
}
