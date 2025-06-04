package com.pawara.bookstore.service.impl;

import com.pawara.bookstore.dto.book.BookRequestDto;
import com.pawara.bookstore.dto.book.BookResponseDto;
import com.pawara.bookstore.entity.Author;
import com.pawara.bookstore.entity.Book;
import com.pawara.bookstore.entity.Category;
import com.pawara.bookstore.exception.DuplicateResourceException;
import com.pawara.bookstore.exception.ResourceNotFoundException;
import com.pawara.bookstore.repository.AuthorRepository;
import com.pawara.bookstore.repository.BookRepository;
import com.pawara.bookstore.repository.CategoryRepository;
import com.pawara.bookstore.service.BookService;
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
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public BookResponseDto createBook(BookRequestDto bookRequestDto) {
        log.info("Creating book with title: {}", bookRequestDto.getTitle());

        if (bookRepository.existsByIsbnAndIsDeletedFalse(bookRequestDto.getIsbn())) {
            throw new DuplicateResourceException("Book with ISBN already exists: " + bookRequestDto.getIsbn());
        }

        Author author = authorRepository.findActiveAuthorById(bookRequestDto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with ID: " + bookRequestDto.getAuthorId()));

        Category category = categoryRepository.findActiveCategoryById(bookRequestDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + bookRequestDto.getCategoryId()));

        Book book = new Book();
        book.setTitle(bookRequestDto.getTitle());
        book.setAuthor(author);
        book.setCategory(category);
        book.setIsbn(bookRequestDto.getIsbn());
        book.setPrice(bookRequestDto.getPrice());
        book.setPublishedYear(bookRequestDto.getPublishedYear());
        book.setStockQuantity(bookRequestDto.getStockQuantity());
        book.setDescription(bookRequestDto.getDescription());
        book.setIsDeleted(false);

        Book savedBook = bookRepository.save(book);
        log.info("Book created successfully with ID: {}", savedBook.getId());

        return mapToResponseDto(savedBook);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponseDto getBookById(Long id) {
        log.info("Fetching book with ID: {}", id);
        Book book = bookRepository.findActiveBookById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));
        return mapToResponseDto(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDto> getAllBooks() {
        log.info("Fetching all active books");
        return bookRepository.findAllActiveBooks()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookResponseDto updateBook(Long id, BookRequestDto bookRequestDto) {
        log.info("Updating book with ID: {}", id);

        Book existingBook = bookRepository.findActiveBookById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));

        // Check for duplicate ISBN (excluding current book)
        if (!existingBook.getIsbn().equals(bookRequestDto.getIsbn()) &&
            bookRepository.existsByIsbnAndIsDeletedFalse(bookRequestDto.getIsbn())) {
            throw new DuplicateResourceException("Book with ISBN already exists: " + bookRequestDto.getIsbn());
        }

        Author author = authorRepository.findActiveAuthorById(bookRequestDto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with ID: " + bookRequestDto.getAuthorId()));

        Category category = categoryRepository.findActiveCategoryById(bookRequestDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + bookRequestDto.getCategoryId()));

        existingBook.setTitle(bookRequestDto.getTitle());
        existingBook.setAuthor(author);
        existingBook.setCategory(category);
        existingBook.setIsbn(bookRequestDto.getIsbn());
        existingBook.setPrice(bookRequestDto.getPrice());
        existingBook.setPublishedYear(bookRequestDto.getPublishedYear());
        existingBook.setStockQuantity(bookRequestDto.getStockQuantity());
        existingBook.setDescription(bookRequestDto.getDescription());

        Book updatedBook = bookRepository.save(existingBook);
        log.info("Book updated successfully with ID: {}", updatedBook.getId());

        return mapToResponseDto(updatedBook);
    }

    @Override
    public void deleteBook(Long id) {
        log.info("Deleting book with ID: {}", id);

        Book book = bookRepository.findActiveBookById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));

        book.setIsDeleted(true);
        bookRepository.save(book);

        log.info("Book soft deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDto> searchBooks(String keyword) {
        log.info("Searching books with keyword: {}", keyword);
        return bookRepository.searchActiveBooks(keyword)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDto> getBooksByCategory(Long categoryId) {
        log.info("Fetching books by category ID: {}", categoryId);
        return bookRepository.findActiveBooksByCategory(categoryId)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDto> getBooksByAuthor(Long authorId) {
        log.info("Fetching books by author ID: {}", authorId);
        return bookRepository.findActiveBooksByAuthor(authorId)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDto> getLowStockBooks(Integer threshold) {
        log.info("Fetching low stock books with threshold: {}", threshold);
        return bookRepository.findLowStockBooks(threshold)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookResponseDto restockBook(Long id, Integer quantity) {
        log.info("Restocking book with ID: {} by quantity: {}", id, quantity);

        Book book = bookRepository.findActiveBookById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));

        book.setStockQuantity(book.getStockQuantity() + quantity);
        Book updatedBook = bookRepository.save(book);

        log.info("Book restocked successfully. New stock: {}", updatedBook.getStockQuantity());
        return mapToResponseDto(updatedBook);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByIsbn(String isbn) {
        return bookRepository.existsByIsbnAndIsDeletedFalse(isbn);
    }

    private BookResponseDto mapToResponseDto(Book book) {
        return new BookResponseDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor().getName(),
                book.getCategory().getName(),
                book.getIsbn(),
                book.getPrice(),
                book.getPublishedYear(),
                book.getStockQuantity(),
                book.getDescription(),
                book.getCreatedAt(),
                book.getUpdatedAt()
        );
    }
}
