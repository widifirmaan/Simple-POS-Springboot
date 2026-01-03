package com.primaelectronic.pos.controller;

import com.primaelectronic.pos.model.Transaction;
import com.primaelectronic.pos.repository.StockRepository;
import com.primaelectronic.pos.repository.TransactionRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
public class ViewController {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private com.primaelectronic.pos.repository.InvoiceRepository invoiceRepository;

    @Autowired
    private com.primaelectronic.pos.service.SequenceGeneratorService sequenceGeneratorService;

    @GetMapping("/")
    public String index(HttpSession session) {
        if (session.getAttribute("user") != null) {
            String username = (String) session.getAttribute("username");
            if ("hamam".equals(username)) {
                return "redirect:/dashboard";
            } else {
                return "redirect:/kasir";
            }
        }
        return "index";
    }

    @GetMapping("/stock-barang")
    public String stockBarang(@RequestParam(required = false) Integer store,
            @RequestParam(required = false) String stockLimit,
            @RequestParam(required = false) String search,
            HttpSession session,
            Model model) {
        boolean isLoggedIn = session.getAttribute("user") != null;
        model.addAttribute("isLoggedIn", isLoggedIn);
        List<com.primaelectronic.pos.model.Stock> allStocks = stockRepository.findAll();

        // 1. Filter by Store
        if (store != null) {
            allStocks = allStocks.stream()
                    .filter(s -> s.getStore() == store)
                    .collect(java.util.stream.Collectors.toList());
        }

        // 2. Filter by Stock Limit
        if (stockLimit != null && !stockLimit.isEmpty()) {
            if ("less5".equals(stockLimit)) {
                allStocks = allStocks.stream()
                        .filter(s -> s.getQty() < 5)
                        .collect(java.util.stream.Collectors.toList());
            } else if ("more5".equals(stockLimit)) {
                allStocks = allStocks.stream()
                        .filter(s -> s.getQty() >= 5)
                        .collect(java.util.stream.Collectors.toList());
            }
        }

        // 3. Filter by Search
        if (search != null && !search.isEmpty()) {
            String searchLower = search.toLowerCase();
            allStocks = allStocks.stream()
                    .filter(s -> s.getName().toLowerCase().contains(searchLower))
                    .collect(java.util.stream.Collectors.toList());
        }

        // Pass params back to view for inputs
        model.addAttribute("stocks", allStocks);
        model.addAttribute("selectedStore", store);
        model.addAttribute("selectedStockLimit", stockLimit);
        model.addAttribute("search", search);

        return "stock-barang";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/";
        }

        String username = (String) session.getAttribute("username");
        if (!"hamam".equals(username)) {
            // Placeholder for kasir check, although PHP only checked if != hamam redirects
            // to kasir.
            // But if we are here and not hamam, we should go to kasir.
            return "redirect:/kasir";
        }

        Integer toko = (Integer) session.getAttribute("toko");
        if (toko == null) {
            toko = 1;
            session.setAttribute("toko", toko);
        }

        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        List<Transaction> transactions = transactionRepository.findByTokoAndDateBetween(toko, start, end);

        double labaKotor = transactions.stream().mapToDouble(Transaction::getAmount).sum();

        model.addAttribute("labaKotor", String.format("Rp. %,.0f", labaKotor));
        model.addAttribute("toko", toko);
        model.addAttribute("username", username);
        model.addAttribute("allStocks", stockRepository.findByStore(toko));
        model.addAttribute("transactionId", sequenceGeneratorService.getCurrentSequence(Transaction.SEQUENCE_NAME) + 1);

