package com.pawara.bookstore.controller;

import com.pawara.bookstore.dto.author.AuthorRequestDto;
import com.pawara.bookstore.dto.author.AuthorResponseDto;
import com.pawara.bookstore.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Authors", description = "Author management APIs")
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new author", description = "Create a new author (Admin only)")
    public ResponseEntity<AuthorResponseDto> createAuthor(@Valid @RequestBody AuthorRequestDto authorRequestDto) {
        AuthorResponseDto response = authorService.createAuthor(authorRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get author by ID", description = "Retrieve an author by their ID")
    public ResponseEntity<AuthorResponseDto> getAuthorById(@PathVariable Long id) {
        AuthorResponseDto response = authorService.getAuthorById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all authors", description = "Retrieve all authors")
    public ResponseEntity<List<AuthorResponseDto>> getAllAuthors() {
        List<AuthorResponseDto> response = authorService.getAllAuthors();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update author", description = "Update an existing author (Admin only)")
    public ResponseEntity<AuthorResponseDto> updateAuthor(@PathVariable Long id, 
                                                         @Valid @RequestBody AuthorRequestDto authorRequestDto) {
        AuthorResponseDto response = authorService.updateAuthor(id, authorRequestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete author", description = "Delete an author (Admin only)")
    public ResponseEntity<String> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.ok("Author deleted successfully");
    }

    @GetMapping("/search")
    @Operation(summary = "Search authors", description = "Search authors by name")
    public ResponseEntity<List<AuthorResponseDto>> searchAuthors(@RequestParam String name) {
        List<AuthorResponseDto> response = authorService.searchAuthorsByName(name);
        return ResponseEntity.ok(response);
    }
}
