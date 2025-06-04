package com.pawara.bookstore.service.impl;

import com.pawara.bookstore.dto.user.UserRequestDto;
import com.pawara.bookstore.dto.user.UserResponseDto;
import com.pawara.bookstore.entity.User;
import com.pawara.bookstore.enums.Role;
import com.pawara.bookstore.exception.ResourceNotFoundException;
import com.pawara.bookstore.exception.DuplicateResourceException;
import com.pawara.bookstore.repository.UserRepository;
import com.pawara.bookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        log.info("Creating user with username: {}", userRequestDto.getUsername());

        if (userRepository.existsByUsername(userRequestDto.getUsername())) {
            throw new DuplicateResourceException("Username already exists: " + userRequestDto.getUsername());
        }

        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + userRequestDto.getEmail());
        }

        User user = new User();
        user.setUsername(userRequestDto.getUsername());
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user.setEmail(userRequestDto.getEmail());
        user.setRole(userRequestDto.getRole());
        user.setIsDeleted(false);

        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());

        return mapToResponseDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        log.info("Fetching user with ID: {}", id);
        User user = userRepository.findActiveUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        return mapToResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        log.info("Fetching all active users");
        return userRepository.findAllActiveUsers()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getUsersByRole(Role role) {
        log.info("Fetching users with role: {}", role);
        return userRepository.findAllActiveUsersByRole(role)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        log.info("Updating user with ID: {}", id);

        User existingUser = userRepository.findActiveUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        // Check for duplicate username (excluding current user)
        if (!existingUser.getUsername().equals(userRequestDto.getUsername()) &&
            userRepository.existsByUsername(userRequestDto.getUsername())) {
            throw new DuplicateResourceException("Username already exists: " + userRequestDto.getUsername());
        }

        // Check for duplicate email (excluding current user)
        if (!existingUser.getEmail().equals(userRequestDto.getEmail()) &&
            userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + userRequestDto.getEmail());
        }

        existingUser.setUsername(userRequestDto.getUsername());
        if (userRequestDto.getPassword() != null && !userRequestDto.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        }
        existingUser.setEmail(userRequestDto.getEmail());
        existingUser.setRole(userRequestDto.getRole());

        User updatedUser = userRepository.save(existingUser);
        log.info("User updated successfully with ID: {}", updatedUser.getId());

        return mapToResponseDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);

        User user = userRepository.findActiveUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        user.setIsDeleted(true);
        userRepository.save(user);

        log.info("User soft deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private UserResponseDto mapToResponseDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }
}
