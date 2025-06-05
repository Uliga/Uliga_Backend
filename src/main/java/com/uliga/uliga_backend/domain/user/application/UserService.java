package com.uliga.uliga_backend.domain.user.application;

import org.springframework.stereotype.Service;

import com.uliga.uliga_backend.domain.user.dto.req.CreateUserDto;
import com.uliga.uliga_backend.domain.user.dto.req.UpdateUserDto;
import com.uliga.uliga_backend.domain.user.repository.UserRepository;
import com.uliga.uliga_backend.jooq.tables.pojos.User;

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