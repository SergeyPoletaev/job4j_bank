package ru.job4j.bank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.bank.dto.UserDto;
import ru.job4j.bank.error.UsernameFormatException;
import ru.job4j.bank.model.Id;
import ru.job4j.bank.model.User;
import ru.job4j.bank.service.BankService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final BankService bankService;
    private final ObjectMapper objectMapper;

    @PostMapping("/")
    public UserDto save(@RequestBody Map<String, String> body) {
        String passport = body.get("passport");
        if (passport == null) {
            throw new IllegalArgumentException("Parameter 'passport' cannot be null");
        }
        if (passport.length() < 10) {
            throw new IllegalArgumentException("Passport number cannot be less than 10 characters");
        }
        String username = body.get("username");
        if (username == null) {
            throw new IllegalArgumentException("Parameter 'username' cannot be null");
        }
        if (!username.startsWith("mr")) {
            throw new UsernameFormatException("Parameter 'username' must start with 'mr'");
        }
        User user = new User().setUsername(username).setPassport(passport);
        bankService.addUser(user);
        if (user.getId() == 0) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format("User with params [%s, %s] not saved", username, passport));
        }
        return new UserDto()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setPassport(user.getPassport());
    }

    @GetMapping("/")
    public UserDto findByPassport(@RequestParam String passport) {
        if (passport == null) {
            throw new IllegalArgumentException("Parameter 'passport' cannot be null");
        }
        User user = bankService.findByPassport(passport)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND, String.format("Passport: [%s] not found", passport)));
        return new UserDto()
                .setId(user.getId())
                .setPassport(user.getPassport())
                .setUsername(user.getUsername())
                .setAccountsIds(user.getAccounts().stream().map(Id::getId).toList());
    }

    @ExceptionHandler(value = UsernameFormatException.class)
    public void exceptionHandler(Exception e, HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setContentType("application/json");
        res.getWriter().write(objectMapper.writeValueAsString(
                Map.of("message", e.getMessage(), "type", e.getClass())));
        log.error(e.getLocalizedMessage());
    }
}
