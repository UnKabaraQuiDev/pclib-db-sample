package lu.kbra.pclib.db.sample.view;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lu.kbra.pclib.db.annotations.view.SelectColumn;
import lu.kbra.pclib.db.annotations.view.Table;
import lu.kbra.pclib.db.annotations.view.View;
import lu.kbra.pclib.db.base.DeferredDatabase;
import lu.kbra.pclib.db.sample.domain.PurchaseSummaryData;
import lu.kbra.pclib.db.sample.table.PostgresCustomerTable;
import lu.kbra.pclib.db.sample.table.PostgresPurchaseTable;
import lu.kbra.pclib.db.view.DeferredDatabaseView;

@Component
@View(name = "purchase_summary", tables = {
		@Table(typeName = PostgresCustomerTable.class, asName = "c", columns = {
				@SelectColumn(name = "id", asName = "customer_id"),
				@SelectColumn(name = "email", asName = "customer_email"),
				@SelectColumn(name = "name", asName = "customer_name") }),
		@Table(typeName = PostgresPurchaseTable.class, join = Table.Type.INNER, asName = "p", on = "c.id = p.customer_id", columns = {
				@SelectColumn(name = "id", asName = "purchase_id"), @SelectColumn(name = "amount", asName = "amount") }) })
public abstract class PostgresPurchaseSummaryView extends DeferredDatabaseView<PurchaseSummaryData>
		implements PurchaseSummaryViewAccess {

	public PostgresPurchaseSummaryView(@Qualifier("postgresDemo") final DeferredDatabase dataBase) {
		super(dataBase);
	}
}
