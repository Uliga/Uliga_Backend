package com.uliga.uliga_backend.domain.Budget.dao;

import com.uliga.uliga_backend.domain.Budget.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
}
