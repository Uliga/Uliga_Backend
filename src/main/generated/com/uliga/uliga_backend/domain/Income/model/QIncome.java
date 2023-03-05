package com.uliga.uliga_backend.domain.Income.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QIncome is a Querydsl query type for Income
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QIncome extends EntityPathBase<Income> {

    private static final long serialVersionUID = 1494441573L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QIncome income = new QIncome("income");

    public final com.uliga.uliga_backend.domain.AccountBook.model.QAccountBook accountBook;

    public final com.uliga.uliga_backend.domain.Common.QDate date;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> value = createNumber("value", Long.class);

    public QIncome(String variable) {
        this(Income.class, forVariable(variable), INITS);
    }

    public QIncome(Path<? extends Income> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QIncome(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QIncome(PathMetadata metadata, PathInits inits) {
        this(Income.class, metadata, inits);
    }

    public QIncome(Class<? extends Income> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.accountBook = inits.isInitialized("accountBook") ? new com.uliga.uliga_backend.domain.AccountBook.model.QAccountBook(forProperty("accountBook")) : null;
        this.date = inits.isInitialized("date") ? new com.uliga.uliga_backend.domain.Common.QDate(forProperty("date")) : null;
    }

}

