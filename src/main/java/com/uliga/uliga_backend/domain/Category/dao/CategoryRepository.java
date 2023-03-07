package com.uliga.uliga_backend.domain.Category.dao;

import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByAccountBookIdAndName(Long id, String name);

    Optional<Category> findByAccountBookAndName(AccountBook accountBook, String name);

}
