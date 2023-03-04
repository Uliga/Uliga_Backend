package com.uliga.uliga_backend.domain.Member.model;

import com.uliga.uliga_backend.domain.Common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class MemberBase extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String email;

    private String password;

    private String applicationPassword;

    @Enumerated(EnumType.STRING)
    protected Authority authority;

    @Enumerated(EnumType.STRING)
    private UserLoginType userLoginType;

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateApplicationPassword(String newPassword) {
        this.applicationPassword = newPassword;
    }
}
