package ru.job4j.bank.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountDto {
    private String requisite;
    private double balance;
    private int userId;
}
