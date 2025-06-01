package com.uliga.uliga_backend.domain.record_comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uliga.uliga_backend.domain.record_comment.model.RecordComment;

public interface RecordCommentRepository extends JpaRepository<RecordComment, Long> {
}
