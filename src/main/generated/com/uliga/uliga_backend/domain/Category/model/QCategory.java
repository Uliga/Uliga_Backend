package com.uliga.uliga_backend.domain.Category.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCategory is a Querydsl query type for Category
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCategory extends EntityPathBase<Category> {

    private static final long serialVersionUID = -1905439163L;

    public static final QCategory category = new QCategory("category");

    public final ListPath<com.uliga.uliga_backend.domain.Budget.model.Budget, com.uliga.uliga_backend.domain.Budget.model.QBudget> budgets = this.<com.uliga.uliga_backend.domain.Budget.model.Budget, com.uliga.uliga_backend.domain.Budget.model.QBudget>createList("budgets", com.uliga.uliga_backend.domain.Budget.model.Budget.class, com.uliga.uliga_backend.domain.Budget.model.QBudget.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final ListPath<com.uliga.uliga_backend.domain.Record.model.Record, com.uliga.uliga_backend.domain.Record.model.QRecord> records = this.<com.uliga.uliga_backend.domain.Record.model.Record, com.uliga.uliga_backend.domain.Record.model.QRecord>createList("records", com.uliga.uliga_backend.domain.Record.model.Record.class, com.uliga.uliga_backend.domain.Record.model.QRecord.class, PathInits.DIRECT2);

    public QCategory(String variable) {
        super(Category.class, forVariable(variable));
    }

    public QCategory(Path<? extends Category> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCategory(PathMetadata metadata) {
        super(Category.class, metadata);
    }

}

