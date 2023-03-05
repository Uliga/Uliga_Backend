package com.uliga.uliga_backend.domain.Post.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = -801986171L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final com.uliga.uliga_backend.domain.Common.QBaseTimeEntity _super = new com.uliga.uliga_backend.domain.Common.QBaseTimeEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createTime = _super.createTime;

    public final com.uliga.uliga_backend.domain.Member.model.QMember creator;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.uliga.uliga_backend.domain.Like.model.Liked, com.uliga.uliga_backend.domain.Like.model.QLiked> likeds = this.<com.uliga.uliga_backend.domain.Like.model.Liked, com.uliga.uliga_backend.domain.Like.model.QLiked>createList("likeds", com.uliga.uliga_backend.domain.Like.model.Liked.class, com.uliga.uliga_backend.domain.Like.model.QLiked.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedTime = _super.modifiedTime;

    public final ListPath<com.uliga.uliga_backend.domain.PostComment.model.PostComment, com.uliga.uliga_backend.domain.PostComment.model.QPostComment> postComments = this.<com.uliga.uliga_backend.domain.PostComment.model.PostComment, com.uliga.uliga_backend.domain.PostComment.model.QPostComment>createList("postComments", com.uliga.uliga_backend.domain.PostComment.model.PostComment.class, com.uliga.uliga_backend.domain.PostComment.model.QPostComment.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.creator = inits.isInitialized("creator") ? new com.uliga.uliga_backend.domain.Member.model.QMember(forProperty("creator")) : null;
    }

}

