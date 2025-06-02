package com.uliga.uliga_backend.domain.account_book.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.uliga.uliga_backend.domain.account_book.model.AccountBook;

@Repository
public interface AccountBookReactiveRepository
    extends ReactiveCrudRepository<AccountBook, Long>, AccountBookCustomRepository {

}
