package com.pawara.bookstore.service;

import com.pawara.bookstore.dto.user.UserRequestDto;
import com.pawara.bookstore.dto.user.UserResponseDto;
import com.pawara.bookstore.enums.Role;

import java.util.List;

public interface UserService {

    UserResponseDto createUser(UserRequestDto userRequestDto);

    UserResponseDto getUserById(Long id);

    List<UserResponseDto> getAllUsers();

    List<UserResponseDto> getUsersByRole(Role role);

    UserResponseDto updateUser(Long id, UserRequestDto userRequestDto);

    void deleteUser(Long id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
