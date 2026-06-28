package lu.kbra.pclib.db.sample.table;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lu.kbra.pclib.db.base.DataBase;
import lu.kbra.pclib.db.sample.config.DemoTableOptions;
import lu.kbra.pclib.db.sample.domain.PurchaseData;
import lu.kbra.pclib.db.table.DeferredDataBaseTable;

@Component
@DemoTableOptions(name = "purchases")
public abstract class PostgresPurchaseTable extends DeferredDataBaseTable<PurchaseData> implements PurchaseTableAccess {

	public PostgresPurchaseTable(@Qualifier("postgresDemo") final DataBase dataBase) {
		super(dataBase);
	}
}
