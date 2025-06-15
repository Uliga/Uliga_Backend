import { Module } from '@nestjs/common';
import { AccountBookService } from './account-book.service';

@Module({
  providers: [AccountBookService],
  exports: [AccountBookService],
})
export class AccountBookModule {}
