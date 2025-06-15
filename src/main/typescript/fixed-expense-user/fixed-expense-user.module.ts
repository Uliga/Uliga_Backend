import { Module } from '@nestjs/common';
import { FixedExpenseUserService } from './fixed-expense-user.service';

@Module({
  providers: [FixedExpenseUserService],
  exports: [FixedExpenseUserService],
})
export class FixedExpenseUserModule {}
