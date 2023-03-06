package com.uliga.uliga_backend.domain.AccountBook.api;

import com.uliga.uliga_backend.domain.AccountBook.application.AccountBookService;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.AccountBookInfo;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.CreateRequest;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.GetAccountBookInfos;
import com.uliga.uliga_backend.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/accountBook")
public class AccountBookController {

    private final AccountBookService accountBookService;

    @GetMapping(value = "")
    public ResponseEntity<GetAccountBookInfos> getMemberAccountBook() {
        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.getMemberAccountBook(id));
    }

    @PostMapping(value = "")
    public ResponseEntity<AccountBookInfo> createAccountBook(@RequestBody CreateRequest createRequest) {
        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.createAccountBook(id, createRequest));
    }
}
