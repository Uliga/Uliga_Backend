package com.uliga.uliga_backend.domain.PostComment.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPostComment is a Querydsl query type for PostComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostComment extends EntityPathBase<PostComment> {

    private static final long serialVersionUID = 334435133L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPostComment postComment = new QPostComment("postComment");

    public final StringPath content = createString("content");

    public final com.uliga.uliga_backend.domain.Member.model.QMember creator;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.uliga.uliga_backend.domain.Post.model.QPost post;

    public QPostComment(String variable) {
        this(PostComment.class, forVariable(variable), INITS);
    }

    public QPostComment(Path<? extends PostComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPostComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPostComment(PathMetadata metadata, PathInits inits) {
        this(PostComment.class, metadata, inits);
    }

    public QPostComment(Class<? extends PostComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.creator = inits.isInitialized("creator") ? new com.uliga.uliga_backend.domain.Member.model.QMember(forProperty("creator")) : null;
        this.post = inits.isInitialized("post") ? new com.uliga.uliga_backend.domain.Post.model.QPost(forProperty("post"), inits.get("post")) : null;
    }

}

