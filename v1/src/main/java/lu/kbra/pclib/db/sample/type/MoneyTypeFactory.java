package lu.kbra.pclib.db.sample.type;

import java.lang.reflect.Type;
import java.util.List;

import org.springframework.stereotype.Component;

import lu.kbra.pclib.db.domain.column.type.ColumnType;
import lu.kbra.pclib.db.domain.column.type.EncodingType;
import lu.kbra.pclib.db.sample.domain.Money;
import lu.kbra.pclib.db.type.factory.DatabaseColumnTypeFactory;
import lu.kbra.pclib.db.utils.registry.ColumnTypeFactory;
import lu.kbra.pclib.db.utils.registry.ColumnTypeRegistry;

@Component
public class MoneyTypeFactory implements DatabaseColumnTypeFactory {

	public static final class MoneyType implements ColumnType<Money, Long> {

		private final EncodingType<Long> encodingType;

		public MoneyType(EncodingType<Long> encodingType) {
			this.encodingType = encodingType;
		}

		@Override
		public Money decode(final Long value, final Type type) {
			return Money.ofEuroCents(((Number) value).longValue());
		}

		@Override
		public Long encode(final Money value) {
			return value.cents();
		}

		@Override
		public Class<Money> getJavaType() {
			return Money.class;
		}

		@Override
		public Class<Long> getJdbcType() {
			return Long.class;
		}

		@Override
		public EncodingType<Long> getEncodingType() {
			return encodingType;
		}

	}

	@Override
	public boolean matches(final String protocol) {
		return true;
	}

	@Override
	public void registerColumnTypes(List<ColumnTypeFactory<?>> typeMap) {
		ColumnTypeRegistry.registerType(MoneyType.class,
				(clazz, hints, etp) -> clazz == Money.class ? ColumnTypeRegistry.PERFECT_MATCH_SCORE : ColumnTypeRegistry.EXCLUDE,
				(annotatedType, hints, etp) -> new MoneyType(etp.getTypeFor(Long.class)),
				typeMap);
	}

}
