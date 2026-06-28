package lu.kbra.pclib.db.sample.domain;

public record Money(long cents) {

	public static Money ofEuroCents(final long cents) {
		return new Money(cents);
	}

	@Override
	public String toString() {
		return String.format("€%d.%02d", this.cents / 100, Math.abs(this.cents % 100));
	}
}
