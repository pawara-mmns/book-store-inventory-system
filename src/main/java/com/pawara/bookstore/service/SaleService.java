package com.pawara.bookstore.service;

import com.pawara.bookstore.dto.sale.SaleRequestDto;
import com.pawara.bookstore.dto.sale.SaleResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleService {

    SaleResponseDto createSale(SaleRequestDto saleRequestDto, String username);

    SaleResponseDto getSaleById(Long id);

    List<SaleResponseDto> getAllSales();

    List<SaleResponseDto> getSalesByUser(Long userId);

    List<SaleResponseDto> getSalesByBook(Long bookId);

    List<SaleResponseDto> getSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
