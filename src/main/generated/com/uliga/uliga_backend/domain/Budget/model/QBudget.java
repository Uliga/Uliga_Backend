package com.uliga.uliga_backend.domain.Budget.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBudget is a Querydsl query type for Budget
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBudget extends EntityPathBase<Budget> {

    private static final long serialVersionUID = -1088988187L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBudget budget = new QBudget("budget");

    public final com.uliga.uliga_backend.domain.Common.QBaseTimeEntity _super = new com.uliga.uliga_backend.domain.Common.QBaseTimeEntity(this);

    public final com.uliga.uliga_backend.domain.AccountBook.model.QAccountBook accountBook;

    public final com.uliga.uliga_backend.domain.Category.model.QCategory category;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createTime = _super.createTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedTime = _super.modifiedTime;

    public final StringPath month = createString("month");

    public final NumberPath<Long> value = createNumber("value", Long.class);

    public QBudget(String variable) {
        this(Budget.class, forVariable(variable), INITS);
    }

    public QBudget(Path<? extends Budget> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBudget(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBudget(PathMetadata metadata, PathInits inits) {
        this(Budget.class, metadata, inits);
    }

    public QBudget(Class<? extends Budget> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.accountBook = inits.isInitialized("accountBook") ? new com.uliga.uliga_backend.domain.AccountBook.model.QAccountBook(forProperty("accountBook")) : null;
        this.category = inits.isInitialized("category") ? new com.uliga.uliga_backend.domain.Category.model.QCategory(forProperty("category")) : null;
    }

}

