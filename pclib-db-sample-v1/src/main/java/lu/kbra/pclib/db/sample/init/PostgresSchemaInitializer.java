package lu.kbra.pclib.db.sample.init;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import lu.kbra.pclib.db.base.DataBase;
import lu.kbra.pclib.db.exception.DBException;
import lu.kbra.pclib.db.sample.config.DemoTableOptions;

@Component
public class PostgresSchemaInitializer implements ApplicationListener<ContextRefreshedEvent>, Ordered {

	private final DataBase postgres;

	public PostgresSchemaInitializer(@Qualifier("postgresDemo") final DataBase postgres) {
		this.postgres = postgres;
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

	@Override
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		this.postgres.create();

		try (Connection connection = this.postgres.createConnection(); Statement stmt = connection.createStatement()) {
			stmt.executeUpdate("CREATE SCHEMA IF NOT EXISTS " + this.quote(DemoTableOptions.POSTGRES_SCHEMA));
			stmt.executeUpdate("SET search_path TO " + this.quote(DemoTableOptions.POSTGRES_SCHEMA) + ", public");
		} catch (final SQLException e) {
			throw new DBException("Could not create PostgreSQL schema for the sample.", e);
		}
	}

	private String quote(final String identifier) {
		return "\"" + identifier.replace("\"", "\"\"") + "\"";
	}
}
