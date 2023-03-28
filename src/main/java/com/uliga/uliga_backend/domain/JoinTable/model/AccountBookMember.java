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
public class AccountBookMember {
    @Id
    @GeneratedValue
    @Column(name = "accountBookMember_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "accountBook_id")
    private AccountBook accountBook;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private Boolean getNotification;
    @Enumerated(EnumType.STRING)
    private AccountBookAuthority accountBookAuthority;
    @Builder
    public AccountBookMember(AccountBook accountBook, Member member, Boolean getNotification, AccountBookAuthority accountBookAuthority) {
        this.accountBook = accountBook;
        this.member = member;
        this.getNotification = getNotification;
        this.accountBookAuthority = accountBookAuthority;
    }
}