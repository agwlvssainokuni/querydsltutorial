package cherry.tutorial.querydsl.db;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QAuthor is a Querydsl query type for BAuthor
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QAuthor extends com.querydsl.sql.RelationalPathBase<BAuthor> {

    private static final long serialVersionUID = -1428955077;

    public static final QAuthor author = new QAuthor("author");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> lockVersion = createNumber("lockVersion", Integer.class);

    public final StringPath loginId = createString("loginId");

    public final StringPath name = createString("name");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final com.querydsl.sql.PrimaryKey<BAuthor> primary = createPrimaryKey(id);

    public QAuthor(String variable) {
        super(BAuthor.class, forVariable(variable), "null", "author");
        addMetadata();
    }

    public QAuthor(String variable, String schema, String table) {
        super(BAuthor.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QAuthor(String variable, String schema) {
        super(BAuthor.class, forVariable(variable), schema, "author");
        addMetadata();
    }

    public QAuthor(Path<? extends BAuthor> path) {
        super(path.getType(), path.getMetadata(), "null", "author");
        addMetadata();
    }

    public QAuthor(PathMetadata metadata) {
        super(BAuthor.class, metadata, "null", "author");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdAt, ColumnMetadata.named("created_at").withIndex(5).ofType(Types.TIMESTAMP).withSize(26).notNull());
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(lockVersion, ColumnMetadata.named("lock_version").withIndex(6).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(loginId, ColumnMetadata.named("login_id").withIndex(2).ofType(Types.VARCHAR).withSize(100).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(3).ofType(Types.VARCHAR).withSize(100).notNull());
        addMetadata(updatedAt, ColumnMetadata.named("updated_at").withIndex(4).ofType(Types.TIMESTAMP).withSize(26).notNull());
    }

}

