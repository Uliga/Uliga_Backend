package com.uliga.uliga_backend.entity;

import java.time.LocalDateTime;

import com.uliga.uliga_backend.jooq.enums.InvitationStatus;
import com.uliga.uliga_backend.jooq.tables.interfaces.IAccountBookInvitation;

import lombok.Getter;

@Getter
public class AccountBookInvitationEntity implements IAccountBookInvitation {
  private final Long id;
  private final Long accountBookId;
  private final Long inviterUserId;
  private final Long inviteeUserId;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;
  private final InvitationStatus status;

  public AccountBookInvitationEntity(IAccountBookInvitation value) {
    this.id = value.getId();
    this.accountBookId = value.getAccountBookId();
    this.inviterUserId = value.getInviterUserId();
    this.inviteeUserId = value.getInviteeUserId();
    this.createdAt = value.getCreatedAt();
    this.updatedAt = value.getUpdatedAt();
    this.status = value.getStatus();
  }

  private AccountBookInvitationEntity(Builder value) {
    if (value.accountBookId == null) {
      throw new IllegalArgumentException("초대할 가계부 아이디가 설정되어야함");
    }
    if (value.inviterUserId == null) {
      throw new IllegalArgumentException("초대한 사람의 아이디가 설정되야함");
    }
    if (value.inviteeUserId == null) {
      throw new IllegalArgumentException("초대할 사람의 아이디가 설정되야함");
    }
    this.id = value.getId();
    this.accountBookId = value.getAccountBookId();
    this.inviterUserId = value.getInviterUserId();
    this.inviteeUserId = value.getInviteeUserId();
    this.createdAt = value.getCreatedAt();
    this.updatedAt = value.getUpdatedAt();
    this.status = value.getStatus();
  }

  public static Builder builder() {
    return new Builder();
  }

  private Builder toBuilder() {
    return new Builder()
        .id(id)
        .accountBookId(accountBookId)
        .inviterUserId(inviterUserId)
        .inviteeUserId(inviteeUserId)
        .createdAt(createdAt)
        .updatedAt(updatedAt)
        .status(status);
  }

  public AccountBookInvitationEntity updateInvitationStatus(InvitationStatus status) {
    return toBuilder().status(status).build();
  }

  @Getter
  public static class Builder implements IAccountBookInvitation {
    private Long id;
    private Long accountBookId;
    private Long inviterUserId;
    private Long inviteeUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private InvitationStatus status;

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

    public Builder inviterUserId(Long inviterUserId) {
      this.inviteeUserId = inviterUserId;
      return this;
    }

    public Builder inviteeUserId(Long inviteeUserId) {
      this.inviteeUserId = inviteeUserId;
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

    public Builder status(InvitationStatus status) {
      this.status = status;
      return this;
    }

    public AccountBookInvitationEntity build() {
      return new AccountBookInvitationEntity(this);
    }
  }
}
