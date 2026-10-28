package lu.kbra.pclib.db.sample.domain;

import java.time.Instant;

import lombok.Data;
import lombok.NoArgsConstructor;
import lu.kbra.pclib.db.annotations.entry.AutoIncrement;
import lu.kbra.pclib.db.annotations.entry.Column;
import lu.kbra.pclib.db.annotations.entry.DefaultValue;
import lu.kbra.pclib.db.annotations.entry.Nullable;
import lu.kbra.pclib.db.annotations.entry.PrimaryKey;
import lu.kbra.pclib.db.annotations.entry.TypeHint;
import lu.kbra.pclib.db.annotations.entry.Unique;
import lu.kbra.pclib.db.annotations.entry.def.MaxLength;
import lu.kbra.pclib.db.dbms.MySQLDbmsProvider;
import lu.kbra.pclib.db.domain.column.meta.DefaultTypeHints;
import lu.kbra.pclib.db.impl.DatabaseEntry;

@Data
@NoArgsConstructor
public class CustomerData implements DatabaseEntry {

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
	public @TypeHint(dbms = "mysql", type = DefaultTypeHints.MAX_LENGTH, value = "64") @TypeHint(
			dbms = "postgresql",
			type = DefaultTypeHints.MAX_LENGTH,
			value = "96"
	) @TypeHint(dbms = "sqlite", type = DefaultTypeHints.MAX_LENGTH, value = "128") String externalReference;

	@Column
	public @MaxLength(80) String name;

	@Column
	@DefaultValue(dbms = MySQLDbmsProvider.DBMS_QUALIFIER_NAME, value = "'mysql-default'")
	@DefaultValue(dbms = "postgresql", value = "'postgres-default'")
	@DefaultValue(dbms = "sqlite", value = "'sqlite-default'")
	public @MaxLength(40) String dbmsDefaultLabel;

	@Column
	@DefaultValue(dbms = "mysql", value = "CURRENT_TIMESTAMP")
	@DefaultValue(dbms = "postgresql", value = "CURRENT_TIMESTAMP")
	@DefaultValue(dbms = "sqlite", value = "{F:current_timestamp}")
	public Instant createdAt;

	@Column(name = "customer_note")
	@Nullable
	public @MaxLength(255) String customerNote;

	@Column(name = "phone_number")
	@Nullable
	public @MaxLength(32) String phoneNumber;

	public CustomerData(final String tenant, final String email, final String externalReference, final String name) {
		this.tenant = tenant;
		this.email = email;
		this.externalReference = externalReference;
		this.name = name;
	}

}
