package lu.kbra.pclib.db.sample.domain;

import java.time.LocalDate;

import lu.kbra.pclib.db.annotations.entry.AutoIncrement;
import lu.kbra.pclib.db.annotations.entry.Column;
import lu.kbra.pclib.db.annotations.entry.DefaultValue;
import lu.kbra.pclib.db.annotations.entry.PrimaryKey;
import lu.kbra.pclib.db.annotations.entry.def.MaxLength;
import lu.kbra.pclib.db.impl.DataBaseEntry;

public class PurchaseData implements DataBaseEntry {

	@Column
	@PrimaryKey
	@AutoIncrement
	public long id;

	@Column
	public long customerId;

	@Column
	public Money amount;

	@Column
	public LocalDate orderedOn;

	@Column
	@DefaultValue(dbms = "mysql", value = "'NEW'")
	@DefaultValue(dbms = "postgresql", value = "'new'")
	@DefaultValue(dbms = "sqlite", value = "'new-sqlite'")
	public @MaxLength(24) String status;

	public PurchaseData() {
	}

	public PurchaseData(final long customerId, final Money amount, final LocalDate orderedOn) {
		this.customerId = customerId;
		this.amount = amount;
		this.orderedOn = orderedOn;
	}

	@Override
	public PurchaseData clone() {
		try {
			return (PurchaseData) super.clone();
		} catch (final CloneNotSupportedException e) {
			throw new AssertionError(e);
		}
	}

	@Override
	public String toString() {
		return "PurchaseData[id=" + this.id + ", customerId=" + this.customerId + ", amount=" + this.amount
				+ ", status=" + this.status + "]";
	}
}
