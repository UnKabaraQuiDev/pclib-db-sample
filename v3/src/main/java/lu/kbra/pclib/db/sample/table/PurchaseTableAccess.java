package lu.kbra.pclib.db.sample.table;

import java.util.List;

import lu.kbra.pclib.db.annotations.query.Query;
import lu.kbra.pclib.db.sample.domain.PurchaseData;
import lu.kbra.pclib.db.table.AbstractDBTable;

public interface PurchaseTableAccess extends AbstractDBTable<PurchaseData> {

	@Query("SELECT * FROM {NAME} WHERE {Q:customer_id} = ? ORDER BY {Q:id} ASC;")
	List<PurchaseData> byCustomer(long customerId);

	@Query("SELECT {F:sum}({Q:amount}) FROM {NAME} WHERE {Q:customer_id} = ?;")
	long totalCentsByCustomer(long customerId);
}
