package lu.kbra.pclib.db.sample.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lu.kbra.pclib.db.annotations.entry.Column;
import lu.kbra.pclib.db.impl.DatabaseEntry.ReadOnlyDatabaseEntry;

@Data
@NoArgsConstructor
public class PurchaseSummaryData implements ReadOnlyDatabaseEntry {

	@Column(name = "customer_id")
	public long customerId;

	@Column(name = "customer_email")
	public String customerEmail;

	@Column(name = "customer_name")
	public String customerName;

	@Column(name = "purchase_id")
	public long purchaseId;

	@Column(name = "amount")
	public Money amount;

}
