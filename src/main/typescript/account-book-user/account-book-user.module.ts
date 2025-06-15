import { Module } from '@nestjs/common';
import { AccountBookUserService } from './account-book-user.service';

@Module({
  providers: [AccountBookUserService],
  exports: [AccountBookUserService],
})
export class AccountBookUserModule {}
