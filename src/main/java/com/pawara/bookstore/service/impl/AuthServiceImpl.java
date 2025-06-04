package com.pawara.bookstore.service.impl;

import com.pawara.bookstore.dto.auth.JwtResponseDto;
import com.pawara.bookstore.dto.auth.LoginRequestDto;
import com.pawara.bookstore.dto.user.UserRequestDto;
import com.pawara.bookstore.dto.user.UserResponseDto;
import com.pawara.bookstore.entity.User;
import com.pawara.bookstore.service.AuthService;
import com.pawara.bookstore.service.UserService;
import com.pawara.bookstore.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Override
    public JwtResponseDto login(LoginRequestDto loginRequestDto) {
        log.info("Attempting login for user: {}", loginRequestDto.getUsername());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getUsername(),
                            loginRequestDto.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = (User) userDetails;

            String token = jwtUtil.generateToken(userDetails);

            log.info("Login successful for user: {}", loginRequestDto.getUsername());

            return new JwtResponseDto(
                    token,
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole()
            );

        } catch (BadCredentialsException e) {
            log.error("Login failed for user: {} - Invalid credentials", loginRequestDto.getUsername());
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Override
    public UserResponseDto register(UserRequestDto userRequestDto) {
        log.info("Registering new user: {}", userRequestDto.getUsername());
        return userService.createUser(userRequestDto);
    }

    @Override
    public void logout(String token) {
        // In a real application, you might want to blacklist the token
        // For now, we'll just log the logout
        log.info("User logged out");
    }
}