        return "dashboard";
    }

    @GetMapping("/kasir")
    public String kasir(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/";
        }

        Integer toko = (Integer) session.getAttribute("toko");
        if (toko == null) {
            toko = 1;
            session.setAttribute("toko", toko);
        }
        String username = (String) session.getAttribute("username");

        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        List<Transaction> transactions = transactionRepository.findByTokoAndDateBetween(toko, start, end);

        double labaKotor = transactions.stream().mapToDouble(Transaction::getAmount).sum();

        model.addAttribute("labaKotor", String.format("Rp. %,.0f", labaKotor));
        model.addAttribute("toko", toko);
        model.addAttribute("username", username);
        model.addAttribute("allStocks", stockRepository.findByStore(toko));
        model.addAttribute("transactionId", sequenceGeneratorService.getCurrentSequence(Transaction.SEQUENCE_NAME) + 1);

        return "kasir";
    }

    @PostMapping("/kasir/change-store")
    public String changeStoreKasir(@RequestParam int toko, HttpSession session) {
        session.setAttribute("toko", toko);
        return "redirect:/kasir";
    }

    @PostMapping("/dashboard/change-store")
    public String changeStoreDashboard(@RequestParam int toko, HttpSession session) {
        session.setAttribute("toko", toko);
        return "redirect:/dashboard";
    }

    @Autowired
    private com.primaelectronic.pos.service.BillService billService;

    @GetMapping("/bill")
    public String bill(@RequestParam(required = false) String search, HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/";
        }

        List<com.primaelectronic.pos.model.Bill> bills;
        if (search != null && !search.isEmpty()) {
            bills = billService.getBillsByName(search);
            model.addAttribute("search", search);
        } else {
            bills = billService.getAllBills();
        }

        model.addAttribute("bills", bills);
        return "bill";
    }

    @GetMapping("/finance")
    public String finance(HttpSession session, Model model,
            @RequestParam(required = false) String tgldari,
            @RequestParam(required = false) String tglsampai,
            @RequestParam(required = false, defaultValue = "0") Integer pilihtoko) {
        if (session.getAttribute("user") == null) {
            return "redirect:/";
        }

        Integer sessionToko = (Integer) session.getAttribute("toko");
        final Integer currentToko = (sessionToko == null) ? 1 : sessionToko;

        if (tgldari == null) {
            tgldari = LocalDate.now().minusDays(30).toString();
        }
        if (tglsampai == null) {
            tglsampai = LocalDate.now().toString();
        }

        LocalDateTime startFilter = LocalDate.parse(tgldari).atStartOfDay();
        LocalDateTime endFilter = LocalDate.parse(tglsampai).atTime(LocalTime.MAX);

        List<Transaction> filteredTransactions;
        if (pilihtoko == 0) {
            filteredTransactions = transactionRepository.findByDateBetween(startFilter, endFilter);
        } else {
            filteredTransactions = transactionRepository.findByTokoAndDateBetween(pilihtoko, startFilter, endFilter);
        }

        model.addAttribute("transactions", filteredTransactions);
        model.addAttribute("tgldari", tgldari);
        model.addAttribute("tglsampai", tglsampai);
        model.addAttribute("pilihtoko", pilihtoko);
        model.addAttribute("totalFiltered", filteredTransactions.stream().mapToDouble(Transaction::getAmount).sum());

        // Laba Kotor Month Logic (Current Month, Current Store)
        LocalDateTime startMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endMonth = LocalDateTime.now();
        List<Transaction> monthTransactions = transactionRepository.findByTokoAndDateBetween(currentToko, startMonth,
                endMonth);
        double labaKotorMonth = monthTransactions.stream().mapToDouble(Transaction::getAmount).sum();
        model.addAttribute("labaKotorMonth", String.format("Rp. %,.0f", labaKotorMonth));

        // Sold Items Logic
        int soldItems = 0;
        java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\((\\d+)\\)");
        for (Transaction t : monthTransactions) {
            java.util.regex.Matcher m = p.matcher(t.getProduct());
            while (m.find()) {
                soldItems += Integer.parseInt(m.group(1));
            }
        }
        model.addAttribute("soldItemsMonth", soldItems);

        // Invoices (Recent 7) - Need to add findByToko in InvoiceRepository or just
        // findAll and filter
        // For efficiency, just findAll for now or add method.
        // Assuming findByToko exists or we add it.
        // I will use findAll and stream limit for now as no findByToko in repo yet.
        List<com.primaelectronic.pos.model.Invoice> invoices = invoiceRepository.findAll().stream()
                .filter(i -> i.getToko() == currentToko)
                .sorted((i1, i2) -> i2.getTgl().compareTo(i1.getTgl())) // Descending
                .limit(7)
                .collect(java.util.stream.Collectors.toList());
        model.addAttribute("invoices", invoices);

        return "finance";
    }
}
