package com.uliga.uliga_backend.config;

import javax.sql.DataSource;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.uliga.uliga_backend.jooq.tables.daos.AccountBookDao;
import com.uliga.uliga_backend.jooq.tables.daos.AccountBookInvitationDao;
import com.uliga.uliga_backend.jooq.tables.daos.AccountBookUserDao;
import com.uliga.uliga_backend.jooq.tables.daos.BudgetDao;
import com.uliga.uliga_backend.jooq.tables.daos.ExpenseCategoryDao;
import com.uliga.uliga_backend.jooq.tables.daos.ExpenseDao;
import com.uliga.uliga_backend.jooq.tables.daos.FixedExpenseDao;
import com.uliga.uliga_backend.jooq.tables.daos.FixedExpenseUserDao;
import com.uliga.uliga_backend.jooq.tables.daos.FixedRevenueDao;
import com.uliga.uliga_backend.jooq.tables.daos.RevenueCategoryDao;
import com.uliga.uliga_backend.jooq.tables.daos.RevenueDao;
import com.uliga.uliga_backend.jooq.tables.daos.UserDao;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JooqConfig {

  private final DataSource dataSource;

  @Bean
  public DataSourceTransactionManager transactionManager() {
    return new DataSourceTransactionManager(dataSource);
  }

  @Bean
  public DSLContext dslContext() {
    return DSL.using(dataSource, SQLDialect.POSTGRES);
  }

  @Bean
  public AccountBookDao accountBookDao() {
    return new AccountBookDao(dslContext().configuration());
  }

  @Bean
  public AccountBookInvitationDao accountBookInvitationDao() {
    return new AccountBookInvitationDao(dslContext().configuration());
  }

  @Bean
  public AccountBookUserDao accountBookUserDao() {
    return new AccountBookUserDao(dslContext().configuration());
  }

  @Bean
  public BudgetDao budgetDao() {
    return new BudgetDao(dslContext().configuration());
  }

  @Bean
  public ExpenseDao expenseDao() {
    return new ExpenseDao(dslContext().configuration());
  }

  @Bean
  public ExpenseCategoryDao expenseCategoryDao() {
    return new ExpenseCategoryDao(dslContext().configuration());
  }

  @Bean
  public FixedExpenseDao fixedExpenseDao() {
    return new FixedExpenseDao(dslContext().configuration());
  }

  @Bean
  public FixedExpenseUserDao fixedExpenseUserDao() {
    return new FixedExpenseUserDao(dslContext().configuration());
  }

  @Bean
  public FixedRevenueDao fixedFixedRevenueDao() {
    return new FixedRevenueDao(dslContext().configuration());
  }

  @Bean
  public RevenueDao revenueDao() {
    return new RevenueDao(dslContext().configuration());
  }

  @Bean
  public RevenueCategoryDao revenueCategoryDao() {
    return new RevenueCategoryDao(dslContext().configuration());
  }

  @Bean
  public UserDao userDao() {
    return new UserDao(dslContext().configuration());
  }

}
