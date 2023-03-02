package com.uliga.uliga_backend.domain.Like.dao;

import com.uliga.uliga_backend.domain.Like.model.Liked;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Liked, Long> {
}
