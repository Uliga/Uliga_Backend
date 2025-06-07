package com.uliga.uliga_backend.service;

import com.uliga.uliga_backend.dto.account_book.req.CreateInvitationDto;
import com.uliga.uliga_backend.dto.account_book.req.InvitationReplyDto;
import com.uliga.uliga_backend.entity.AccountBookInvitationEntity;

import reactor.core.publisher.Mono;

public class AccountBookInvitationService {

  public Mono<AccountBookInvitationEntity> createInvitation(CreateInvitationDto dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createInvitation'");
  }

  public Mono<AccountBookInvitationEntity> replyToInvitation(InvitationReplyDto dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'replyToInvitation'");
  }

}
