package com.uliga.uliga_backend.domain.Member.model;

import com.uliga.uliga_backend.domain.JoinTable.AccountBookMember;
import com.uliga.uliga_backend.domain.JoinTable.PostComment;
import com.uliga.uliga_backend.domain.JoinTable.RecordComment;
import com.uliga.uliga_backend.domain.Like.model.Like;
import com.uliga.uliga_backend.domain.Post.model.Post;
import com.uliga.uliga_backend.domain.Record.model.Record;
import com.uliga.uliga_backend.domain.Schedule.model.Schedule;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member extends MemberBase{
    private String userName;

    private String nickname;

    private String avatarUrl;
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private final List<AccountBookMember> accountBooks = new ArrayList<>();


    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private final List<Schedule> schedules = new ArrayList<>();
    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private final List<Record> records = new ArrayList<>();

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private final List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private final List<RecordComment> recordComments = new ArrayList<>();

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private final List<PostComment> postComments = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private final List<Like> likedPosts = new ArrayList<>();

    @Builder
    public Member(Long id, String email, String password, Authority authority, UserLoginType userLoginType, String userName, String nickname, String avatarUrl) {
        super(id, email, password, authority, userLoginType);
        this.userName = userName;
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
    }
}
