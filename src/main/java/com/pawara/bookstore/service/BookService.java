package com.pawara.bookstore.service;

import com.pawara.bookstore.dto.book.BookRequestDto;
import com.pawara.bookstore.dto.book.BookResponseDto;

import java.util.List;

public interface BookService {

    BookResponseDto createBook(BookRequestDto bookRequestDto);

    BookResponseDto getBookById(Long id);

    List<BookResponseDto> getAllBooks();

    BookResponseDto updateBook(Long id, BookRequestDto bookRequestDto);

    void deleteBook(Long id);

    List<BookResponseDto> searchBooks(String keyword);

    List<BookResponseDto> getBooksByCategory(Long categoryId);

    List<BookResponseDto> getBooksByAuthor(Long authorId);

    List<BookResponseDto> getLowStockBooks(Integer threshold);

    BookResponseDto restockBook(Long id, Integer quantity);

    boolean existsByIsbn(String isbn);
}
