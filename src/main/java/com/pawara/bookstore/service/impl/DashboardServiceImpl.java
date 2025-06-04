package com.pawara.bookstore.service.impl;

import com.pawara.bookstore.dto.dashboard.DashboardResponseDto;
import com.pawara.bookstore.entity.Book;
import com.pawara.bookstore.repository.BookRepository;
import com.pawara.bookstore.repository.SaleRepository;
import com.pawara.bookstore.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final BookRepository bookRepository;
    private final SaleRepository saleRepository;

    @Override
    public DashboardResponseDto getDashboardData() {
        log.info("Fetching dashboard data");

        // Get basic statistics
        Long totalBooks = bookRepository.countActiveBooks();
        Long totalSales = saleRepository.countTotalSales();
        BigDecimal totalRevenue = saleRepository.getTotalRevenue();
        if (totalRevenue == null) {
            totalRevenue = BigDecimal.ZERO;
        }

        // Get low stock books (threshold: 10)
        List<Book> lowStockBooks = bookRepository.findLowStockBooks(10);
        Long lowStockCount = (long) lowStockBooks.size();

        // Convert low stock books to DTOs
        List<DashboardResponseDto.LowStockBookDto> lowStockBookDtos = lowStockBooks.stream()
                .map(book -> new DashboardResponseDto.LowStockBookDto(
                        book.getTitle(),
                        book.getAuthor().getName(),
                        book.getStockQuantity()
                ))
                .collect(Collectors.toList());

        // Get most sold books
        List<Object[]> mostSoldBooksData = saleRepository.findMostSoldBooks();
        List<DashboardResponseDto.BookSalesDto> mostSoldBooks = mostSoldBooksData.stream()
                .limit(5) // Top 5 most sold books
                .map(data -> new DashboardResponseDto.BookSalesDto(
                        (String) data[0], // book title
                        (String) data[1], // author name
                        ((Number) data[2]).intValue(), // total quantity sold
                        (BigDecimal) data[3] // total revenue
                ))
                .collect(Collectors.toList());

        DashboardResponseDto dashboard = new DashboardResponseDto(
                totalBooks,
                totalSales,
                totalRevenue,
                lowStockCount,
                mostSoldBooks,
                lowStockBookDtos
        );

        log.info("Dashboard data fetched successfully");
        return dashboard;
    }
}
