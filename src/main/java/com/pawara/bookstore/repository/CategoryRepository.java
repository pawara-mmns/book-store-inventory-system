package com.pawara.bookstore.repository;

import com.pawara.bookstore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.isDeleted = false")
    List<Category> findAllActiveCategories();

    @Query("SELECT c FROM Category c WHERE c.isDeleted = false AND c.id = :id")
    Optional<Category> findActiveCategoryById(Long id);

    @Query("SELECT c FROM Category c WHERE c.isDeleted = false AND c.name = :name")
    Optional<Category> findActiveCategoryByName(String name);

    boolean existsByNameAndIsDeletedFalse(String name);
}
