package lu.kbra.pclib.db.sample.type;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.stereotype.Component;

import lu.kbra.pclib.db.domain.column.type.ColumnType;
import lu.kbra.pclib.db.domain.column.type.ColumnType.FixedColumnType;
import lu.kbra.pclib.db.sample.domain.Money;
import lu.kbra.pclib.db.type.factory.DataBaseTypeFactory;
import lu.kbra.pclib.db.utils.registry.ColumnTypeFactory;
import lu.kbra.pclib.db.utils.registry.ColumnTypeRegistry;

@Component
public class MoneyTypeFactory implements DataBaseTypeFactory {

	public static final class MoneyType implements FixedColumnType {

		@Override
		public Object decode(final Object value, final Type type) {
			if (type == Money.class) {
				return Money.ofEuroCents(((Number) value).longValue());
			}
			return ColumnType.unsupported(type);
		}

		@Override
		public Object encode(final Object value) {
			if (value instanceof final Money money) {
				return money.cents();
			}
			return ColumnType.unsupported(value);
		}

		@Override
		public Object getObject(final ResultSet rs, final int columnIndex) throws SQLException {
			return rs.getLong(columnIndex);
		}

		@Override
		public Object getObject(final ResultSet rs, final String columnName) throws SQLException {
			return rs.getLong(columnName);
		}

		@Override
		public int getSQLType() {
			return Types.BIGINT;
		}

		@Override
		public String getTypeName() {
			return "BIGINT";
		}

		@Override
		public void setObject(final PreparedStatement stmt, final int index, final Object value) throws SQLException {
			stmt.setLong(index, ((Number) value).longValue());
		}
	}

	@Override
	public boolean matches(final String protocol) {
		return true;
	}

	@Override
	public void registerTypes(final List<ColumnTypeFactory> typeMap) {
		ColumnTypeRegistry.registerType(MoneyType.class, (clazz,
				hints) -> clazz == Money.class ? ColumnTypeRegistry.PERFECT_MATCH_SCORE : ColumnTypeRegistry.EXCLUDE,
				(annotatedType, hints) -> new MoneyType(), typeMap);
	}
}
