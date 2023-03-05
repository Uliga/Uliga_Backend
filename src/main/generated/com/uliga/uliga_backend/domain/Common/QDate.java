package com.uliga.uliga_backend.domain.Common;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDate is a Querydsl query type for Date
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QDate extends BeanPath<Date> {

    private static final long serialVersionUID = -1717358077L;

    public static final QDate date = new QDate("date");

    public final NumberPath<Long> day = createNumber("day", Long.class);

    public final NumberPath<Long> month = createNumber("month", Long.class);

    public final NumberPath<Long> year = createNumber("year", Long.class);

    public QDate(String variable) {
        super(Date.class, forVariable(variable));
    }

    public QDate(Path<? extends Date> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDate(PathMetadata metadata) {
        super(Date.class, metadata);
    }

}

