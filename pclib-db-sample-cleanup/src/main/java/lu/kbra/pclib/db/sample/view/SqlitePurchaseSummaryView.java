package lu.kbra.pclib.db.sample.view;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lu.kbra.pclib.db.annotations.view.DB_View;
import lu.kbra.pclib.db.annotations.view.ViewColumn;
import lu.kbra.pclib.db.annotations.view.ViewTable;
import lu.kbra.pclib.db.base.DataBase;
import lu.kbra.pclib.db.sample.config.DemoTableOptions;
import lu.kbra.pclib.db.sample.domain.PurchaseSummaryData;
import lu.kbra.pclib.db.sample.table.SqliteCustomerTable;
import lu.kbra.pclib.db.sample.table.SqlitePurchaseTable;
import lu.kbra.pclib.db.view.DeferredDataBaseView;

@Component
@DemoTableOptions(name = "purchase_summary")
@DB_View(name = "purchase_summary", tables = {
		@ViewTable(typeName = SqliteCustomerTable.class, asName = "c", columns = {
				@ViewColumn(name = "id", asName = "customer_id"),
				@ViewColumn(name = "email", asName = "customer_email"),
				@ViewColumn(name = "name", asName = "customer_name") }),
		@ViewTable(typeName = SqlitePurchaseTable.class, join = ViewTable.Type.INNER, asName = "p", on = "c.id = p.customer_id", columns = {
				@ViewColumn(name = "id", asName = "purchase_id"), @ViewColumn(name = "amount", asName = "amount") }) })
public abstract class SqlitePurchaseSummaryView extends DeferredDataBaseView<PurchaseSummaryData>
		implements PurchaseSummaryViewAccess {

	public SqlitePurchaseSummaryView(@Qualifier("sqliteDemo") final DataBase dataBase) {
		super(dataBase);
	}
}
