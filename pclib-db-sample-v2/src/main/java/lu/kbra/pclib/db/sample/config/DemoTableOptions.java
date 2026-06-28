package lu.kbra.pclib.db.sample.config;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import lu.kbra.pclib.db.annotations.queryable.QueryableHint;
import lu.kbra.pclib.db.autobuild.postgres.PostgreSQLTableHints;
import lu.kbra.pclib.db.domain.table.meta.DefaultTableHints;

@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface DemoTableOptions {

	String POSTGRES_SCHEMA = "pclib_sample";

	@QueryableHint(type = DefaultTableHints.NAME_OVERRIDE)
	String name();

	@QueryableHint(dbms = "mysql", type = DefaultTableHints.ENGINE)
	String mysqlEngine() default "InnoDB";

	@QueryableHint(dbms = "mysql", type = DefaultTableHints.CHARACTER_SET)
	String mysqlCharset() default "utf8mb4";

	@QueryableHint(dbms = "postgresql", type = PostgreSQLTableHints.SCHEMA)
	String postgresSchema() default POSTGRES_SCHEMA;
}
