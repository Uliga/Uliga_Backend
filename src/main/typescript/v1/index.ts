import { Module } from '@nestjs/common';
import { AccountBookInvitationController } from '../account-book-invitation/account-book-invitation.controller';
import { AccountBookInvitationModule } from '../account-book-invitation/account-book-invitation.module';
import { AccountBookUserController } from '../account-book-user/account-book-user.controller';
import { AccountBookUserModule } from '../account-book-user/account-book-user.module';
import { AccountBookController } from '../account-book/account-book.controller';
import { AccountBookModule } from '../account-book/account-book.module';
import { AuthController } from '../auth/auth.controller';
import { AuthModule } from '../auth/auth.module';
import { BudgetController } from '../budget/budget.controller';
import { BudgetModule } from '../budget/budget.module';
import { ExpenseCategoryController } from '../expense-category/expense-category.controller';
import { ExpenseCategoryModule } from '../expense-category/expense-category.module';
import { ExpenseController } from '../expense/expense.controller';
import { ExpenseModule } from '../expense/expense.module';
import { FixedExpenseUserController } from '../fixed-expense-user/fixed-expense-user.controller';
import { FixedExpenseUserModule } from '../fixed-expense-user/fixed-expense-user.module';
import { FixedExpenseController } from '../fixed-expense/fixed-expense.controller';
import { FixedExpenseModule } from '../fixed-expense/fixed-expense.module';
import { FixedRevenueController } from '../fixed-revenue/fixed-revenue.controller';
import { FixedRevenueModule } from '../fixed-revenue/fixed-revenue.module';
import { RevenueCategoryController } from '../revenue-category/revenue-category.controller';
import { RevenueCategoryModule } from '../revenue-category/revenue-category.module';
import { RevenueController } from '../revenue/revenue.controller';
import { RevenueModule } from '../revenue/revenue.module';
import { UserController } from '../user/user.controller';
import { UserModule } from '../user/user.module';

@Module({
  imports: [
    AccountBookInvitationModule,
    AccountBookUserModule,
    AccountBookModule,
    AuthModule,
    BudgetModule,
    ExpenseCategoryModule,
    ExpenseModule,
    FixedExpenseUserModule,
    FixedExpenseModule,
    FixedRevenueModule,
    RevenueCategoryModule,
    RevenueModule,
    UserModule,
  ],
  controllers: [
    AccountBookInvitationController,
    AccountBookUserController,
    AccountBookController,
    AuthController,
    BudgetController,
    ExpenseCategoryController,
    ExpenseController,
    FixedExpenseUserController,
    FixedExpenseController,
    FixedRevenueController,
    RevenueCategoryController,
    RevenueController,
    UserController,
  ],
})
export class V1Module {}
