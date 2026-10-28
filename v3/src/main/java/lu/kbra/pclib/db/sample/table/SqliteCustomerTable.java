package lu.kbra.pclib.db.sample.table;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lu.kbra.pclib.db.annotations.queryable.def.NameOverride;
import lu.kbra.pclib.db.annotations.queryable.def.TableId;
import lu.kbra.pclib.db.base.DeferredDatabase;
import lu.kbra.pclib.db.sample.domain.CustomerData;
import lu.kbra.pclib.db.table.DeferredDatabaseTable;

@Component
@NameOverride("customers")
@TableId("customers")
public abstract class SqliteCustomerTable extends DeferredDatabaseTable<CustomerData> implements CustomerTableAccess {

	public SqliteCustomerTable(@Qualifier("sqliteDemo") final DeferredDatabase dataBase) {
		super(dataBase);
	}

}
