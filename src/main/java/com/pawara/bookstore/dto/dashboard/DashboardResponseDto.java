package com.pawara.bookstore.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponseDto {

    private Long totalBooks;
    private Long totalSales;
    private BigDecimal totalRevenue;
    private Long lowStockBooks;
    private List<BookSalesDto> mostSoldBooks;
    private List<LowStockBookDto> lowStockBooksList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookSalesDto {
        private String bookTitle;
        private String authorName;
        private Integer totalQuantitySold;
        private BigDecimal totalRevenue;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LowStockBookDto {
        private String bookTitle;
        private String authorName;
        private Integer currentStock;
    }
}
