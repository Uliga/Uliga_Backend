package com.uliga.uliga_backend.domain.auth.dto.req;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.uliga.uliga_backend.jooq.enums.Authority;
import com.uliga.uliga_backend.jooq.enums.UserLoginType;
import com.uliga.uliga_backend.jooq.tables.pojos.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDto {
  @Schema(description = "회원가입 이메일", defaultValue = "test@email.com")
  @Email
  private String email;
  @Schema(description = "회원가입 비밀번호", defaultValue = "12345678")
  @NotNull
  @Size(min = 8)
  private String password;
  private String appPassword;
  @Schema(description = "닉네임", defaultValue = "nickName")
  @NotNull
  @Size(min = 2, max = 19)
  private String nickName;
  @Schema(description = "본명", defaultValue = "userName")
  @NotNull
  private String userName;

  public void encrypt(PasswordEncoder passwordEncoder) {
    this.password = passwordEncoder.encode(this.password);
    this.appPassword = passwordEncoder.encode(this.appPassword);
  }

  public User toEntity() {

    // User user = new User();
    // user.setAppPassword(appPassword);
    // user.setAuthority(Authority.ROLE_USER);
    // user.setEmail(email);
    // user.setPassword(password);
    // user.setUserLoginType(UserLoginType.EMAIL);
    // user.setNickName(nickName);
    // user.setUserName(userName);
    // user.setIsActive(true);
    return User.builder()
    .;
  }

}
