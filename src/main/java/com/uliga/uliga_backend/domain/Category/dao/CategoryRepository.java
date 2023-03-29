package com.uliga.uliga_backend.domain.Category.dao;

import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByAccountBookIdAndName(Long id, String name);

    void deleteById(Long id);

    Optional<Category> findByAccountBookIdAndName(Long accountBookId, String name);

    Optional<Category> findByAccountBookAndName(AccountBook accountBook, String name);

    @Query("SELECT c " +
            "From Category c " +
            "join AccountBookMember abm ON abm.accountBook.id = c.accountBook.id " +
            "WHERE abm.member.id=:id " +
            "AND c.name=:name")
    List<Category> findCategoriesByMemberIdAndName(@Param("id") Long memberId, @Param("name") String name);
}
