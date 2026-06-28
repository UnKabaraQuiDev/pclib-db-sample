package lu.kbra.pclib.db.sample.table;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lu.kbra.pclib.db.base.DataBase;
import lu.kbra.pclib.db.sample.config.DemoTableOptions;
import lu.kbra.pclib.db.sample.domain.CustomerData;
import lu.kbra.pclib.db.table.DeferredDataBaseTable;

@Component
@DemoTableOptions(name = "customers")
public abstract class MysqlCustomerTable extends DeferredDataBaseTable<CustomerData> implements CustomerTableAccess {

	public MysqlCustomerTable(@Qualifier("mysqlDemo") final DataBase dataBase) {
		super(dataBase);
	}
}
