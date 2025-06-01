package com.uliga.uliga_backend.domain.post_comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uliga.uliga_backend.domain.post_comment.model.PostComment;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
}
