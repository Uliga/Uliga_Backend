package com.uliga.uliga_backend.domain.Record.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecord is a Querydsl query type for Record
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecord extends EntityPathBase<Record> {

    private static final long serialVersionUID = -1724387483L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecord record = new QRecord("record");

    public final com.uliga.uliga_backend.domain.Common.QBaseTimeEntity _super = new com.uliga.uliga_backend.domain.Common.QBaseTimeEntity(this);

    public final com.uliga.uliga_backend.domain.AccountBook.model.QAccountBook accountBook;

    public final com.uliga.uliga_backend.domain.Category.model.QCategory category;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createTime = _super.createTime;

    public final com.uliga.uliga_backend.domain.Member.model.QMember creator;

    public final com.uliga.uliga_backend.domain.Common.QDate date;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath memo = createString("memo");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedTime = _super.modifiedTime;

    public final ListPath<com.uliga.uliga_backend.domain.RecordComment.model.RecordComment, com.uliga.uliga_backend.domain.RecordComment.model.QRecordComment> recordComments = this.<com.uliga.uliga_backend.domain.RecordComment.model.RecordComment, com.uliga.uliga_backend.domain.RecordComment.model.QRecordComment>createList("recordComments", com.uliga.uliga_backend.domain.RecordComment.model.RecordComment.class, com.uliga.uliga_backend.domain.RecordComment.model.QRecordComment.class, PathInits.DIRECT2);

    public final NumberPath<Long> spend = createNumber("spend", Long.class);

    public QRecord(String variable) {
        this(Record.class, forVariable(variable), INITS);
    }

    public QRecord(Path<? extends Record> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecord(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecord(PathMetadata metadata, PathInits inits) {
        this(Record.class, metadata, inits);
    }

    public QRecord(Class<? extends Record> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.accountBook = inits.isInitialized("accountBook") ? new com.uliga.uliga_backend.domain.AccountBook.model.QAccountBook(forProperty("accountBook")) : null;
        this.category = inits.isInitialized("category") ? new com.uliga.uliga_backend.domain.Category.model.QCategory(forProperty("category")) : null;
        this.creator = inits.isInitialized("creator") ? new com.uliga.uliga_backend.domain.Member.model.QMember(forProperty("creator")) : null;
        this.date = inits.isInitialized("date") ? new com.uliga.uliga_backend.domain.Common.QDate(forProperty("date")) : null;
    }

}

