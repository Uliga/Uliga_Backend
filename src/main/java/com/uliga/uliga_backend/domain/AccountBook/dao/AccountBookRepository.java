package com.uliga.uliga_backend.domain.AccountBook.dao;

import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountBookRepository extends JpaRepository<AccountBook, Long> {
}
