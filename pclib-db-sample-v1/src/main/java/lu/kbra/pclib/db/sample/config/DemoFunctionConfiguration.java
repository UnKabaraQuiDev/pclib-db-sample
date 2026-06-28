package lu.kbra.pclib.db.sample.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import lu.kbra.pclib.db.domain.dialect.AbstractSQLFunctionResolver;
import lu.kbra.pclib.db.utils.impl.DataBaseEntryUtils;

@Configuration
public class DemoFunctionConfiguration {

	DataBaseEntryUtils mySQLDbmsProvider;
	DataBaseEntryUtils postgreSQLDbmsProvider;
	DataBaseEntryUtils sqLiteDbmsProvider;

	public DemoFunctionConfiguration(@Qualifier("mysqlDemoDataBaseEntryUtils") DataBaseEntryUtils mySQLDbmsProvider,
			@Qualifier("postgresDemoDataBaseEntryUtils") DataBaseEntryUtils postgreSQLDbmsProvider,
			@Qualifier("sqliteDemoDataBaseEntryUtils") DataBaseEntryUtils sqLiteDbmsProvider) {
		this.mySQLDbmsProvider = mySQLDbmsProvider;
		this.postgreSQLDbmsProvider = postgreSQLDbmsProvider;
		this.sqLiteDbmsProvider = sqLiteDbmsProvider;
	}

	@PostConstruct
	public void init() {
		((AbstractSQLFunctionResolver) mySQLDbmsProvider.getFunctionResolver()).put("text_length", "LENGTH");
		((AbstractSQLFunctionResolver) postgreSQLDbmsProvider.getFunctionResolver()).put("text_length", "char_length");
		((AbstractSQLFunctionResolver) sqLiteDbmsProvider.getFunctionResolver()).put("text_length", "length");
	}

}
