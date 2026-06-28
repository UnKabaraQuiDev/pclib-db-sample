package lu.kbra.pclib.db.sample.domain;

import lu.kbra.pclib.db.annotations.entry.Column;
import lu.kbra.pclib.db.impl.DataBaseEntry;

public class PurchaseSummaryData implements DataBaseEntry.ReadOnlyDataBaseEntry {

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

	public PurchaseSummaryData() {
	}

	@Override
	public PurchaseSummaryData clone() {
		try {
			return (PurchaseSummaryData) super.clone();
		} catch (final CloneNotSupportedException e) {
			throw new AssertionError(e);
		}
	}

	@Override
	public String toString() {
		return "PurchaseSummaryData[customerEmail=" + this.customerEmail + ", purchaseId=" + this.purchaseId
				+ ", amount=" + this.amount + "]";
	}
}
