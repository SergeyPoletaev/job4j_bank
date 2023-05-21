package ru.job4j.bank.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.job4j.bank.service.BankService;

import java.util.Map;

@RestController
@RequestMapping("/bank")
@RequiredArgsConstructor
public class BankController {
    private final BankService bankService;

    @PostMapping("/")
    public void transfer(@RequestBody Map<String, String> body) {
        String srcPassport = body.get("srcPassport");
        String srcRequisite = body.get("srcRequisite");
        String destPassport = body.get("destPassport");
        String destRequisite = body.get("destRequisite");
        double amount = Double.parseDouble(body.get("amount"));
        bankService.transferMoney(srcPassport, srcRequisite, destPassport, destRequisite, amount);
    }
}
