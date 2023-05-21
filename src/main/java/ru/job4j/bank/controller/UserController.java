package ru.job4j.bank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.bank.dto.UserDto;
import ru.job4j.bank.model.Id;
import ru.job4j.bank.model.User;
import ru.job4j.bank.service.BankService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
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
        if (passport.length() < 10) {
            throw new IllegalArgumentException("Passport number cannot be less than 10 characters");
        }
        User user = new User().setUsername(body.get("username")).setPassport(passport);
        bankService.addUser(user);
        if (user.getId() == 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User not saved");
        }
        return new UserDto()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setPassport(user.getPassport());
    }

    @GetMapping("/")
    public UserDto findByPassport(@RequestParam String passport) {
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

    @ExceptionHandler(value = IllegalArgumentException.class)
    public void exceptionHandler(Exception e, HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setContentType("application/json");
        res.getWriter().write(objectMapper.writeValueAsString(
                Map.of("message", e.getMessage(), "type", e.getClass())));
        log.error(e.getLocalizedMessage());
    }
}
