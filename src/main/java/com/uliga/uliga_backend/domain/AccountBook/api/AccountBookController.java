package com.uliga.uliga_backend.domain.AccountBook.api;

import com.uliga.uliga_backend.domain.AccountBook.application.AccountBookService;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO;
import com.uliga.uliga_backend.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/accountBook")
public class AccountBookController {

    private final AccountBookService accountBookService;

    @GetMapping(value = "")
    public ResponseEntity<AccountBookDTO.GetAccountBookInfos> getMemberAccountBook() {
        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.getMemberAccountBook(id));
    }
}
