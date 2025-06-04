package com.pawara.bookstore.repository;

import com.pawara.bookstore.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT a FROM Author a WHERE a.isDeleted = false")
    List<Author> findAllActiveAuthors();

    @Query("SELECT a FROM Author a WHERE a.isDeleted = false AND a.id = :id")
    Optional<Author> findActiveAuthorById(Long id);

    @Query("SELECT a FROM Author a WHERE a.isDeleted = false AND a.name LIKE %:name%")
    List<Author> findActiveAuthorsByNameContaining(String name);
}
