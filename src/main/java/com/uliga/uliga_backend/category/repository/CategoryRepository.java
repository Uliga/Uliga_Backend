package com.uliga.uliga_backend.category.repository;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.uliga.uliga_backend.jooq.tables.daos.ExpenseCategoryDao;
import com.uliga.uliga_backend.jooq.tables.daos.RevenueCategoryDao;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {
  private final DSLContext dsl;
  private final RevenueCategoryDao revenueCategoryDao;
  private final ExpenseCategoryDao expenseCategoryDao;

  /*
   * @Query("select new com.uliga.uliga_backend.domain.Category.dto.NativeQ.AccountBookCategoryInfoQ("
   * +
   * "c.id," +
   * "c.name)" +
   * "FROM " +
   * "AccountBook ab JOIN Category c on c.accountBook.id = ab.id WHERE ab.id = :id"
   * )
   * List<AccountBookCategoryInfoQ> findAccountBookCategoryInfoById(@Param("id")
   * Long id);
   * 
   * @Query("select new com.uliga.uliga_backend.domain.Category.dto.NativeQ.AccountBookCategoryInfoQ("
   * +
   * "c.id," +
   * "c.name)" +
   * "FROM " +
   * "AccountBook ab JOIN Category c on c.accountBook.id = ab.id WHERE ab.id = :id ORDER BY c.name LIMIT 5"
   * )
   * List<AccountBookCategoryInfoQ> findAccountBookCategoryAnalyze(@Param("id")
   * Long id);
   * 
   * boolean existsByAccountBookIdAndName(Long id, String name);
   * 
   * @Query("SELECT NEW com.uliga.uliga_backend.domain.Category.dto.NativeQ.AccountBookCategoryAnalyzeQ("
   * +
   * "c.id, " +
   * "c.name, " +
   * "SUM(r.value)) " +
   * "FROM Category c " +
   * "JOIN Record r ON r.category.id = c.id " +
   * "WHERE c.accountBook.id = :id  " +
   * "AND r.date.year = :year " +
   * "AND r.date.month=:month " +
   * "group by c " +
   * "order by SUM(r.value) DESC LIMIT 5")
   * List<AccountBookCategoryAnalyzeQ> findAccountBookCategoryAnalyze(@Param("id")
   * Long id, @Param("year") Long year,
   * 
   * @Param("month") Long month);
   * 
   * @Query(value = "SELECT " +
   * "c.name " +
   * "FROM category c " +
   * "JOIN account_book_data r ON r.category_id = c.category_id " +
   * "WHERE c.account_book_id = :id " +
   * "AND r.year = :year " +
   * "AND r.month = :month " +
   * "group by r.category_id " +
   * "order by SUM(r.value) DESC LIMIT 5, 100", nativeQuery = true)
   * List<String> findExtraAccountBookCategory(@Param("id") Long
   * id, @Param("year") Long year,
   * 
   * @Param("month") Long month);
   * 
   * void deleteById(Long id);
   * 
   * void deleteByNameAndAccountBookId(String name, Long accountBookId);
   * 
   * @Query("SELECT c.name FROM Category c WHERE c.accountBook.id = :accountBookId"
   * )
   * HashSet<String> findCategoryNamesByAccountBookId(@Param("accountBookId") Long
   * accountBookId);
   * 
   * Optional<Category> findByAccountBookIdAndName(Long accountBookId, String
   * name);
   * 
   * Optional<Category> findByAccountBookAndName(AccountBook accountBook, String
   * name);
   * 
   * List<Category> findCategoriesByAccountBookId(Long accountBookId);
   * 
   * @Query("SELECT c FROM Category c where c.accountBook.id in (:accountBookIds) AND c.name = '기타'"
   * )
   * List<Category> findCategoriesByAccountBookIds(@Param("accountBookIds")
   * List<Long> accountBookIds);
   * 
   * @Query("SELECT c " +
   * "From Category c " +
   * "join AccountBookMember abm ON abm.accountBook.id = c.accountBook.id " +
   * "WHERE abm.member.id=:id " +
   * "AND c.name=:name")
   * List<Category> findCategoriesByMemberIdAndName(@Param("id") Long
   * memberId, @Param("name") String name);
   */
}