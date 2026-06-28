package lu.kbra.pclib.db.sample.runner;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lu.kbra.pclib.db.base.DataBase;
import lu.kbra.pclib.db.sample.table.MysqlCustomerTable;
import lu.kbra.pclib.db.sample.table.MysqlPurchaseTable;
import lu.kbra.pclib.db.sample.table.PostgresCustomerTable;
import lu.kbra.pclib.db.sample.table.PostgresPurchaseTable;
import lu.kbra.pclib.db.sample.table.SqliteCustomerTable;
import lu.kbra.pclib.db.sample.table.SqlitePurchaseTable;
import lu.kbra.pclib.db.sample.view.MysqlPurchaseSummaryView;
import lu.kbra.pclib.db.sample.view.PostgresPurchaseSummaryView;
import lu.kbra.pclib.db.sample.view.SqlitePurchaseSummaryView;
import lu.kbra.pclib.db.table.AbstractDBTable;
import lu.kbra.pclib.db.view.AbstractDBView;

@Component
public class SampleRunner implements CommandLineRunner {

	private record DemoDatabase(String label, DataBase database, AbstractDBView<?> summaryView,
			AbstractDBTable<?> purchases, AbstractDBTable<?> customers) {
	}

	private final List<DemoDatabase> databases;

	public SampleRunner(final SqliteCustomerTable sqliteCustomers, final SqlitePurchaseTable sqlitePurchases,
			final SqlitePurchaseSummaryView sqliteSummary, final MysqlCustomerTable mysqlCustomers,
			final MysqlPurchaseTable mysqlPurchases, final MysqlPurchaseSummaryView mysqlSummary,
			final PostgresCustomerTable postgresCustomers, final PostgresPurchaseTable postgresPurchases,
			final PostgresPurchaseSummaryView postgresSummary) {
		this.databases = List.of(
				new DemoDatabase("sqlite", sqliteCustomers.getDatabase(), sqliteSummary, sqlitePurchases,
						sqliteCustomers),
				new DemoDatabase("mysql", mysqlCustomers.getDatabase(), mysqlSummary, mysqlPurchases, mysqlCustomers),
				new DemoDatabase("postgresql", postgresCustomers.getDatabase(), postgresSummary, postgresPurchases,
						postgresCustomers));
	}

	@Override
	public void run(final String... args) {
		System.out.println("\nPCLib DB Spring sample cleanup starting.\n");

		for (final DemoDatabase demo : this.databases) {
			this.cleanupOneDatabase(demo);
		}

		System.out.println("\nPCLib DB Spring sample cleanup finished.\n");
	}

	private void cleanupOneDatabase(final DemoDatabase demo) {
		System.out.println("--- cleanup / " + demo.label() + " ---");
		this.dropQuietly("view " + demo.summaryView().getQualifiedName(), demo.summaryView()::drop);
		this.dropQuietly("table " + demo.purchases().getQualifiedName(), demo.purchases()::drop);
		this.dropQuietly("table " + demo.customers().getQualifiedName(), demo.customers()::drop);
		this.dropQuietly("database " + demo.database().getDataBaseName(), demo.database()::drop);
		System.out.println();
	}

	private void dropQuietly(final String label, final Runnable action) {
		try {
			action.run();
			System.out.println("dropped " + label);
		} catch (final RuntimeException e) {
			System.out.println("skip " + label + " (already missing or not reachable): " + e.getMessage());
		}
	}
}
