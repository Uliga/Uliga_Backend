import { Module } from '@nestjs/common';
import { ExpenseService } from './expense.service';

@Module({
  providers: [ExpenseService],
  exports: [ExpenseService],
})
export class ExpenseModule {}
