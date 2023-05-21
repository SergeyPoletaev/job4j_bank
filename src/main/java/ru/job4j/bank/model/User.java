package ru.job4j.bank.model;

import lombok.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class User extends Id {
    private String passport;
    private String username;
    private final List<Account> accounts = new CopyOnWriteArrayList<>();

}
