package com.pawara.bookstore.service.impl;

import com.pawara.bookstore.dto.author.AuthorRequestDto;
import com.pawara.bookstore.dto.author.AuthorResponseDto;
import com.pawara.bookstore.entity.Author;
import com.pawara.bookstore.exception.ResourceNotFoundException;
import com.pawara.bookstore.repository.AuthorRepository;
import com.pawara.bookstore.service.AuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public AuthorResponseDto createAuthor(AuthorRequestDto authorRequestDto) {
        log.info("Creating author with name: {}", authorRequestDto.getName());

        Author author = new Author();
        author.setName(authorRequestDto.getName());
        author.setBiography(authorRequestDto.getBiography());
        author.setIsDeleted(false);

        Author savedAuthor = authorRepository.save(author);
        log.info("Author created successfully with ID: {}", savedAuthor.getId());

        return mapToResponseDto(savedAuthor);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorResponseDto getAuthorById(Long id) {
        log.info("Fetching author with ID: {}", id);
        Author author = authorRepository.findActiveAuthorById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with ID: " + id));
        return mapToResponseDto(author);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorResponseDto> getAllAuthors() {
        log.info("Fetching all active authors");
        return authorRepository.findAllActiveAuthors()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorResponseDto updateAuthor(Long id, AuthorRequestDto authorRequestDto) {
        log.info("Updating author with ID: {}", id);

        Author existingAuthor = authorRepository.findActiveAuthorById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with ID: " + id));

        existingAuthor.setName(authorRequestDto.getName());
        existingAuthor.setBiography(authorRequestDto.getBiography());

        Author updatedAuthor = authorRepository.save(existingAuthor);
        log.info("Author updated successfully with ID: {}", updatedAuthor.getId());

        return mapToResponseDto(updatedAuthor);
    }

    @Override
    public void deleteAuthor(Long id) {
        log.info("Deleting author with ID: {}", id);

        Author author = authorRepository.findActiveAuthorById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with ID: " + id));

        author.setIsDeleted(true);
        authorRepository.save(author);

        log.info("Author soft deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorResponseDto> searchAuthorsByName(String name) {
        log.info("Searching authors with name containing: {}", name);
        return authorRepository.findActiveAuthorsByNameContaining(name)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    private AuthorResponseDto mapToResponseDto(Author author) {
        int bookCount = author.getBooks() != null ? 
                (int) author.getBooks().stream().filter(book -> !book.getIsDeleted()).count() : 0;
        
        return new AuthorResponseDto(
                author.getId(),
                author.getName(),
                author.getBiography(),
                bookCount
        );
    }
}
