package com.effectivemobile.cardmanagement.controller;

import com.effectivemobile.cardmanagement.dto.AuthDto;
import com.effectivemobile.cardmanagement.model.entity.User;
import com.effectivemobile.cardmanagement.model.entity.UserRole;
import com.effectivemobile.cardmanagement.repository.UserRepository;
import com.effectivemobile.cardmanagement.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Простой контроллер аутентификации: регистрация и логин.
 *
 * @author Дмитрий Ельцов
 **/
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    @Operation(summary = "Регистрация пользователя USER")
    @PostMapping("/register")
    public void register(@Valid @RequestBody AuthDto.LoginRequest req) {
        if (users.existsByUserName(req.getUsername())) {
            throw new IllegalArgumentException("Пользователь уже существует");
        }
        User u = new User();
        u.setUserName(req.getUsername());
        u.setPassword(encoder.encode(req.getPassword()));
        u.setRole(UserRole.USER);
        users.save(u);
    }

    @Operation(summary = "Логин и получение JWT")
    @PostMapping("/login")
    public AuthDto.TokenResponse login(@Valid @RequestBody AuthDto.LoginRequest req) {
        User u = users.findByUserName(req.getUsername())
                .filter(x -> encoder.matches(req.getPassword(), x.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("Неверные учетные данные"));
        String token = jwt.generate(u.getUserName(), u.getRole().name());
        return new AuthDto.TokenResponse(token);
    }
}
