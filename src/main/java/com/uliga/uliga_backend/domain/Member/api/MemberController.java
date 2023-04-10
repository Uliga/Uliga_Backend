package com.uliga.uliga_backend.domain.Member.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.uliga.uliga_backend.domain.Member.application.MemberService;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.MatchResult;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.UpdateApplicationPasswordDto;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.UpdateResult;
import com.uliga.uliga_backend.global.error.response.ErrorResponse;
import com.uliga.uliga_backend.global.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

import static com.uliga.uliga_backend.domain.Member.dto.MemberDTO.*;

@Tag(name = "멤버 관리", description = "멤버 관리 API 입니다")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/member")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "로그인한 멤버 정보 조회 API", description = "로그인한 멤버 정보 조회 API 입니다", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정보 조회 성공시", content = @Content(schema = @Schema(implementation = GetMemberInfo.class))),
            @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "")
    public ResponseEntity<GetMemberInfo> getMemberInfo(@Parameter(in = ParameterIn.PATH, name = "pageable", description = "페이징할때 필요한 정보") Pageable pageable) throws JsonProcessingException {

        log.info("로그인한 멤버 정보조회 API 호출");
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberService.getCurrentMemberInfo(currentMemberId, pageable));
    }

    @Operation(summary = "멤버 정보 업데이트 API", description = "멤버 정보 업데이트 API 입니다", security = @SecurityRequirement(name = "bearerAuth"))
    @PatchMapping(value = "")
    public ResponseEntity<MemberInfoUpdateRequest> updateMemberInfo(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "멤버 정보 업데이트 요청", content = @Content(schema = @Schema(implementation = MemberInfoUpdateRequest.class)))
                                                                    @RequestBody Map<String, Object> updates) {

        log.info("멤버 정보 업데이트 API 호출됌");
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberService.updateMemberInfo(currentMemberId, updates));
    }

    @Operation(summary = "멤버 애플리케이션 비밀번호 확인", description = "로그인한 멤버 애플리케이션 비밀번호 확인 API", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 확인 성공시", content = @Content(schema = @Schema(implementation = MatchResult.class))),
            @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/applicationPassword")
    public ResponseEntity<MatchResult> checkApplicationPassword(@Valid @RequestBody ApplicationPasswordCheck passwordCheck) {
        log.info("멤버 애플리케이션 비밀번호 확인 API 호출");
        return ResponseEntity.ok(MatchResult.builder()
                .matches(
                        memberService.checkApplicationPassword(SecurityUtil.getCurrentMemberId(), passwordCheck)
                ).build());
    }

    @Operation(summary = "멤버 비밀번호 확인", description = "로그인한 멤버 비밀번호 확인 API", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 확인 성공시", content = @Content(schema = @Schema(implementation = MatchResult.class))),
            @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/password")
    public ResponseEntity<MatchResult> checkPassword(@Valid @RequestBody PasswordCheck passwordCheck) {
        log.info("멤버 비밀번호 확인 API 호출");
        return ResponseEntity.ok(MatchResult.builder()
                .matches(
                        memberService.checkPassword(SecurityUtil.getCurrentMemberId(), passwordCheck)
                ).build());
    }

    @Operation(summary = "닉네임 존재 여부 확인", description = "닉네임 존재 확인 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "닉네임 존재 여부 확인", content = @Content(schema = @Schema(implementation = ExistsCheckDto.class))),
            @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/nickname")
    public ResponseEntity<ExistsCheckDto> nicknameExistsCheck(@Valid @RequestBody NicknameCheckDto nicknameCheckDto) {

        log.info("닉네임 존재 여부 확인 API 호출");
        return ResponseEntity.ok(
                ExistsCheckDto.builder().exists(memberService.nicknameExists(SecurityUtil.getCurrentMemberId(), nicknameCheckDto)).build()
        );
    }

    @Operation(summary = "멤버 탈퇴", description = "멤버 탈퇴 API")
    @DeleteMapping(value = "")
    public ResponseEntity<String> deleteMember() {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        memberService.deleteMember(currentMemberId);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(URI.create("/logout"));
        return ResponseEntity.ok("DELETED");
    }

    @Operation(summary = "이메일로 존재하는 멤버 찾기", description = "이메일로 존재하는 멤버 찾는 API", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일로 존재하는 멤버 존재시", content = @Content(schema = @Schema(implementation = SearchEmailResult.class))),
            @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/search/email")
    public ResponseEntity<SearchEmailResult> getMemberByEmail(@RequestParam(name = "accountBook", required = false, defaultValue = "") Long accountBookId,
                                                              @Valid @RequestBody SearchMemberByEmail searchMemberByEmail) {

        log.info("이메일로 존재하는 멤버 찾기 API 호출");
        log.info(String.valueOf(accountBookId));
        return ResponseEntity.ok(memberService.findMemberByEmail(accountBookId, searchMemberByEmail));
    }


    @Operation(summary = "금융 일정 알림 전체 제거", description = "멤버한테 온 금융 일정 알림 전체 제거 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공시"),
            @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping(value = "/notification")
    public ResponseEntity<String> deleteMemberNotification() {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        memberService.deleteMemberNotification(currentMemberId);
        return ResponseEntity.ok("DELETED");
    }


    @Operation(summary = "공유 가계부 탈퇴 API", description = "공유 가계부에 탈퇴 할때 쓰는 API 입니다")
    @DeleteMapping(value = "/accountBook/{id}")
    public ResponseEntity<String> deleteAccountBookMember(@PathVariable("id") Long id) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        memberService.deleteAccountBookMember(id, currentMemberId);
        return ResponseEntity.ok("DELETED");
    }
}
