package com.uliga.uliga_backend.auth.service;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uliga.uliga_backend.account_book.service.AccountBookService;
import com.uliga.uliga_backend.auth.dto.req.CreateUserDto;
import com.uliga.uliga_backend.category.service.CategoryService;
import com.uliga.uliga_backend.jooq.tables.pojos.User;
import com.uliga.uliga_backend.member.dto.MemberDTO.ExistsCheckDto;
import com.uliga.uliga_backend.member.dto.MemberDTO.LoginRequest;
import com.uliga.uliga_backend.member.dto.MemberDTO.LoginResult;
import com.uliga.uliga_backend.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthServiceV2 {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AccountBookService accountBookService;
  private final CategoryService categoryService;

  public Mono<User> signup(CreateUserDto dto) {
    dto.encrypt(passwordEncoder);
    User user = dto.toEntity();
    return userRepository.save(user) // Mono<User>
        .flatMap(savedUser -> {

          // accountBookService.createAccountBookPrivateSocialLogin(...)는
          // Mono<AccountBook>을 반환한다고 가정
          return accountBookService
              .createPrivateAccountBook(savedUser) // Mono<AccountBook>
              .flatMap(accountBook -> {
                // 3. 생성된 가계부를 바탕으로 기본 카테고리 생성
                // categoryService.createDefaultCategories(...)는 Mono<Void> 반환을 가정
                return categoryService
                    .createDefaultCategories(accountBook) // Mono<Void>
                    .thenReturn(savedUser); // Mono<Member> 으로 이어붙임
              });
        });

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
