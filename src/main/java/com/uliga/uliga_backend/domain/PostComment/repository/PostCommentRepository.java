package com.uliga.uliga_backend.domain.PostComment.repository;

import com.uliga.uliga_backend.domain.PostComment.model.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
}
