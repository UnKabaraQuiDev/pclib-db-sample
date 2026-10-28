package lu.kbra.pclib.db.sample.runner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lu.kbra.pclib.db.sample.domain.CustomerData;
import lu.kbra.pclib.db.sample.domain.Money;
import lu.kbra.pclib.db.sample.domain.PurchaseData;
import lu.kbra.pclib.db.sample.domain.PurchaseSummaryData;
import lu.kbra.pclib.db.sample.table.CustomerTableAccess;
import lu.kbra.pclib.db.sample.table.MysqlCustomerTable;
import lu.kbra.pclib.db.sample.table.MysqlPurchaseTable;
import lu.kbra.pclib.db.sample.table.PostgresCustomerTable;
import lu.kbra.pclib.db.sample.table.PostgresPurchaseTable;
import lu.kbra.pclib.db.sample.table.PurchaseTableAccess;
import lu.kbra.pclib.db.sample.table.SqliteCustomerTable;
import lu.kbra.pclib.db.sample.table.SqlitePurchaseTable;
import lu.kbra.pclib.db.sample.view.MysqlPurchaseSummaryView;
import lu.kbra.pclib.db.sample.view.PostgresPurchaseSummaryView;
import lu.kbra.pclib.db.sample.view.PurchaseSummaryViewAccess;
import lu.kbra.pclib.db.sample.view.SqlitePurchaseSummaryView;

@Component
public class SampleRunner implements CommandLineRunner {

	private final List<DemoDatabase> databases;

	public SampleRunner(
			final SqliteCustomerTable sqliteCustomers,
			final SqlitePurchaseTable sqlitePurchases,
			final SqlitePurchaseSummaryView sqliteSummary,
			final MysqlCustomerTable mysqlCustomers,
			final MysqlPurchaseTable mysqlPurchases,
			final MysqlPurchaseSummaryView mysqlSummary,
			final PostgresCustomerTable postgresCustomers,
			final PostgresPurchaseTable postgresPurchases,
			final PostgresPurchaseSummaryView postgresSummary) {
		this.databases = List.of(new DemoDatabase("sqlite", sqliteCustomers, sqlitePurchases, sqliteSummary),
				new DemoDatabase("mysql", mysqlCustomers, mysqlPurchases, mysqlSummary),
				new DemoDatabase("postgresql", postgresCustomers, postgresPurchases, postgresSummary));
	}

	private record DemoDatabase(
			String label,
			CustomerTableAccess customers,
			PurchaseTableAccess purchases,
			PurchaseSummaryViewAccess summaryView) {
	}

	@Override
	public void run(final String... args) {
		System.out.println("\\nPCLib DB Spring sample v3 starting. Schema initialization/migration comes from application.yml.\\n");

		for (final DemoDatabase demo : this.databases) {
			this.runOneDatabase(demo);
		}

		System.out.println("\\nPCLib DB Spring sample v3 finished.\\n");
	}

	private void runOneDatabase(final DemoDatabase demo) {
		System.out.println("--- v3 / " + demo.label() + " ---");

		// One customer is added by each version; existing rows must survive migration.
		final int existingCustomers = demo.customers().all().size();
		if (existingCustomers != 2) {
			throw new IllegalStateException("Expected 2 customer(s) from earlier versions in "
					+ demo.label() + " before v3, but found " + existingCustomers
					+ ". Run ./run.sh against a fresh Compose volume.");
		}

		final CustomerData inputCustomer = new CustomerData(
				"v3", "v3-" + demo.label() + "@example.test",
				"demo-v3-" + demo.label(), "Alice v3 " + demo.label());
		inputCustomer.customerNote = "Created in v3";
		final CustomerData customer = demo.customers().insertAndReload(inputCustomer);
		System.err.println("Inserted: " + customer);
		System.err.println(demo.customers().all());
		final PurchaseData firstPurchase = new PurchaseData(customer.id, Money.ofEuroCents(1299), LocalDate.now());
		firstPurchase.fulfillmentStatus = "PROCESSING";
		firstPurchase.trackingReference = "track-v3-" + demo.label();
		firstPurchase.shippedOn = LocalDate.now().plusDays(2);
		demo.purchases().insertAndReload(firstPurchase);
		demo.purchases().insertAndReload(new PurchaseData(customer.id, Money.ofEuroCents(2499), LocalDate.now().plusDays(1)));

		final Optional<CustomerData> byEmail = demo.customers().byEmail(customer.email);
		final Optional<CustomerData> byCompositeUnique =
				demo.customers().byTenantAndReference("v3", "demo-v3-" + demo.label());
		final int nameLength = demo.customers().nameLengthByEmail(customer.email);
		final List<CustomerData> generatedQuery = demo.customers().byNameLikeGenerated("Alice%");
		final List<CustomerData> explicitQuery = demo.customers().byNameLikeExplicitSql("Alice%");
		final List<PurchaseData> purchases = demo.purchases().byCustomer(customer.id);
		final long total = demo.purchases().totalCentsByCustomer(customer.id);
		final List<PurchaseSummaryData> viewRows = demo.summaryView().allRows();

		System.out.println("qualified customer table: " + demo.customers().getQualifiedName());
		System.out
				.println("create SQL for customers:\\n" + Arrays.stream(demo.customers().getCreateSQL()).collect(Collectors.joining("\n")));
		System.out.println("inserted customer: " + customer);
		System.out.println("byEmail: " + byEmail);
		System.out.println("by composite repeated @Unique: " + byCompositeUnique);
		System.out.println("{F:length} result: " + nameLength);
		System.out.println("generated @Query with @Param comparator rows: " + generatedQuery.size());
		System.out.println("explicit @Query with {NAME}/{Q:...} rows: " + explicitQuery.size());
		System.out.println("purchases: " + purchases);
		System.out.println("{F:sum} total cents: " + total);
		System.out.println("view rows: " + viewRows);
		System.out.println();
	}

}
