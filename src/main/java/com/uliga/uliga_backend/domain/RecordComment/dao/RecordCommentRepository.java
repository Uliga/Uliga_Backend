package com.uliga.uliga_backend.domain.RecordComment.dao;

import com.uliga.uliga_backend.domain.RecordComment.model.RecordComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RecordCommentRepository extends JpaRepository<RecordComment, Long>, JpaSpecificationExecutor<RecordComment> {
}
