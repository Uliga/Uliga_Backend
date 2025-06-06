package com.uliga.uliga_backend.member.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Member extends MemberBase {
  // private Boolean deleted;
  // @Column(name = "user_name")
  // private String userName;
  // @Column(name = "nick_name")
  // private String nickName;

  // @OneToOne
  // @JoinColumn(name = "account_book_id")
  // private AccountBook privateAccountBook;
  // @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade =
  // CascadeType.REMOVE)
  // private final List<AccountBookMember> accountBooks = new ArrayList<>();
  // @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade =
  // CascadeType.REMOVE)
  // private List<ScheduleMember> scheduleMembers = new ArrayList<>();

  // @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY, cascade =
  // CascadeType.REMOVE)
  // private final List<Schedule> schedules = new ArrayList<>();
  // @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY, cascade =
  // CascadeType.REMOVE)
  // private final List<AccountBookData> records = new ArrayList<>();

  // @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY, cascade =
  // CascadeType.REMOVE)
  // private final List<AccountBookData> incomes = new ArrayList<>();

  // @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY, cascade =
  // CascadeType.REMOVE)
  // private final List<Post> posts = new ArrayList<>();

  // @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY, cascade =
  // CascadeType.REMOVE)
  // private final List<RecordComment> recordComments = new ArrayList<>();

  // @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY, cascade =
  // CascadeType.REMOVE)
  // private final List<PostComment> postComments = new ArrayList<>();

  // @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade =
  // CascadeType.REMOVE)
  // private final List<Liked> likedPosts = new ArrayList<>();

  // @Builder
  // public Member(Long id, String email, String password, Authority authority,
  // UserLoginType userLoginType, String userName, String applicationPassword,
  // String nickName, Boolean deleted) {
  // super(id, email, password, applicationPassword, authority, userLoginType);
  // this.userName = userName;
  // this.nickName = nickName;
  // this.deleted = deleted;
  // }

  // public MemberInfoNativeQ toMemberInfoQ() {
  // return MemberInfoNativeQ.builder()
  // .id(getId())
  // .email(getEmail())
  // .nickName(nickName)
  // .userName(userName)
  // .privateAccountBookId(getPrivateAccountBook().getId())
  // .build();
  // }

  // public void delete() {
  // this.privateAccountBook = null;
  // this.deleted = true;
  // }

  // public void updatePassword(String newPassword) {
  // super.updatePassword(newPassword);
  // }

  // p

  // public void updateNickname(String nickName) {
  // this.nickName = nickName;
  // }

  // public void setPrivateAccountBook(AccountBook accountBook) {
  // this.privateAccountBook = accountBook;
  // }

  // public void updateUserName(String userName) {
  // this.userName = userName;
  // }
}
