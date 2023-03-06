package com.uliga.uliga_backend.domain.Member.model;

import com.uliga.uliga_backend.domain.JoinTable.model.AccountBookMember;
import com.uliga.uliga_backend.domain.PostComment.model.PostComment;
import com.uliga.uliga_backend.domain.RecordComment.model.RecordComment;
import com.uliga.uliga_backend.domain.Like.model.Liked;
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
public class Member extends MemberBase {
    private String userName;

    private String nickName;

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
    private final List<Liked> likedPosts = new ArrayList<>();

    @Builder
    public Member(Long id, String email, String password, Authority authority, UserLoginType userLoginType, String userName, String applicationPassword, String nickName, String avatarUrl) {
        super(id, email, password, applicationPassword, authority, userLoginType);
        this.userName = userName;
        this.nickName = nickName;
        this.avatarUrl = avatarUrl;
    }

    public void updatePassword(String newPassword) {
        super.updatePassword(newPassword);
    }

    public void updateApplicationPassword(String newPassword) {
        super.updateApplicationPassword(newPassword);
    }

    public void updateAvatarUrl(String updateUrl) {
        this.avatarUrl = updateUrl;
    }

    public void updateNickname(String nickName) {
        this.nickName = nickName;
    }
}
