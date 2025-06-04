package com.pawara.bookstore.service;

import com.pawara.bookstore.dto.auth.JwtResponseDto;
import com.pawara.bookstore.dto.auth.LoginRequestDto;
import com.pawara.bookstore.dto.user.UserRequestDto;
import com.pawara.bookstore.dto.user.UserResponseDto;

public interface AuthService {

    JwtResponseDto login(LoginRequestDto loginRequestDto);

    UserResponseDto register(UserRequestDto userRequestDto);

    void logout(String token);
}
