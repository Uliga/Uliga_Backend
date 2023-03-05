package com.uliga.uliga_backend.domain.Like.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLiked is a Querydsl query type for Liked
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLiked extends EntityPathBase<Liked> {

    private static final long serialVersionUID = -1948275745L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLiked liked = new QLiked("liked");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.uliga.uliga_backend.domain.Member.model.QMember member;

    public final com.uliga.uliga_backend.domain.Post.model.QPost post;

    public QLiked(String variable) {
        this(Liked.class, forVariable(variable), INITS);
    }

    public QLiked(Path<? extends Liked> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLiked(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLiked(PathMetadata metadata, PathInits inits) {
        this(Liked.class, metadata, inits);
    }

    public QLiked(Class<? extends Liked> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.uliga.uliga_backend.domain.Member.model.QMember(forProperty("member")) : null;
        this.post = inits.isInitialized("post") ? new com.uliga.uliga_backend.domain.Post.model.QPost(forProperty("post"), inits.get("post")) : null;
    }

}

