package com.uliga.uliga_backend.user.service;

import org.springframework.stereotype.Service;

import com.uliga.uliga_backend.jooq.tables.pojos.User;
import com.uliga.uliga_backend.user.dto.req.CreateUserDto;
import com.uliga.uliga_backend.user.dto.req.UpdateUserDto;
import com.uliga.uliga_backend.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public Mono<User> getCurrentUser() {
    throw new UnsupportedOperationException("Unimplemented method 'getUsers'");
  }

  public Mono<User> createUser(CreateUserDto dto) {
    throw new UnsupportedOperationException("Unimplemented method 'createUser'");
  }

  public Mono<User> updateUser(UpdateUserDto dto) {
    throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
  }

  public Mono<User> deleteUser(Long userId) {
    throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
  }
}