package com.uliga.uliga_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uliga.uliga_backend.common.annotation.Serialize;
import com.uliga.uliga_backend.jooq.tables.pojos.User;
import com.uliga.uliga_backend.service.UserService;
import com.uliga.uliga_backend.user.dto.req.UpdateUserDto;
import com.uliga.uliga_backend.user.dto.res.UserDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
  public ResponseEntity<Mono<User>> getCurrentUser() {
    return ResponseEntity.ok(userService.getCurrentUser());
  }

  @Operation(summary = "사용자 수정 API")
  @PatchMapping()
  @Serialize(dto = UserDto.class)
  public ResponseEntity<Mono<User>> updateUser(@RequestBody UpdateUserDto dto) {
    return ResponseEntity.ok(userService.updateUser(dto));
  }

  @Operation(summary = "사용자 삭제 API")
  @DeleteMapping("/{userId}")
  @Serialize(dto = UserDto.class)
  public ResponseEntity<Mono<User>> deleteUser(@PathVariable("userId") Long userId) {
    return ResponseEntity.ok(userService.deleteUser(userId));
  }
}