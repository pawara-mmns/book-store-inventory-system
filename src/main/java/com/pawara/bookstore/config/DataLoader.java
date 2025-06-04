package com.pawara.bookstore.config;

import com.pawara.bookstore.entity.Author;
import com.pawara.bookstore.entity.Book;
import com.pawara.bookstore.entity.Category;
import com.pawara.bookstore.entity.User;
import com.pawara.bookstore.enums.Role;
import com.pawara.bookstore.repository.AuthorRepository;
import com.pawara.bookstore.repository.BookRepository;
import com.pawara.bookstore.repository.CategoryRepository;
import com.pawara.bookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            loadInitialData();
        }
    }

    private void loadInitialData() {
        log.info("Loading initial data...");

        // Create default admin user
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setEmail("admin@bookstore.com");
        admin.setRole(Role.ADMIN);
        admin.setIsDeleted(false);
        userRepository.save(admin);

        // Create default employee user
        User employee = new User();
        employee.setUsername("employee");
        employee.setPassword(passwordEncoder.encode("employee123"));
        employee.setEmail("employee@bookstore.com");
        employee.setRole(Role.EMPLOYEE);
        employee.setIsDeleted(false);
        userRepository.save(employee);

        // Create categories
        Category fiction = new Category();
        fiction.setName("Fiction");
        fiction.setDescription("Fictional books and novels");
        fiction.setIsDeleted(false);
        categoryRepository.save(fiction);

        Category nonFiction = new Category();
        nonFiction.setName("Non-Fiction");
        nonFiction.setDescription("Non-fictional books");
        nonFiction.setIsDeleted(false);
        categoryRepository.save(nonFiction);

        Category technology = new Category();
        technology.setName("Technology");
        technology.setDescription("Technology and programming books");
        technology.setIsDeleted(false);
        categoryRepository.save(technology);

        // Create authors
        Author author1 = new Author();
        author1.setName("J.K. Rowling");
        author1.setBiography("British author, best known for the Harry Potter series");
        author1.setIsDeleted(false);
        authorRepository.save(author1);

        Author author2 = new Author();
        author2.setName("Robert C. Martin");
        author2.setBiography("American software engineer and author");
        author2.setIsDeleted(false);
        authorRepository.save(author2);

        Author author3 = new Author();
        author3.setName("George Orwell");
        author3.setBiography("English novelist and essayist");
        author3.setIsDeleted(false);
        authorRepository.save(author3);

        // Create books
        Book book1 = new Book();
        book1.setTitle("Harry Potter and the Philosopher's Stone");
        book1.setAuthor(author1);
        book1.setCategory(fiction);
        book1.setIsbn("978-0747532699");
        book1.setPrice(new BigDecimal("15.99"));
        book1.setPublishedYear(1997);
        book1.setStockQuantity(50);
        book1.setDescription("The first book in the Harry Potter series");
        book1.setIsDeleted(false);
        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setTitle("Clean Code");
        book2.setAuthor(author2);
        book2.setCategory(technology);
        book2.setIsbn("978-0132350884");
        book2.setPrice(new BigDecimal("42.99"));
        book2.setPublishedYear(2008);
        book2.setStockQuantity(25);
        book2.setDescription("A handbook of agile software craftsmanship");
        book2.setIsDeleted(false);
        bookRepository.save(book2);

        Book book3 = new Book();
        book3.setTitle("1984");
        book3.setAuthor(author3);
        book3.setCategory(fiction);
        book3.setIsbn("978-0451524935");
        book3.setPrice(new BigDecimal("13.99"));
        book3.setPublishedYear(1949);
        book3.setStockQuantity(30);
        book3.setDescription("A dystopian social science fiction novel");
        book3.setIsDeleted(false);
        bookRepository.save(book3);

        log.info("Initial data loaded successfully!");
        log.info("Default Admin - Username: admin, Password: admin123");
        log.info("Default Employee - Username: employee, Password: employee123");
    }
}
