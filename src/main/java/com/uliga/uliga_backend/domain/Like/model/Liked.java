package com.uliga.uliga_backend.domain.Like.model;

import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.domain.Post.model.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "liked", catalog = "uliga_db")
public class Liked {
    @Id
    @GeneratedValue
    @Column(name = "liked_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}

