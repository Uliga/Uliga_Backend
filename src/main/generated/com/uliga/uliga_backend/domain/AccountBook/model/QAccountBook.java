package com.uliga.uliga_backend.domain.AccountBook.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAccountBook is a Querydsl query type for AccountBook
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccountBook extends EntityPathBase<AccountBook> {

    private static final long serialVersionUID = 170024363L;

    public static final QAccountBook accountBook = new QAccountBook("accountBook");

    public final com.uliga.uliga_backend.domain.Common.QBaseTimeEntity _super = new com.uliga.uliga_backend.domain.Common.QBaseTimeEntity(this);

    public final ListPath<com.uliga.uliga_backend.domain.Budget.model.Budget, com.uliga.uliga_backend.domain.Budget.model.QBudget> budgets = this.<com.uliga.uliga_backend.domain.Budget.model.Budget, com.uliga.uliga_backend.domain.Budget.model.QBudget>createList("budgets", com.uliga.uliga_backend.domain.Budget.model.Budget.class, com.uliga.uliga_backend.domain.Budget.model.QBudget.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createTime = _super.createTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.uliga.uliga_backend.domain.Income.model.Income, com.uliga.uliga_backend.domain.Income.model.QIncome> incomes = this.<com.uliga.uliga_backend.domain.Income.model.Income, com.uliga.uliga_backend.domain.Income.model.QIncome>createList("incomes", com.uliga.uliga_backend.domain.Income.model.Income.class, com.uliga.uliga_backend.domain.Income.model.QIncome.class, PathInits.DIRECT2);

    public final BooleanPath isPrivate = createBoolean("isPrivate");

    public final ListPath<com.uliga.uliga_backend.domain.JoinTable.AccountBookMember, com.uliga.uliga_backend.domain.JoinTable.QAccountBookMember> members = this.<com.uliga.uliga_backend.domain.JoinTable.AccountBookMember, com.uliga.uliga_backend.domain.JoinTable.QAccountBookMember>createList("members", com.uliga.uliga_backend.domain.JoinTable.AccountBookMember.class, com.uliga.uliga_backend.domain.JoinTable.QAccountBookMember.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedTime = _super.modifiedTime;

    public final StringPath name = createString("name");

    public final ListPath<com.uliga.uliga_backend.domain.Record.model.Record, com.uliga.uliga_backend.domain.Record.model.QRecord> records = this.<com.uliga.uliga_backend.domain.Record.model.Record, com.uliga.uliga_backend.domain.Record.model.QRecord>createList("records", com.uliga.uliga_backend.domain.Record.model.Record.class, com.uliga.uliga_backend.domain.Record.model.QRecord.class, PathInits.DIRECT2);

    public final ListPath<com.uliga.uliga_backend.domain.Schedule.model.Schedule, com.uliga.uliga_backend.domain.Schedule.model.QSchedule> schedules = this.<com.uliga.uliga_backend.domain.Schedule.model.Schedule, com.uliga.uliga_backend.domain.Schedule.model.QSchedule>createList("schedules", com.uliga.uliga_backend.domain.Schedule.model.Schedule.class, com.uliga.uliga_backend.domain.Schedule.model.QSchedule.class, PathInits.DIRECT2);

    public QAccountBook(String variable) {
        super(AccountBook.class, forVariable(variable));
    }

    public QAccountBook(Path<? extends AccountBook> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccountBook(PathMetadata metadata) {
        super(AccountBook.class, metadata);
    }

}

