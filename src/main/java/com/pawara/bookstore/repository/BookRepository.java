package com.pawara.bookstore.repository;

import com.pawara.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE b.isDeleted = false")
    List<Book> findAllActiveBooks();

    @Query("SELECT b FROM Book b WHERE b.isDeleted = false AND b.id = :id")
    Optional<Book> findActiveBookById(Long id);

    @Query("SELECT b FROM Book b WHERE b.isDeleted = false AND b.isbn = :isbn")
    Optional<Book> findActiveBookByIsbn(String isbn);

    @Query("SELECT b FROM Book b WHERE b.isDeleted = false AND " +
           "(LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.author.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.category.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Book> searchActiveBooks(@Param("keyword") String keyword);

    @Query("SELECT b FROM Book b WHERE b.isDeleted = false AND b.category.id = :categoryId")
    List<Book> findActiveBooksByCategory(Long categoryId);

    @Query("SELECT b FROM Book b WHERE b.isDeleted = false AND b.author.id = :authorId")
    List<Book> findActiveBooksByAuthor(Long authorId);

    @Query("SELECT b FROM Book b WHERE b.isDeleted = false AND b.stockQuantity <= :threshold")
    List<Book> findLowStockBooks(@Param("threshold") Integer threshold);

    @Query("SELECT COUNT(b) FROM Book b WHERE b.isDeleted = false")
    Long countActiveBooks();

    boolean existsByIsbnAndIsDeletedFalse(String isbn);
}
