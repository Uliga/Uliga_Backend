package com.uliga.uliga_backend.domain.Schedule.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSchedule is a Querydsl query type for Schedule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSchedule extends EntityPathBase<Schedule> {

    private static final long serialVersionUID = 701909285L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSchedule schedule = new QSchedule("schedule");

    public final com.uliga.uliga_backend.domain.Common.QBaseTimeEntity _super = new com.uliga.uliga_backend.domain.Common.QBaseTimeEntity(this);

    public final com.uliga.uliga_backend.domain.AccountBook.model.QAccountBook accountBook;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createTime = _super.createTime;

    public final com.uliga.uliga_backend.domain.Member.model.QMember creator;

    public final com.uliga.uliga_backend.domain.Common.QDate dueDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedTime = _super.modifiedTime;

    public final com.uliga.uliga_backend.domain.Common.QDate notificationDate;

    public final NumberPath<Long> value = createNumber("value", Long.class);

    public QSchedule(String variable) {
        this(Schedule.class, forVariable(variable), INITS);
    }

    public QSchedule(Path<? extends Schedule> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSchedule(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSchedule(PathMetadata metadata, PathInits inits) {
        this(Schedule.class, metadata, inits);
    }

    public QSchedule(Class<? extends Schedule> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.accountBook = inits.isInitialized("accountBook") ? new com.uliga.uliga_backend.domain.AccountBook.model.QAccountBook(forProperty("accountBook")) : null;
        this.creator = inits.isInitialized("creator") ? new com.uliga.uliga_backend.domain.Member.model.QMember(forProperty("creator")) : null;
        this.dueDate = inits.isInitialized("dueDate") ? new com.uliga.uliga_backend.domain.Common.QDate(forProperty("dueDate")) : null;
        this.notificationDate = inits.isInitialized("notificationDate") ? new com.uliga.uliga_backend.domain.Common.QDate(forProperty("notificationDate")) : null;
    }

}

