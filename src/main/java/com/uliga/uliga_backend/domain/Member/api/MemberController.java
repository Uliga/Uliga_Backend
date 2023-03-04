package com.uliga.uliga_backend.domain.Member.api;

import com.uliga.uliga_backend.domain.Member.application.MemberService;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.MatchResult;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.UpdateApplicationPasswordDto;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.UpdateResult;
import com.uliga.uliga_backend.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static com.uliga.uliga_backend.domain.Member.dto.MemberDTO.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping(value = "/applicationPassword")
    public ResponseEntity<MatchResult> checkApplicationPassword(@RequestBody ApplicationPasswordCheck passwordCheck) {
        return ResponseEntity.ok(MatchResult.builder()
                .matches(
                        memberService.checkApplicationPassword(SecurityUtil.getCurrentMemberId(), passwordCheck)
                ).build());
    }

    @PatchMapping(value = "/applicationPassword")
    public ResponseEntity<UpdateResult> updateApplicationPassword(@RequestBody UpdateApplicationPasswordDto updateApplicationPasswordDto) {
        memberService.updateApplicationPassword(SecurityUtil.getCurrentMemberId(), updateApplicationPasswordDto);
        return ResponseEntity.ok(
                UpdateResult.builder().result("UPDATE").build()
        );
    }

    @PostMapping(value = "/password")
    public ResponseEntity<MatchResult> checkPassword(@RequestBody PasswordCheck passwordCheck) {
        return ResponseEntity.ok(MatchResult.builder()
                .matches(
                        memberService.checkPassword(SecurityUtil.getCurrentMemberId(), passwordCheck)
                ).build());
    }

    @PatchMapping(value = "/password")
    public ResponseEntity<UpdateResult> updatePassword(@RequestBody UpdatePasswordDto passwordDto) {
        memberService.updatePassword(SecurityUtil.getCurrentMemberId(), passwordDto);
        return ResponseEntity.ok(
                UpdateResult.builder().result("UPDATE").build()
        );
    }

    @PatchMapping(value = "/avatarUrl")
    public ResponseEntity<UpdateResult> updateAvatarUrl(@RequestBody UpdateAvatarUrl avatarUrl) {
        memberService.updateAvatarUrl(SecurityUtil.getCurrentMemberId(), avatarUrl);
        return ResponseEntity.ok(
                UpdateResult.builder().result("UPDATE").build()
        );
    }


}
