import { Module } from '@nestjs/common';
import { ExpenseCategoryService } from './expense-category.service';

@Module({
  providers: [ExpenseCategoryService],
  exports: [ExpenseCategoryService],
})
export class ExpenseCategoryModule {}
