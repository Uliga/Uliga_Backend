package com.uliga.uliga_backend.domain.Income.dao;

import com.uliga.uliga_backend.domain.Income.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeRepository extends JpaRepository<Income, Long> {
}
