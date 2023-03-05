package com.uliga.uliga_backend.domain.RecordComment.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecordComment is a Querydsl query type for RecordComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecordComment extends EntityPathBase<RecordComment> {

    private static final long serialVersionUID = -1297293285L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecordComment recordComment = new QRecordComment("recordComment");

    public final com.uliga.uliga_backend.domain.Common.QBaseTimeEntity _super = new com.uliga.uliga_backend.domain.Common.QBaseTimeEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createTime = _super.createTime;

    public final com.uliga.uliga_backend.domain.Member.model.QMember creator;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedTime = _super.modifiedTime;

    public final com.uliga.uliga_backend.domain.Record.model.QRecord record;

    public QRecordComment(String variable) {
        this(RecordComment.class, forVariable(variable), INITS);
    }

    public QRecordComment(Path<? extends RecordComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecordComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecordComment(PathMetadata metadata, PathInits inits) {
        this(RecordComment.class, metadata, inits);
    }

    public QRecordComment(Class<? extends RecordComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.creator = inits.isInitialized("creator") ? new com.uliga.uliga_backend.domain.Member.model.QMember(forProperty("creator")) : null;
        this.record = inits.isInitialized("record") ? new com.uliga.uliga_backend.domain.Record.model.QRecord(forProperty("record"), inits.get("record")) : null;
    }

}

