package com.uliga.uliga_backend.entity;

import java.time.LocalDateTime;

import com.uliga.uliga_backend.jooq.enums.AccountBookAuthority;
import com.uliga.uliga_backend.jooq.tables.interfaces.IAccountBookUser;

import lombok.Getter;

@Getter
public class AccountBookUserEntity implements IAccountBookUser {
  private final Long id;
  private final Long accountBookId;
  private final Long userId;
  private final String profileUrl;
  private final Boolean notificationsEnabled;
  private final AccountBookAuthority accountBookAuthority;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;

  public AccountBookUserEntity(IAccountBookUser value) {
    this.id = value.getId();
    this.accountBookId = value.getAccountBookId();
    this.userId = value.getUserId();
    this.profileUrl = value.getProfileUrl();
    this.notificationsEnabled = value.getNotificationsEnabled();
    this.accountBookAuthority = value.getAccountBookAuthority();
    this.createdAt = value.getCreatedAt();
    this.updatedAt = value.getUpdatedAt();
  }

  public static Builder builder() {
    return new Builder();
  }

  private Builder toBuilder() {
    return new Builder()
        .id(id)
        .accountBookId(accountBookId)
        .userId(userId)
        .profileUrl(profileUrl)
        .notificationsEnabled(notificationsEnabled)
        .accountBookAuthority(accountBookAuthority)
        .createdAt(createdAt)
        .updatedAt(updatedAt);
  }

  public AccountBookUserEntity updateAccountBookAuthority(AccountBookAuthority accountBookAuthority) {
    return toBuilder().accountBookAuthority(accountBookAuthority).build();
  }

  public AccountBookUserEntity updateProfileUrl(String profileUrl) {
    return toBuilder().profileUrl(profileUrl).build();
  }

  public AccountBookUserEntity updateNotificationsEnabled(Boolean notificationsEnabled) {
    return toBuilder().notificationsEnabled(notificationsEnabled).build();
  }

  @Getter
  public static class Builder implements IAccountBookUser {
    private Long id;
    private Long accountBookId;
    private Long userId;
    private String profileUrl;
    private Boolean notificationsEnabled;
    private AccountBookAuthority accountBookAuthority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Builder() {
    }

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder accountBookId(Long accountBookId) {
      this.accountBookId = accountBookId;
      return this;
    }

    public Builder userId(Long userId) {
      this.userId = userId;
      return this;
    }

    public Builder profileUrl(String profileUrl) {
      this.profileUrl = profileUrl;
      return this;
    }

    public Builder notificationsEnabled(Boolean notificationsEnabled) {
      this.notificationsEnabled = notificationsEnabled;
      return this;
    }

    public Builder accountBookAuthority(AccountBookAuthority accountBookAuthority) {
      this.accountBookAuthority = accountBookAuthority;
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

    public AccountBookUserEntity build() {
      return new AccountBookUserEntity(this);
    }

  }
}
