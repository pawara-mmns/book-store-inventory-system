package com.pawara.bookstore.controller;

import com.pawara.bookstore.dto.sale.SaleRequestDto;
import com.pawara.bookstore.dto.sale.SaleResponseDto;
import com.pawara.bookstore.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Sales", description = "Sales management APIs")
public class SaleController {

    private final SaleService saleService;

    @PostMapping
    @Operation(summary = "Create a new sale", description = "Record a new sale transaction")
    public ResponseEntity<SaleResponseDto> createSale(@Valid @RequestBody SaleRequestDto saleRequestDto,
                                                     Authentication authentication) {
        String username = authentication.getName();
        SaleResponseDto response = saleService.createSale(saleRequestDto, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get sale by ID", description = "Retrieve a sale by its ID")
    public ResponseEntity<SaleResponseDto> getSaleById(@PathVariable Long id) {
        SaleResponseDto response = saleService.getSaleById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all sales", description = "Retrieve all sales")
    public ResponseEntity<List<SaleResponseDto>> getAllSales() {
        List<SaleResponseDto> response = saleService.getAllSales();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get sales by user", description = "Retrieve sales by user ID")
    public ResponseEntity<List<SaleResponseDto>> getSalesByUser(@PathVariable Long userId) {
        List<SaleResponseDto> response = saleService.getSalesByUser(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/book/{bookId}")
    @Operation(summary = "Get sales by book", description = "Retrieve sales by book ID")
    public ResponseEntity<List<SaleResponseDto>> getSalesByBook(@PathVariable Long bookId) {
        List<SaleResponseDto> response = saleService.getSalesByBook(bookId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get sales by date range", description = "Retrieve sales within a date range")
    public ResponseEntity<List<SaleResponseDto>> getSalesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<SaleResponseDto> response = saleService.getSalesByDateRange(startDate, endDate);
        return ResponseEntity.ok(response);
    }
}
