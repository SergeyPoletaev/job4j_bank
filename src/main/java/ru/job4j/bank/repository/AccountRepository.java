package ru.job4j.bank.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.bank.model.Account;

import java.util.Optional;

@Repository
public class AccountRepository extends Store<Account> {

    public Optional<Account> findByRequisite(String passport, String requisite) {
        return findAll().stream()
                .filter(account ->
                        account.getRequisite().equals(requisite)
                                && account.getUser().getPassport().equals(passport))
                .findFirst();
    }
}
