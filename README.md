# PCLib DB native-image schema sample test

This is a four-module Maven project derived from the supplied v1 sample.
Each module is a separate Spring Boot 4.1 / Java 25 GraalVM native image, and
each starts in the **same persistent databases**. This exercises schema
migration and persistence across successive application versions.

## Prerequisites

- JDK 25 and Maven
- Docker Engine with the Compose v2 plugin
- Local Maven access to `lu.kbra:pclib:1.2.4-SNAPSHOT` (including
  `pclib-db-spring` and `pclib-db-all`)
- Sufficient RAM/disk for four sequential Paketo/GraalVM native builds

Run in the project root:

```bash
./build.sh       # Build native images v1, v2, v3, v4 in order
./run.sh         # Start DBs, run each image in order, clean up on exit
```

### JVM / Docker mode (no GraalVM native-image build)

```bash
./build-jvm.sh   # Package four executable Spring Boot JARs, then build JVM Docker images
./run-jvm.sh     # Start Docker databases, run v1 → v4 in JVM containers, clean up
# or ./all-jvm.sh to do both
```

The JVM image uses Eclipse Temurin Java 25. The JVM build skips Spring AOT.
`docker-compose-native.yml` and `docker-compose-jvm.yml` use separate Compose
project names and database volumes, so the two workflows do not share data.
Use `KEEP_DATA=1 ./run-jvm.sh` to retain JVM databases for debugging, then
`./clean-jvm.sh` to remove them. Both cleanup scripts delete their mode's
database volumes. The original `docker-compose.yml` is retained for compatibility.

Or run `./all.sh` to do both. The default `run.sh` cleanup executes even
when a version fails, and calls `docker compose down --volumes --remove-orphans`.
**That deletes the database and SQLite data in this Compose project.**
To retain data for debugging, run `KEEP_DATA=1 ./run.sh`, inspect it,
then call `./clean.sh`. Do not set `KEEP_DATA=1` for normal repeatable runs:
the runners expect a fresh database for v1.

Each version inserts one distinct customer and two purchases into every
database. From v2 onwards, the runner checks that previous customers remain,
instead of clearing tables. The original v1 runner's `clear()` and `drop()`
operations were removed.

## What changes between versions?

| Stage | CustomerData | PurchaseData |
| --- | --- | --- |
| v1 | Original `note`, `name`, `tenant`, etc. | Original `status` and order columns |
| v2 | Rename `note` → `customer_note`; add nullable `phone_number` | Add nullable `tracking_reference` |
| v3 | Remove `phone_number` | Rename `status` → `fulfillment_status`; add nullable `shipped_on` |
| v4 | Rename `customer_note` → `remarks`; add nullable `marketing_opt_in`; grow `name` max length to 120 | Remove `tracking_reference`; add nullable `payment_reference` |

These are **physical column changes** defined by `@Column(name = ...)`.
A rename may be interpreted by the PCLib migration engine as *remove old
column + add new column*, not as a data-preserving SQL `RENAME COLUMN`.
The test preserves rows, but does not claim to preserve values in renamed
columns. For production data, introduce explicit data migrations where needed.

v1 keeps `auto-migrate`, `auto-add-columns` and `auto-remove-columns`
disabled; v2–v4 enable them to exercise the PCLib automatic migration
settings. The table names, important query columns, and purchase summary
views remain consistent. The views' SQL sources are unchanged.

## Database settings

The supplied sample uses `application.yml` (rather than
`application.properties`). Docker Compose passes its exact variable names:

| Variable | Container value | Default in application.yml |
| --- | --- | --- |
| `PCLIB_SAMPLE_MYSQL_HOST` | `mysql` | `localhost` |
| `PCLIB_SAMPLE_MYSQL_PORT` | `3306` | `3306` |
| `PCLIB_SAMPLE_MYSQL_USERNAME` | `root` | `root` |
| `PCLIB_SAMPLE_MYSQL_PASSWORD` | `root` (overridable) | `root` |
| `PCLIB_SAMPLE_POSTGRES_HOST` | `postgres` | `localhost` |
| `PCLIB_SAMPLE_POSTGRES_PORT` | `5432` | `5432` |
| `PCLIB_SAMPLE_POSTGRES_USERNAME` | `postgres` | `postgres` |
| `PCLIB_SAMPLE_POSTGRES_PASSWORD` | `postgres` (overridable) | `postgres` |
| `PCLIB_SAMPLE_SQLITE_DIR` | `/workspace/target/pclib-db-sample-sqlite` | `./target/pclib-db-sample-sqlite` |

For example, override credentials with environment variables passed to
`./run.sh`:

```bash
PCLIB_SAMPLE_MYSQL_PASSWORD=change-me PCLIB_SAMPLE_POSTGRES_PASSWORD=change-me ./run.sh
```

Compose initializes the MySQL database `pclib_sample_mysql` and
PostgreSQL database `pclib_sample_postgres`. SQLite persists in a shared
named volume across the four executions. Database ports are **not published
to the host** to avoid collisions with your existing MySQL and PostgreSQL.

The image tag defaults to `1.2.4-SNAPSHOT` via
`PCLIB_SAMPLE_IMAGE_TAG`; update that if you change the parent POM version.

## Debugging

To run a single image against the currently running databases:

```bash
docker compose up -d --wait mysql postgres
docker compose run --rm app-v2
```

Versioned runners verify exact prior row counts, so v2 requires a
successful v1, and so on. The image build has not been validated in this
archive environment, where Maven and Docker are not available; compile and
native-image validation should be performed on your machine with the
PCLib dependencies installed.
