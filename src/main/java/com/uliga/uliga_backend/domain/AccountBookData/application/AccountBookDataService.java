package com.uliga.uliga_backend.domain.AccountBookData.application;

import com.uliga.uliga_backend.domain.AccountBookData.dto.AccountBookDataDTO.CreateResult;
import com.uliga.uliga_backend.domain.AccountBookData.dto.AccountBookDataDTO;
import com.uliga.uliga_backend.domain.Income.dao.IncomeRepository;
import com.uliga.uliga_backend.domain.Record.dao.RecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountBookDataService {
    private final IncomeRepository incomeRepository;
    private final RecordRepository recordRepository;

    /**
     * 가계부에 다수의 수입, 지출 한번에 추가
     * @param id 생성자의 아이디
     * @param items 아이템 생성 요청
     * @return 아이템 생성 결과
     */
    @Transactional
    public CreateResult createItems(Long id, AccountBookDataDTO.CreateItems items) {
        return null;
    }
}
