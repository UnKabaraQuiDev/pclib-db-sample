# PCLib DB Spring sample

This zip contains three real Spring Boot command-line sample projects. They are not JUnit tests.

The sample uses `pclib-db-spring` and `pclib-db-all` with three connectors at the same time:

- SQLite
- MySQL
- PostgreSQL

MySQL and PostgreSQL run through Docker Compose. All database properties live in each version's `src/main/resources/application.yml`.

## Run the full sequence

To test, run:

```bash
./run-sample.sh
```

The script runs:

1. `pclib-db-sample-v1`
2. `pclib-db-sample-v2`
3. `pclib-db-sample-cleanup`

The cleanup app drops the SQLite/MySQL/PostgreSQL sample databases. The script also runs `docker compose down -v` at the end.

## Run only the databases

```bash
docker compose up -d mysql postgres
```

Then run a version from the host:

```bash
mvn -f pclib-db-sample-v1/pom.xml spring-boot:run
mvn -f pclib-db-sample-v2/pom.xml spring-boot:run
mvn -f pclib-db-sample-cleanup/pom.xml spring-boot:run
```

## Version layout

- `pclib-db-sample-v1` creates the initial schema and inserts/query data.
- `pclib-db-sample-v2` simulates a new app version. It adds the `loyaltyTier` column and lets `pclib-db-spring` apply migrations from startup config.
- `pclib-db-sample-cleanup` drops the views, tables, and databases.

## What it demonstrates

- repeated `@Unique` on the same column
- per-DBMS repeated `@DefaultValue`
- direct `@TypeHint`
- meta-annotation type hints through `@MaxLength`
- queryable hints through `@DemoTableOptions`
- PostgreSQL schema hints through `PostgreSQLTableHints.SCHEMA`
- annotation-based views with `@DB_View`, `@ViewTable`, and `@ViewColumn`
- a Spring `@Component DataBaseTypeFactory` for the custom `Money` value type
- `@Query` methods with `{NAME}`, `{Q:...}`, `{F:sum}`, and custom `{F:text_length}`
- a Spring `@Configuration` that registers `text_length` in all three SQL function resolvers
- migration by running app versions in order instead of calling `DataBase#migrate(...)` in the runner
