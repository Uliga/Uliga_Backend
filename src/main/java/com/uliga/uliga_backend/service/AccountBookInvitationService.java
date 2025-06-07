package com.uliga.uliga_backend.service;

import com.uliga.uliga_backend.dto.account_book.req.CreateInvitationDto;
import com.uliga.uliga_backend.dto.account_book.req.InvitationReplyDto;
import com.uliga.uliga_backend.jooq.tables.pojos.AccountBookInvitation;

import reactor.core.publisher.Mono;

public class AccountBookInvitationService {

  public Mono<AccountBookInvitation> createInvitation(CreateInvitationDto dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createInvitation'");
  }

  public Mono<AccountBookInvitation> replyToInvitation(InvitationReplyDto dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'replyToInvitation'");
  }

}
