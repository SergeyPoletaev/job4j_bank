package ru.job4j.bank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.bank.model.Account;
import ru.job4j.bank.model.User;
import ru.job4j.bank.repository.AccountRepository;
import ru.job4j.bank.repository.UserRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void addAccount(String passport, Account account) {
        userRepository.findByPassport(passport)
                .ifPresent(user -> {
                    accountRepository.save(account.setUser(user));
                    user.getAccounts().add(account);
                });
    }

    @Override
    public Optional<User> findByPassport(String passport) {
        return userRepository.findByPassport(passport);
    }

    @Override
    public Optional<Account> findByRequisite(String passport, String requisite) {
        return accountRepository.findByRequisite(passport, requisite);
    }

    @Override
    public boolean transferMoney(String srcPassport,
                                 String srcRequisite,
                                 String destPassport,
                                 String destRequisite,
                                 double amount) {
        Optional<Account> srcAccount = accountRepository.findByRequisite(srcPassport, srcRequisite);
        if (srcAccount.isEmpty() || srcAccount.get().getBalance() < amount) {
            return false;
        }
        Optional<Account> destAccount = accountRepository.findByRequisite(destPassport, destRequisite);
        if (destAccount.isEmpty()) {
            return false;
        }
        srcAccount.get().setBalance(srcAccount.get().getBalance() - amount);
        destAccount.get().setBalance(destAccount.get().getBalance() + amount);
        return true;
    }
}
