package com.uliga.uliga_backend.service;

import org.springframework.stereotype.Service;

import com.uliga.uliga_backend.auth.dto.req.CreateUserDto;
import com.uliga.uliga_backend.dto.user.req.UpdateUserDto;
import com.uliga.uliga_backend.entity.UserEntity;
import com.uliga.uliga_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public Mono<UserEntity> getCurrentUser() {
    throw new UnsupportedOperationException("Unimplemented method 'getUsers'");
  }

  public Mono<UserEntity> createUser(CreateUserDto dto) {
    throw new UnsupportedOperationException("Unimplemented method 'createUser'");
  }

  public Mono<UserEntity> updateUser(UpdateUserDto dto) {
    throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
  }

  public Mono<UserEntity> deleteUser(Long userId) {
    throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
  }
}