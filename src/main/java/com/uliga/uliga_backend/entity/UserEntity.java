package com.uliga.uliga_backend.entity;

import java.time.LocalDateTime;

import com.uliga.uliga_backend.jooq.enums.Authority;
import com.uliga.uliga_backend.jooq.enums.UserLoginType;
import com.uliga.uliga_backend.jooq.tables.interfaces.IUser;

import lombok.Getter;

@Getter
public class UserEntity implements IUser {
  private final Long id;
  private final String email;
  private final String password;
  private final String appPassword;
  private final Authority authority;
  private final UserLoginType userLoginType;
  private final Boolean isActive;
  private final String userName;
  private final String nickName;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;

  public UserEntity(IUser value) {
    this.id = value.getId();
    this.email = value.getEmail();
    this.password = value.getPassword();
    this.appPassword = value.getAppPassword();
    this.authority = value.getAuthority();
    this.userLoginType = value.getUserLoginType();
    this.isActive = value.getIsActive();
    this.userName = value.getUserName();
    this.nickName = value.getNickName();
    this.createdAt = value.getCreatedAt();
    this.updatedAt = value.getUpdatedAt();
  }

  public static Builder builder() {
    return new Builder();
  }

  private Builder toBuilder() {
    return new Builder()
        .id(id)
        .email(email)
        .password(password)
        .appPassword(appPassword)
        .authority(authority)
        .userLoginType(userLoginType)
        .isActive(isActive)
        .userName(userName)
        .nickName(nickName)
        .createdAt(createdAt)
        .updatedAt(updatedAt);
  }

  public UserEntity updateEmail(String email) {
    return toBuilder().email(email).build();
  }

  public UserEntity updatePassword(String password) {
    return toBuilder().password(password).build();
  }

  public UserEntity updateAppPassword(String appPassword) {
    return toBuilder().appPassword(appPassword).build();
  }

  public UserEntity updateAuthority(Authority authority) {
    return toBuilder().authority(authority).build();
  }

  public UserEntity updateUserLoginType(UserLoginType userLoginType) {
    return toBuilder().userLoginType(userLoginType).build();
  }

  public UserEntity updateIsActive(Boolean isActive) {
    return toBuilder().isActive(isActive).build();
  }

  public UserEntity updateUserName(String userName) {
    return toBuilder().userName(userName).build();
  }

  public UserEntity updateNickName(String nickName) {
    return toBuilder().nickName(nickName).build();
  }

  @Getter
  public static class Builder implements IUser {
    private Long id;
    private String email;
    private String password;
    private String appPassword;
    private Authority authority;
    private UserLoginType userLoginType;
    private Boolean isActive;
    private String userName;
    private String nickName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Builder() {
    }

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder password(String password) {
      this.password = password;
      return this;
    }

    public Builder appPassword(String appPassword) {
      this.appPassword = appPassword;
      return this;
    }

    public Builder authority(Authority authority) {
      this.authority = authority;
      return this;
    }

    public Builder userLoginType(UserLoginType userLoginType) {
      this.userLoginType = userLoginType;
      return this;
    }

    public Builder isActive(Boolean isActive) {
      this.isActive = isActive;
      return this;
    }

    public Builder userName(String userName) {
      this.userName = userName;
      return this;
    }

    public Builder nickName(String nickName) {
      this.nickName = nickName;
      return this;
    }

    public Builder createdAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public Builder updatedAt(LocalDateTime updatedAt) {
      this.updatedAt = updatedAt;
      return this;
    }

    public UserEntity build() {
      return new UserEntity(this);
    }
  }
}
