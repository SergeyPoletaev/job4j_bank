package ru.job4j.bank.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<AccountDto> addAccount(@RequestBody Map<String, String> body) {
        String requisite = body.get("requisite");
        if (requisite == null) {
            throw new IllegalArgumentException("Parameter 'requisite' cannot be null");
        }
        String passport = body.get("passport");
        if (passport == null) {
            throw new IllegalArgumentException("Parameter 'username' cannot be null");
        }
        if (passport.length() < 10) {
            throw new IllegalArgumentException("Passport number cannot be less than 10 characters");
        }
        Account account = new Account().setRequisite(requisite);
        bankService.addAccount(passport, account);
        if (account.getId() == 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format("Account with requisites [%s, %s] not saved", requisite, passport));
        }
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new AccountDto()
                        .setUserId(account.getId())
                        .setRequisite(account.getRequisite())
                        .setBalance(account.getBalance()));
    }

    @GetMapping("/")
    public ResponseEntity<AccountDto> findByRequisite(@RequestParam String passport, @RequestParam String requisite) {
        if (passport == null) {
            throw new IllegalArgumentException("Parameter 'passport' cannot be null");
        }
        if (requisite == null) {
            throw new IllegalArgumentException("Parameter 'requisite' cannot be null");
        }
        Account account = bankService.findByRequisite(passport, requisite).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Account with requisites [%s, %s] not found", passport, requisite)));
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new AccountDto()
                        .setUserId(account.getId())
                        .setRequisite(account.getRequisite())
                        .setBalance(account.getBalance()));
    }
}
