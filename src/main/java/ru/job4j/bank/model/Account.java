package ru.job4j.bank.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class Account extends Id {
    private String requisite;
    private double balance;
    private User user;

}
