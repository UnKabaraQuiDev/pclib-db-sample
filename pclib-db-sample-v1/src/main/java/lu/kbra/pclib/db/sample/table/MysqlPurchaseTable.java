package lu.kbra.pclib.db.sample.table;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lu.kbra.pclib.db.annotations.queryable.def.NameOverride;
import lu.kbra.pclib.db.base.DeferredDatabase;
import lu.kbra.pclib.db.sample.domain.PurchaseData;
import lu.kbra.pclib.db.table.DeferredDatabaseTable;

@Component
@NameOverride("purchases")
public abstract class MysqlPurchaseTable extends DeferredDatabaseTable<PurchaseData> implements PurchaseTableAccess {

	public MysqlPurchaseTable(@Qualifier("mysqlDemo") final DeferredDatabase dataBase) {
		super(dataBase);
	}
}
