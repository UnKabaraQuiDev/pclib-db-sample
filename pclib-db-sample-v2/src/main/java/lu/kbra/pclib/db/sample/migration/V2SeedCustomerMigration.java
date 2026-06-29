package lu.kbra.pclib.db.sample.migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

import lu.kbra.pclib.db.base.DataBase;
import lu.kbra.pclib.db.domain.dialect.SQLStructureVisitor;
import lu.kbra.pclib.db.exception.DBException;
import lu.kbra.pclib.db.migration.DataBaseMigration;
import lu.kbra.pclib.db.sample.table.CustomerTableAccess;
import lu.kbra.pclib.db.utils.impl.DataBaseEntryUtils;

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
	public void up(final DataBase database, final Connection connection) throws DBException {
		final CustomerTableAccess table = database.getTables().stream().filter(CustomerTableAccess.class::isInstance)
				.map(CustomerTableAccess.class::cast).findFirst().get();

		final DataBaseEntryUtils dbEntryUtils = database.getDataBaseEntryUtils();
		final SQLStructureVisitor structureVisitor = dbEntryUtils.getStructureVisitor();
		final String protocol = database.getConnector().getProtocol();
		final String sql = "UPDATE " + table.getQualifiedName() + " SET "
				+ structureVisitor.qualifiedName(structureVisitor.fieldToColumnName("loyaltyTier")) + " = ?;";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, "32 chars :)");
			stmt.executeUpdate();
		} catch (final SQLException e) {
			throw new DBException("Could not run v2 seed migration for " + protocol + ".", e);
		}
	}

}
