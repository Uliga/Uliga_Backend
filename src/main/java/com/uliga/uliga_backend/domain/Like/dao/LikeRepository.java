package com.uliga.uliga_backend.domain.Like.dao;

import com.uliga.uliga_backend.domain.Like.model.Liked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LikeRepository extends JpaRepository<Liked, Long>, JpaSpecificationExecutor {
}
