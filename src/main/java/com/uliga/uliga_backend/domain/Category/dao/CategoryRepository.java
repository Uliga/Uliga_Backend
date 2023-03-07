package com.uliga.uliga_backend.domain.Category.dao;

import com.uliga.uliga_backend.domain.Category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByAccountBookIdAndName(Long id, String name);
}
