package lu.kbra.pclib.db.sample.table;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lu.kbra.pclib.db.base.DataBase;
import lu.kbra.pclib.db.sample.config.DemoTableOptions;
import lu.kbra.pclib.db.sample.domain.CustomerData;
import lu.kbra.pclib.db.table.DeferredDataBaseTable;

@Component
@DemoTableOptions(name = "customers")
public abstract class SqliteCustomerTable extends DeferredDataBaseTable<CustomerData> implements CustomerTableAccess {

	public SqliteCustomerTable(@Qualifier("sqliteDemo") final DataBase dataBase) {
		super(dataBase);
	}

}
