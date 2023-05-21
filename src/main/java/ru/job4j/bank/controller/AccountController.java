package ru.job4j.bank.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.bank.dto.AccountDto;
import ru.job4j.bank.model.Account;
import ru.job4j.bank.service.BankService;

import java.util.Map;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final BankService bankService;

    @PostMapping("/")
    public AccountDto addAccount(@RequestBody Map<String, String> body) {
        String requisite = body.get("requisite");
        String passport = body.get("passport");
        Account account = new Account().setRequisite(requisite);
        bankService.addAccount(passport, account);
        if (account.getId() == 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Account not saved");
        }
        return new AccountDto()
                .setUserId(account.getId())
                .setRequisite(account.getRequisite())
                .setBalance(account.getBalance());
    }

    @GetMapping("/")
    public AccountDto findByRequisite(@RequestParam String passport, @RequestParam String requisite) {
        Account account = bankService.findByRequisite(passport, requisite).orElseThrow();
        return new AccountDto()
                .setUserId(account.getId())
                .setRequisite(account.getRequisite())
                .setBalance(account.getBalance());
    }
}
