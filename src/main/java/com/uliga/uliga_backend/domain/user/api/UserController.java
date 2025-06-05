package com.uliga.uliga_backend.domain.user.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uliga.uliga_backend.domain.user.application.UserService;
import com.uliga.uliga_backend.domain.user.dto.req.CreateUserDto;
import com.uliga.uliga_backend.domain.user.dto.req.UserQueryDto;
import com.uliga.uliga_backend.domain.user.dto.req.UpdateUserDto;
import com.uliga.uliga_backend.domain.user.dto.res.UserDto;
import com.uliga.uliga_backend.global.common.annotation.Serialize;
import com.uliga.uliga_backend.jooq.tables.pojos.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "사용자 API")
@RestController
@RequestMapping("v2/user")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @Operation(summary = "사용자 조회 API")
  @GetMapping()
  @Serialize(dto = UserDto.class)
  public ResponseEntity<Flux<User>> getUsers(@ModelAttribute UserQueryDto query) {
    return ResponseEntity.ok(userService.getUsers(query));
  }

  @Operation(summary = "사용자 생성 API")
  @PostMapping()
  @Serialize(dto = UserDto.class)
  public ResponseEntity<Mono<User>> createUser(@RequestBody CreateUserDto dto) {
    return ResponseEntity.ok(userService.createUser(dto));
  }

  @Operation(summary = "사용자 수정 API")
  @PatchMapping()
  @Serialize(dto = UserDto.class)
  public ResponseEntity<Mono<User>> updateUser(@RequestBody UpdateUserDto dto) {
    return ResponseEntity.ok(userService.updateUser(dto));
  }

  @Operation(summary = "사용자 삭제 API")
  @DeleteMapping("{userId}")
  @Serialize(dto = UserDto.class)
  public ResponseEntity<Mono<User>> deleteUser(@PathVariable("userId") Long userId) {
    return ResponseEntity.ok(userService.deleteUser(userId));
  }
}