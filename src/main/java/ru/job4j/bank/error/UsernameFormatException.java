package ru.job4j.bank.error;

public class UsernameFormatException extends RuntimeException {

    public UsernameFormatException(String message) {
        super(message);
    }
}
