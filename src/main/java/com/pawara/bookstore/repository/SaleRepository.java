package com.pawara.bookstore.repository;

import com.pawara.bookstore.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT s FROM Sale s ORDER BY s.soldDate DESC")
    List<Sale> findAllSalesOrderByDateDesc();

    @Query("SELECT s FROM Sale s WHERE s.soldBy.id = :userId ORDER BY s.soldDate DESC")
    List<Sale> findSalesByUser(@Param("userId") Long userId);

    @Query("SELECT s FROM Sale s WHERE s.book.id = :bookId ORDER BY s.soldDate DESC")
    List<Sale> findSalesByBook(@Param("bookId") Long bookId);

    @Query("SELECT COUNT(s) FROM Sale s")
    Long countTotalSales();

    @Query("SELECT SUM(s.totalAmount) FROM Sale s")
    BigDecimal getTotalRevenue();

    @Query("SELECT s.book.title, s.book.author.name, SUM(s.quantity), SUM(s.totalAmount) " +
           "FROM Sale s " +
           "GROUP BY s.book.id, s.book.title, s.book.author.name " +
           "ORDER BY SUM(s.quantity) DESC")
    List<Object[]> findMostSoldBooks();

    @Query("SELECT s FROM Sale s WHERE s.soldDate BETWEEN :startDate AND :endDate ORDER BY s.soldDate DESC")
    List<Sale> findSalesByDateRange(@Param("startDate") LocalDateTime startDate, 
                                   @Param("endDate") LocalDateTime endDate);
}
