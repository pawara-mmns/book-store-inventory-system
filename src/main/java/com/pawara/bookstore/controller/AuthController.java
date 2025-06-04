package com.pawara.bookstore.controller;

import com.pawara.bookstore.dto.auth.JwtResponseDto;
import com.pawara.bookstore.dto.auth.LoginRequestDto;
import com.pawara.bookstore.dto.user.UserRequestDto;
import com.pawara.bookstore.dto.user.UserResponseDto;
import com.pawara.bookstore.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    public ResponseEntity<JwtResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        JwtResponseDto response = authService.login(loginRequestDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Register a new user")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto response = authService.register(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "User logout", description = "Logout user")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        // Extract token from "Bearer " prefix
        String jwtToken = token.substring(7);
        authService.logout(jwtToken);
        return ResponseEntity.ok("Logged out successfully");
    }
}
