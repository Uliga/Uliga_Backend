package com.uliga.uliga_backend.dto.auth.req;

import com.uliga.uliga_backend.entity.UserEntity;
import com.uliga.uliga_backend.jooq.enums.Authority;
import com.uliga.uliga_backend.jooq.enums.UserLoginType;

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
public class SignupUserDto {
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

  // public void encrypt(PasswordEncoder passwordEncoder) {
  // this.password = passwordEncoder.encode(this.password);
  // this.appPassword = passwordEncoder.encode(this.appPassword);
  // }

  public UserEntity toEntity() {
    return UserEntity.builder()
        .email(email)
        .password(password)
        .appPassword(appPassword)
        .nickName(nickName)
        .userName(userName)
        .userLoginType(UserLoginType.EMAIL)
        .authority(Authority.ROLE_USER)
        .build();
  }

}
