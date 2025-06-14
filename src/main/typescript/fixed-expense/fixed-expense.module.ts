import { Module } from '@nestjs/common';
import { FixedExpenseService } from './fixed-expense.service';

@Module({
  providers: [FixedExpenseService],
  exports: [FixedExpenseService],
})
export class FixedExpenseModule {}
