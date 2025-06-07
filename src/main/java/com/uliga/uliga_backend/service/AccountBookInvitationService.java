package com.uliga.uliga_backend.service;

import org.springframework.stereotype.Service;

import com.uliga.uliga_backend.dto.account_book.req.CreateInvitationDto;
import com.uliga.uliga_backend.dto.account_book.req.InvitationReplyDto;
import com.uliga.uliga_backend.entity.AccountBookInvitationEntity;
import com.uliga.uliga_backend.repository.AccountBookInvitationRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountBookInvitationService {
  private final AccountBookInvitationRepository accountBookInvitationRepository;

  public Mono<AccountBookInvitationEntity> createInvitation(CreateInvitationDto dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createInvitation'");
  }

  public Mono<AccountBookInvitationEntity> replyToInvitation(InvitationReplyDto dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'replyToInvitation'");
  }

}
