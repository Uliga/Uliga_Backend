package com.uliga.uliga_backend.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uliga.uliga_backend.domain.post.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
