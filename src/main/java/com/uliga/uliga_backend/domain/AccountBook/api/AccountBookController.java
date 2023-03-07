package com.uliga.uliga_backend.domain.AccountBook.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.uliga.uliga_backend.domain.AccountBook.application.AccountBookService;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.*;
import com.uliga.uliga_backend.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
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

    @GetMapping(value = "/{id}")
    public ResponseEntity<AccountBookInfo> getSingleAccountBookInfo(@PathVariable("id") Long id) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.getSingleAccountBookInfo(id, memberId));
    }

    // 이메일로 유저 초대장 여기서 보내야함
    @PostMapping(value = "")
    public ResponseEntity<SimpleAccountBookInfo> createAccountBook(@RequestBody CreateRequest createRequest) throws JsonProcessingException {
        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.createAccountBook(id, createRequest));
    }

    @PostMapping(value = "/private")
    public ResponseEntity<SimpleAccountBookInfo> createAccountBookPrivate(@RequestBody CreateRequestPrivate createRequest) {
        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.createAccountBookPrivate(id, createRequest));
    }

    @PostMapping(value = "/invitation")
    public ResponseEntity<Invited> createInvitation(@RequestBody GetInvitations invitations) throws JsonProcessingException {
        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.createInvitation(id, invitations));
    }

    @PostMapping(value = "/invitation/reply")
    public ResponseEntity<InvitationReplyResult> invitationReply(@RequestBody InvitationReply invitationReply) throws JsonProcessingException {

        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(accountBookService.invitationReply(id, invitationReply));
    }
}
