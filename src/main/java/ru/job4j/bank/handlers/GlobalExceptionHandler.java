package ru.job4j.bank.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ObjectMapper objectMapper;

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public void exceptionHandler(Exception e, HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setContentType("application/json");
        res.getWriter().write(objectMapper.writeValueAsString(
                Map.of("message", e.getMessage(), "type", e.getClass())
        ));
        log.error(e.getLocalizedMessage());
    }
}
