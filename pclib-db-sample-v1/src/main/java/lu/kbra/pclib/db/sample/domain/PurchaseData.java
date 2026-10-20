package lu.kbra.pclib.db.sample.domain;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;
import lu.kbra.pclib.db.annotations.entry.AutoIncrement;
import lu.kbra.pclib.db.annotations.entry.Column;
import lu.kbra.pclib.db.annotations.entry.DefaultValue;
import lu.kbra.pclib.db.annotations.entry.ForeignKey;
import lu.kbra.pclib.db.annotations.entry.PrimaryKey;
import lu.kbra.pclib.db.annotations.entry.def.MaxLength;
import lu.kbra.pclib.db.domain.table.ForeignKeyData.OnAction;
import lu.kbra.pclib.db.impl.DatabaseEntry;
import lu.kbra.pclib.db.sample.table.CustomerTableAccess;

@Data
@NoArgsConstructor
public class PurchaseData implements DatabaseEntry {

	@Column
	@PrimaryKey
	@AutoIncrement
	public long id;

	@Column
	@ForeignKey(table = CustomerTableAccess.class, name = "customers", onDelete = OnAction.CASCADE, onUpdate = OnAction.CASCADE)
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

	public PurchaseData(final long customerId, final Money amount, final LocalDate orderedOn) {
		this.customerId = customerId;
		this.amount = amount;
		this.orderedOn = orderedOn;
	}

}
