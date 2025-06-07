package com.uliga.uliga_backend.service;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import com.uliga.uliga_backend.category.service.CategoryService;
import com.uliga.uliga_backend.dto.auth.req.SignupUserDto;
import com.uliga.uliga_backend.dto.member.MemberDTO.ExistsCheckDto;
import com.uliga.uliga_backend.dto.member.MemberDTO.LoginRequest;
import com.uliga.uliga_backend.dto.member.MemberDTO.LoginResult;
import com.uliga.uliga_backend.entity.UserEntity;
import com.uliga.uliga_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class AuthServiceV2 {
  private final UserRepository userRepository;
  // private final PasswordEncoder passwordEncoder;
  private final AccountBookService accountBookService;
  private final CategoryService categoryService;
  private final ExpenseCategoryService expenseCategoryService;

  public Mono<UserEntity> signup(SignupUserDto dto) {
    // dto.encrypt(passwordEncoder);
    UserEntity user = dto.toEntity();

    return Mono.fromCallable(() -> userRepository.save(user))
        .subscribeOn(Schedulers.boundedElastic())
        .flatMap(savedUser -> accountBookService.createPrivateAccountBook(savedUser)
            .flatMap(accountBook -> expenseCategoryService.createDefaultCategories(accountBook))
            .then(Mono.just(savedUser)));
  }

  public Mono<LoginResult> login(LoginRequest dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'login'");
  }

  public Mono<String> logout(ServerHttpRequest request) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'logout'");
  }

  public Mono<ExistsCheckDto> nicknameExists(String nickname) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'nicknameExists'");
  }

}
