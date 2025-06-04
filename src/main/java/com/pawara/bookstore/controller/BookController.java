package com.pawara.bookstore.controller;

import com.pawara.bookstore.dto.book.BookRequestDto;
import com.pawara.bookstore.dto.book.BookResponseDto;
import com.pawara.bookstore.service.BookService;
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
@RequestMapping("/api/books")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Books", description = "Book management APIs")
public class BookController {

    private final BookService bookService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new book", description = "Create a new book (Admin only)")
    public ResponseEntity<BookResponseDto> createBook(@Valid @RequestBody BookRequestDto bookRequestDto) {
        BookResponseDto response = bookService.createBook(bookRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Retrieve a book by its ID")
    public ResponseEntity<BookResponseDto> getBookById(@PathVariable Long id) {
        BookResponseDto response = bookService.getBookById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all books", description = "Retrieve all books")
    public ResponseEntity<List<BookResponseDto>> getAllBooks() {
        List<BookResponseDto> response = bookService.getAllBooks();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update book", description = "Update an existing book (Admin only)")
    public ResponseEntity<BookResponseDto> updateBook(@PathVariable Long id, 
                                                     @Valid @RequestBody BookRequestDto bookRequestDto) {
        BookResponseDto response = bookService.updateBook(id, bookRequestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete book", description = "Delete a book (Admin only)")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok("Book deleted successfully");
    }

    @GetMapping("/search")
    @Operation(summary = "Search books", description = "Search books by keyword")
    public ResponseEntity<List<BookResponseDto>> searchBooks(@RequestParam String keyword) {
        List<BookResponseDto> response = bookService.searchBooks(keyword);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get books by category", description = "Retrieve books by category")
    public ResponseEntity<List<BookResponseDto>> getBooksByCategory(@PathVariable Long categoryId) {
        List<BookResponseDto> response = bookService.getBooksByCategory(categoryId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/author/{authorId}")
    @Operation(summary = "Get books by author", description = "Retrieve books by author")
    public ResponseEntity<List<BookResponseDto>> getBooksByAuthor(@PathVariable Long authorId) {
        List<BookResponseDto> response = bookService.getBooksByAuthor(authorId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get low stock books", description = "Retrieve books with low stock (Admin only)")
    public ResponseEntity<List<BookResponseDto>> getLowStockBooks(@RequestParam(defaultValue = "10") Integer threshold) {
        List<BookResponseDto> response = bookService.getLowStockBooks(threshold);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/restock")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Restock book", description = "Add stock to a book (Admin only)")
    public ResponseEntity<BookResponseDto> restockBook(@PathVariable Long id, @RequestParam Integer quantity) {
        BookResponseDto response = bookService.restockBook(id, quantity);
        return ResponseEntity.ok(response);
    }
}
