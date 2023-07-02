package com.uliga.uliga_backend.domain.Category.dao;

import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByAccountBookIdAndName(Long id, String name);

    void deleteById(Long id);

    @Query("SELECT c.name FROM Category c WHERE c.accountBook.id = :accountBookId")
    HashSet<String> findCategoryNamesByAccountBookId(@Param("accountBookId") Long accountBookId);

    Optional<Category> findByAccountBookIdAndName(Long accountBookId, String name);

    Optional<Category> findByAccountBookAndName(AccountBook accountBook, String name);

    List<Category> findCategoriesByAccountBookId(Long accountBookId);
    @Query("SELECT c FROM Category c where c.accountBook.id in (:accountBookIds) AND c.name = '기타'")
    List<Category> findCategoriesByAccountBookIds(@Param("accountBookIds") List<Long> accountBookIds);

    @Query("SELECT c " +
            "From Category c " +
            "join AccountBookMember abm ON abm.accountBook.id = c.accountBook.id " +
            "WHERE abm.member.id=:id " +
            "AND c.name=:name")
    List<Category> findCategoriesByMemberIdAndName(@Param("id") Long memberId, @Param("name") String name);
}
