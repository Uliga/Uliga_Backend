package com.uliga.uliga_backend.domain.Member.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 144125445L;

    public static final QMember member = new QMember("member1");

    public final QMemberBase _super = new QMemberBase(this);

    public final ListPath<com.uliga.uliga_backend.domain.JoinTable.AccountBookMember, com.uliga.uliga_backend.domain.JoinTable.QAccountBookMember> accountBooks = this.<com.uliga.uliga_backend.domain.JoinTable.AccountBookMember, com.uliga.uliga_backend.domain.JoinTable.QAccountBookMember>createList("accountBooks", com.uliga.uliga_backend.domain.JoinTable.AccountBookMember.class, com.uliga.uliga_backend.domain.JoinTable.QAccountBookMember.class, PathInits.DIRECT2);

    //inherited
    public final StringPath applicationPassword = _super.applicationPassword;

    //inherited
    public final EnumPath<Authority> authority = _super.authority;

    public final StringPath avatarUrl = createString("avatarUrl");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createTime = _super.createTime;

    //inherited
    public final StringPath email = _super.email;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final ListPath<com.uliga.uliga_backend.domain.Like.model.Liked, com.uliga.uliga_backend.domain.Like.model.QLiked> likedPosts = this.<com.uliga.uliga_backend.domain.Like.model.Liked, com.uliga.uliga_backend.domain.Like.model.QLiked>createList("likedPosts", com.uliga.uliga_backend.domain.Like.model.Liked.class, com.uliga.uliga_backend.domain.Like.model.QLiked.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedTime = _super.modifiedTime;

    public final StringPath nickName = createString("nickName");

    //inherited
    public final StringPath password = _super.password;

    public final ListPath<com.uliga.uliga_backend.domain.PostComment.model.PostComment, com.uliga.uliga_backend.domain.PostComment.model.QPostComment> postComments = this.<com.uliga.uliga_backend.domain.PostComment.model.PostComment, com.uliga.uliga_backend.domain.PostComment.model.QPostComment>createList("postComments", com.uliga.uliga_backend.domain.PostComment.model.PostComment.class, com.uliga.uliga_backend.domain.PostComment.model.QPostComment.class, PathInits.DIRECT2);

    public final ListPath<com.uliga.uliga_backend.domain.Post.model.Post, com.uliga.uliga_backend.domain.Post.model.QPost> posts = this.<com.uliga.uliga_backend.domain.Post.model.Post, com.uliga.uliga_backend.domain.Post.model.QPost>createList("posts", com.uliga.uliga_backend.domain.Post.model.Post.class, com.uliga.uliga_backend.domain.Post.model.QPost.class, PathInits.DIRECT2);

    public final ListPath<com.uliga.uliga_backend.domain.RecordComment.model.RecordComment, com.uliga.uliga_backend.domain.RecordComment.model.QRecordComment> recordComments = this.<com.uliga.uliga_backend.domain.RecordComment.model.RecordComment, com.uliga.uliga_backend.domain.RecordComment.model.QRecordComment>createList("recordComments", com.uliga.uliga_backend.domain.RecordComment.model.RecordComment.class, com.uliga.uliga_backend.domain.RecordComment.model.QRecordComment.class, PathInits.DIRECT2);

    public final ListPath<com.uliga.uliga_backend.domain.Record.model.Record, com.uliga.uliga_backend.domain.Record.model.QRecord> records = this.<com.uliga.uliga_backend.domain.Record.model.Record, com.uliga.uliga_backend.domain.Record.model.QRecord>createList("records", com.uliga.uliga_backend.domain.Record.model.Record.class, com.uliga.uliga_backend.domain.Record.model.QRecord.class, PathInits.DIRECT2);

    public final ListPath<com.uliga.uliga_backend.domain.Schedule.model.Schedule, com.uliga.uliga_backend.domain.Schedule.model.QSchedule> schedules = this.<com.uliga.uliga_backend.domain.Schedule.model.Schedule, com.uliga.uliga_backend.domain.Schedule.model.QSchedule>createList("schedules", com.uliga.uliga_backend.domain.Schedule.model.Schedule.class, com.uliga.uliga_backend.domain.Schedule.model.QSchedule.class, PathInits.DIRECT2);

    //inherited
    public final EnumPath<UserLoginType> userLoginType = _super.userLoginType;

    public final StringPath userName = createString("userName");

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

