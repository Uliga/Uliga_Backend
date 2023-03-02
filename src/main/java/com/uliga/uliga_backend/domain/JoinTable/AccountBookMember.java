package com.uliga.uliga_backend.domain.JoinTable;

import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBookAuthority;
import com.uliga.uliga_backend.domain.Member.model.Member;
import jakarta.persistence.*;
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

    private AccountBookAuthority accountBookAuthority;
}