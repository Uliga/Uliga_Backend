package com.uliga.uliga_backend.domain.Member.api;

import com.uliga.uliga_backend.domain.Member.application.MemberService;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.MatchResult;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.UpdateApplicationPasswordDto;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.UpdateResult;
import com.uliga.uliga_backend.domain.Member.dto.NativeQuery.MemberInfoNativeQ;
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

    @GetMapping(value = "")
    public ResponseEntity<MemberInfoNativeQ> getMemberInfo() {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberService.getCurrentMemberInfo(currentMemberId));
    }

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
    public ResponseEntity<AvatarUrlUpdateResult> updateAvatarUrl(@RequestBody UpdateAvatarUrl avatarUrl) {
        memberService.updateAvatarUrl(SecurityUtil.getCurrentMemberId(), avatarUrl);
        return ResponseEntity.ok(
                AvatarUrlUpdateResult.builder().avatarUrl(avatarUrl.getAvatarUrl()).build()
        );
    }

    @PostMapping(value = "/nickname")
    public ResponseEntity<ExistsCheckDto> nicknameExistsCheck(@RequestBody NicknameCheckDto nicknameCheckDto) {
        return ResponseEntity.ok(
                ExistsCheckDto.builder()
                        .exists(memberService.nicknameExists(SecurityUtil.getCurrentMemberId()
                                , nicknameCheckDto))
                        .build()
        );
    }

    @PatchMapping(value = "/nickname")
    public ResponseEntity<NicknameUpdateResult> updateNickname(@RequestBody UpdateNicknameDto nicknameDto) {
        memberService.updateNickname(SecurityUtil.getCurrentMemberId(), nicknameDto);
        return ResponseEntity.ok(
                NicknameUpdateResult.builder().nickname(nicknameDto.getNewNickname()).build()
        );
    }

    @DeleteMapping(value = "")
    public ResponseEntity<String> deleteMember() {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();

        return ResponseEntity.ok("DELETED");
    }
}
