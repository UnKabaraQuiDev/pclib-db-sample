package lu.kbra.pclib.db.sample.view;

import java.util.List;

import lu.kbra.pclib.db.annotations.query.Query;
import lu.kbra.pclib.db.sample.domain.PurchaseSummaryData;
import lu.kbra.pclib.db.view.AbstractDBView;

public interface PurchaseSummaryViewAccess extends AbstractDBView<PurchaseSummaryData> {

	@Query("SELECT * FROM {NAME} ORDER BY {Q:purchase_id} ASC;")
	List<PurchaseSummaryData> allRows();
}
