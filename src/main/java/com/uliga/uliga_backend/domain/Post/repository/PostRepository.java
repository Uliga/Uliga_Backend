package com.uliga.uliga_backend.domain.Post.repository;

import com.uliga.uliga_backend.domain.Post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostRepository extends JpaRepository<Post, Long> {
}
