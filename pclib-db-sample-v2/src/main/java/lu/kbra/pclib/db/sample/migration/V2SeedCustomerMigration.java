package lu.kbra.pclib.db.sample.migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

import lu.kbra.pclib.db.base.DataBase;
import lu.kbra.pclib.db.exception.DBException;
import lu.kbra.pclib.db.migration.DataBaseMigration;
import lu.kbra.pclib.db.sample.config.DemoTableOptions;

@Component
public class V2SeedCustomerMigration implements DataBaseMigration {

	@Override
	public String name() {
		return "v2_seed_customer";
	}

	@Override
	public int order() {
		return 2;
	}

	@Override
	public void up(final DataBase dataBase, final Connection connection) throws DBException {
		final String protocol = dataBase.getConnector().getProtocol();
		final String sql = "INSERT INTO " + this.customersTable(dataBase) + " (" + this.quote(dataBase, "tenant") + ", "
				+ this.quote(dataBase, "email") + ", " + this.quote(dataBase, "external_reference") + ", "
				+ this.quote(dataBase, "name") + ", " + this.quote(dataBase, "note") + ") VALUES (?, ?, ?, ?, ?)";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, "migration-v2");
			stmt.setString(2, "seed-v2@" + protocol + ".example");
			stmt.setString(3, "seed-v2-" + protocol);
			stmt.setString(4, "Migration Seed V2 " + protocol);
			stmt.setString(5, "Inserted by the v2 DataBaseMigration");
			stmt.executeUpdate();
		} catch (final SQLException e) {
			throw new DBException("Could not run v2 seed migration for " + protocol + ".", e);
		}
	}

	private String customersTable(final DataBase dataBase) {
		if ("postgresql".equals(dataBase.getConnector().getProtocol())) {
			return this.quote(dataBase, DemoTableOptions.POSTGRES_SCHEMA) + "." + this.quote(dataBase, "customers");
		}
		return this.quote(dataBase, "customers");
	}

	private String quote(final DataBase dataBase, final String identifier) {
		final String escaped = identifier.replace("\"", "\"\"").replace("`", "``");
		return "mysql".equals(dataBase.getConnector().getProtocol()) ? "`" + escaped + "`" : "\"" + escaped + "\"";
	}
}
