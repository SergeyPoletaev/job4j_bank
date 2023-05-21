package ru.job4j.bank.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserDto {
    private int id;
    private String passport;
    private String username;
    List<Integer> accountsIds;
}
