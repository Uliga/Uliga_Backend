package com.uliga.uliga_backend.domain.account_book_data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uliga.uliga_backend.domain.account_book_data.model.AccountBookData;

public interface AccountBookDataRepository extends JpaRepository<AccountBookData, Long> {

}
