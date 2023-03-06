package com.uliga.uliga_backend.domain.AccountBook.application;

import com.uliga.uliga_backend.domain.AccountBook.dao.AccountBookRepository;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQuery.AccountBookInfoQ;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountBookService {
    private final AccountBookRepository accountBookRepository;

    public GetAccountBookInfos getMemberAccountBook(Long id) {
        List<AccountBookInfoQ> accountBookInfosByMemberId = accountBookRepository.findAccountBookInfosByMemberId(id);

        return GetAccountBookInfos.builder()
                .accountBooks(accountBookInfosByMemberId).build();
    }
}
