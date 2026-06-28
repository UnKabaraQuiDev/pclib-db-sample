package lu.kbra.pclib.db.sample.table;

import java.util.List;
import java.util.Optional;

import lu.kbra.pclib.db.annotations.query.Param;
import lu.kbra.pclib.db.annotations.query.Query;
import lu.kbra.pclib.db.sample.domain.CustomerData;
import lu.kbra.pclib.db.table.AbstractDBTable;

public interface CustomerTableAccess extends AbstractDBTable<CustomerData> {

	@Query("SELECT * FROM {NAME} WHERE {Q:email} = ?;")
	Optional<CustomerData> byEmail(String email);

	@Query("SELECT * FROM {NAME} WHERE {Q:tenant} = ? AND {Q:external_reference} = ?;")
	Optional<CustomerData> byTenantAndReference(String tenant, String externalReference);

	@Query("SELECT {F:text_length}({Q:name}) FROM {NAME} WHERE {Q:email} = ?;")
	int nameLengthByEmail(String email);

	@Query("SELECT * FROM {NAME} WHERE {Q:name} LIKE ? ORDER BY {Q:id} ASC;")
	List<CustomerData> byNameLikeExplicitSql(String pattern);

	@Query(columns = { "tenant" })
	List<CustomerData> byTenant(String tenant);

	@Query
	List<CustomerData> byNameLikeGenerated(@Param(value = "name", comparator = "LIKE") String pattern);
}
