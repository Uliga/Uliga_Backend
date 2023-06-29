package com.uliga.uliga_backend.domain.JoinTable.model;

import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBookAuthority;
import com.uliga.uliga_backend.domain.Member.model.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "account_book_member", catalog = "uliga_db")
public class AccountBookMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_book_member_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_book_id")
    private AccountBook accountBook;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column(name = "get_notification")
    private Boolean getNotification;
    @Enumerated(EnumType.STRING)
    @Column(name = "account_book_authority")
    private AccountBookAuthority accountBookAuthority;
    @Builder
    public AccountBookMember(AccountBook accountBook, Member member, Boolean getNotification, AccountBookAuthority accountBookAuthority, String avatarUrl) {
        this.accountBook = accountBook;
        this.member = member;
        this.getNotification = getNotification;
        this.accountBookAuthority = accountBookAuthority;
        this.avatarUrl = avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}