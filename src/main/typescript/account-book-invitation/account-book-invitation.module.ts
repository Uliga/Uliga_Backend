import { Module } from '@nestjs/common';
import { AccountBookInvitationService } from './account-book-invitation.service';

@Module({
  providers: [AccountBookInvitationService],
  exports: [AccountBookInvitationService],
})
export class AccountBookInvitationModule {}
