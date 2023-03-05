package com.uliga.uliga_backend.domain.JoinTable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAccountBookMember is a Querydsl query type for AccountBookMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccountBookMember extends EntityPathBase<AccountBookMember> {

    private static final long serialVersionUID = 1838293560L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAccountBookMember accountBookMember = new QAccountBookMember("accountBookMember");

    public final com.uliga.uliga_backend.domain.AccountBook.model.QAccountBook accountBook;

    public final EnumPath<com.uliga.uliga_backend.domain.AccountBook.model.AccountBookAuthority> accountBookAuthority = createEnum("accountBookAuthority", com.uliga.uliga_backend.domain.AccountBook.model.AccountBookAuthority.class);

    public final BooleanPath getNotification = createBoolean("getNotification");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.uliga.uliga_backend.domain.Member.model.QMember member;

    public QAccountBookMember(String variable) {
        this(AccountBookMember.class, forVariable(variable), INITS);
    }

    public QAccountBookMember(Path<? extends AccountBookMember> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAccountBookMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAccountBookMember(PathMetadata metadata, PathInits inits) {
        this(AccountBookMember.class, metadata, inits);
    }

    public QAccountBookMember(Class<? extends AccountBookMember> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.accountBook = inits.isInitialized("accountBook") ? new com.uliga.uliga_backend.domain.AccountBook.model.QAccountBook(forProperty("accountBook")) : null;
        this.member = inits.isInitialized("member") ? new com.uliga.uliga_backend.domain.Member.model.QMember(forProperty("member")) : null;
    }

}

