package com.pawara.bookstore.service;

import com.pawara.bookstore.dto.author.AuthorRequestDto;
import com.pawara.bookstore.dto.author.AuthorResponseDto;

import java.util.List;

public interface AuthorService {

    AuthorResponseDto createAuthor(AuthorRequestDto authorRequestDto);

    AuthorResponseDto getAuthorById(Long id);

    List<AuthorResponseDto> getAllAuthors();

    AuthorResponseDto updateAuthor(Long id, AuthorRequestDto authorRequestDto);

    void deleteAuthor(Long id);

    List<AuthorResponseDto> searchAuthorsByName(String name);
}
