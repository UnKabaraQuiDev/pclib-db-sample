package lu.kbra.pclib.db.sample.domain;

import java.time.Instant;

import lu.kbra.pclib.db.annotations.entry.AutoIncrement;
import lu.kbra.pclib.db.annotations.entry.Column;
import lu.kbra.pclib.db.annotations.entry.DefaultValue;
import lu.kbra.pclib.db.annotations.entry.Nullable;
import lu.kbra.pclib.db.annotations.entry.PrimaryKey;
import lu.kbra.pclib.db.annotations.entry.TypeHint;
import lu.kbra.pclib.db.annotations.entry.Unique;
import lu.kbra.pclib.db.annotations.entry.def.MaxLength;
import lu.kbra.pclib.db.domain.column.meta.DefaultTypeHints;
import lu.kbra.pclib.db.impl.DataBaseEntry;

public class CustomerData implements DataBaseEntry {

	@Column
	@PrimaryKey
	@AutoIncrement
	public long id;

	@Column
	@Unique(1)
	@Unique(2)
	public @MaxLength(32) String tenant;

	@Column
	@Unique(1)
	public @TypeHint(type = DefaultTypeHints.MAX_LENGTH, value = "160") String email;

	@Column
	@Unique(2)
	public @TypeHint(dbms = "mysql", type = DefaultTypeHints.MAX_LENGTH, value = "64") @TypeHint(dbms = "postgresql", type = DefaultTypeHints.MAX_LENGTH, value = "96") @TypeHint(dbms = "sqlite", type = DefaultTypeHints.MAX_LENGTH, value = "128") String externalReference;

	@Column
	public @MaxLength(80) String name;

	@Column
	@DefaultValue(dbms = "mysql", value = "'mysql-default'")
	@DefaultValue(dbms = "postgresql", value = "'postgres-default'")
	@DefaultValue(dbms = "sqlite", value = "'sqlite-default'")
	public @MaxLength(40) String dbmsDefaultLabel;

	@Column
	@DefaultValue(dbms = "mysql", value = "CURRENT_TIMESTAMP")
	@DefaultValue(dbms = "postgresql", value = "CURRENT_TIMESTAMP")
	@DefaultValue(dbms = "sqlite", value = "0")
	public Instant createdAt;

	@Column
	@DefaultValue(dbms = "mysql", value = "'standard-mysql'")
	@DefaultValue(dbms = "postgresql", value = "'standard-postgres'")
	@DefaultValue(dbms = "sqlite", value = "'standard-sqlite'")
	public @MaxLength(32) String loyaltyTier;

	@Column
	@Nullable
	public @MaxLength(255) String note;

	public CustomerData() {
	}

	public CustomerData(final String tenant, final String email, final String externalReference, final String name) {
		this.tenant = tenant;
		this.email = email;
		this.externalReference = externalReference;
		this.name = name;
	}

	@Override
	public CustomerData clone() {
		try {
			return (CustomerData) super.clone();
		} catch (final CloneNotSupportedException e) {
			throw new AssertionError(e);
		}
	}

	@Override
	public String toString() {
		return "CustomerData[id=" + this.id + ", tenant=" + this.tenant + ", email=" + this.email + ", name="
				+ this.name + ", dbmsDefaultLabel=" + this.dbmsDefaultLabel + ", loyaltyTier=" + this.loyaltyTier + "]";
	}
}
