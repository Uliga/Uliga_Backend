package com.uliga.uliga_backend.domain.Member.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMemberBase is a Querydsl query type for MemberBase
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberBase extends EntityPathBase<MemberBase> {

    private static final long serialVersionUID = 1840651894L;

    public static final QMemberBase memberBase = new QMemberBase("memberBase");

    public final com.uliga.uliga_backend.domain.Common.QBaseTimeEntity _super = new com.uliga.uliga_backend.domain.Common.QBaseTimeEntity(this);

    public final StringPath applicationPassword = createString("applicationPassword");

    public final EnumPath<Authority> authority = createEnum("authority", Authority.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createTime = _super.createTime;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedTime = _super.modifiedTime;

    public final StringPath password = createString("password");

    public final EnumPath<UserLoginType> userLoginType = createEnum("userLoginType", UserLoginType.class);

    public QMemberBase(String variable) {
        super(MemberBase.class, forVariable(variable));
    }

    public QMemberBase(Path<? extends MemberBase> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMemberBase(PathMetadata metadata) {
        super(MemberBase.class, metadata);
    }

}

