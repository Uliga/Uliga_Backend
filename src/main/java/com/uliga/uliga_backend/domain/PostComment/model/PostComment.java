package com.uliga.uliga_backend.domain.PostComment.model;

import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.domain.Post.model.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "post_comment", catalog = "uliga_db")
public class PostComment {
    @Id
    @GeneratedValue
    @Column(name = "post_comment_id")
    private Long id;

    private String content;


    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member creator;
}

