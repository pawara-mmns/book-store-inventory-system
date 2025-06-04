package com.pawara.bookstore.service.impl;

import com.pawara.bookstore.dto.sale.SaleRequestDto;
import com.pawara.bookstore.dto.sale.SaleResponseDto;
import com.pawara.bookstore.entity.Book;
import com.pawara.bookstore.entity.Sale;
import com.pawara.bookstore.entity.User;
import com.pawara.bookstore.exception.InsufficientStockException;
import com.pawara.bookstore.exception.ResourceNotFoundException;
import com.pawara.bookstore.repository.BookRepository;
import com.pawara.bookstore.repository.SaleRepository;
import com.pawara.bookstore.repository.UserRepository;
import com.pawara.bookstore.service.SaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    public SaleResponseDto createSale(SaleRequestDto saleRequestDto, String username) {
        log.info("Creating sale for book ID: {} by user: {}", saleRequestDto.getBookId(), username);

        Book book = bookRepository.findActiveBookById(saleRequestDto.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + saleRequestDto.getBookId()));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        // Check stock availability
        if (book.getStockQuantity() < saleRequestDto.getQuantity()) {
            throw new InsufficientStockException(
                    String.format("Insufficient stock. Available: %d, Requested: %d", 
                            book.getStockQuantity(), saleRequestDto.getQuantity()));
        }

        // Create sale
        Sale sale = new Sale();
        sale.setBook(book);
        sale.setQuantity(saleRequestDto.getQuantity());
        sale.setUnitPrice(book.getPrice());
        sale.setTotalAmount(book.getPrice().multiply(BigDecimal.valueOf(saleRequestDto.getQuantity())));
        sale.setSoldBy(user);

        // Update book stock
        book.setStockQuantity(book.getStockQuantity() - saleRequestDto.getQuantity());
        bookRepository.save(book);

        Sale savedSale = saleRepository.save(sale);
        log.info("Sale created successfully with ID: {}", savedSale.getId());

        return mapToResponseDto(savedSale);
    }

    @Override
    @Transactional(readOnly = true)
    public SaleResponseDto getSaleById(Long id) {
        log.info("Fetching sale with ID: {}", id);
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with ID: " + id));
        return mapToResponseDto(sale);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponseDto> getAllSales() {
        log.info("Fetching all sales");
        return saleRepository.findAllSalesOrderByDateDesc()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponseDto> getSalesByUser(Long userId) {
        log.info("Fetching sales by user ID: {}", userId);
        return saleRepository.findSalesByUser(userId)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponseDto> getSalesByBook(Long bookId) {
        log.info("Fetching sales by book ID: {}", bookId);
        return saleRepository.findSalesByBook(bookId)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponseDto> getSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching sales between {} and {}", startDate, endDate);
        return saleRepository.findSalesByDateRange(startDate, endDate)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    private SaleResponseDto mapToResponseDto(Sale sale) {
        return new SaleResponseDto(
                sale.getId(),
                sale.getBook().getTitle(),
                sale.getBook().getIsbn(),
                sale.getQuantity(),
                sale.getUnitPrice(),
                sale.getTotalAmount(),
                sale.getSoldBy().getUsername(),
                sale.getSoldDate()
        );
    }
}
