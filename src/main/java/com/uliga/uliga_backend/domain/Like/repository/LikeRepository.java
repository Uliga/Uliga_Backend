package com.uliga.uliga_backend.domain.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uliga.uliga_backend.domain.like.model.Liked;

public interface LikeRepository extends JpaRepository<Liked, Long> {
}
