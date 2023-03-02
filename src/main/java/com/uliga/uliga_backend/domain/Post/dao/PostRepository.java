package com.uliga.uliga_backend.domain.Post.dao;

import com.uliga.uliga_backend.domain.Post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
