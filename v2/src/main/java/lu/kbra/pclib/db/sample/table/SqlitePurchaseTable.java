package lu.kbra.pclib.db.sample.table;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lu.kbra.pclib.db.annotations.queryable.def.NameOverride;
import lu.kbra.pclib.db.annotations.queryable.def.TableId;
import lu.kbra.pclib.db.base.DeferredDatabase;
import lu.kbra.pclib.db.sample.domain.PurchaseData;
import lu.kbra.pclib.db.table.DeferredDatabaseTable;

@Component
@NameOverride("purchases")
@TableId("purchase")
public abstract class SqlitePurchaseTable extends DeferredDatabaseTable<PurchaseData> implements PurchaseTableAccess {

	public SqlitePurchaseTable(@Qualifier("sqliteDemo") final DeferredDatabase dataBase) {
		super(dataBase);
	}
}
