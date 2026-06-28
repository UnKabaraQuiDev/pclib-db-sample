package lu.kbra.pclib.db.sample.config;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lu.kbra.pclib.db.connector.impl.DataBaseConnectorFactory;
import lu.kbra.pclib.db.dbms.DbmsProvider;
import lu.kbra.pclib.db.dbms.MySQLDbmsProvider;
import lu.kbra.pclib.db.dbms.PostgreSQLDbmsProvider;
import lu.kbra.pclib.db.dbms.SQLiteDbmsProvider;
import lu.kbra.pclib.db.domain.dialect.AbstractSQLFunctionResolver;
import lu.kbra.pclib.db.domain.dialect.SQLFunctionResolver;
import lu.kbra.pclib.db.domain.dialect.SQLStructureVisitor;
import lu.kbra.pclib.db.utils.registry.ColumnTypeRegistry;

@Configuration
public class DemoFunctionConfiguration {

	@Bean
	DbmsProvider mysqlFunctionProvider() {
		return new FunctionProvider(new MySQLDbmsProvider(), "LENGTH");
	}

	@Bean
	DbmsProvider postgresFunctionProvider() {
		return new FunctionProvider(new PostgreSQLDbmsProvider(), "char_length");
	}

	@Bean
	DbmsProvider sqliteFunctionProvider() {
		return new FunctionProvider(new SQLiteDbmsProvider(), "length");
	}

	private static final class FunctionProvider implements DbmsProvider {

		private final DbmsProvider delegate;
		private final String textLengthFunctionName;

		private FunctionProvider(final DbmsProvider delegate, final String textLengthFunctionName) {
			this.delegate = delegate;
			this.textLengthFunctionName = textLengthFunctionName;
		}

		@Override
		public ColumnTypeRegistry createColumnTypeRegistry() {
			return this.delegate.createColumnTypeRegistry();
		}

		@Override
		public DataBaseConnectorFactory createConnectorFactory(final Map<String, Object> properties) {
			return this.delegate.createConnectorFactory(properties);
		}

		@Override
		public SQLFunctionResolver createFunctionResolver() {
			final AbstractSQLFunctionResolver resolver = new AbstractSQLFunctionResolver();
			resolver.putAll(this.delegate.createFunctionResolver());
			resolver.put("text_length", this.textLengthFunctionName);
			return resolver;
		}

		@Override
		public SQLStructureVisitor createStructureVisitor() {
			return this.delegate.createStructureVisitor();
		}

		@Override
		public int getPriority() {
			return 100;
		}

		@Override
		public String getProtocol() {
			return this.delegate.getProtocol();
		}
	}
}
