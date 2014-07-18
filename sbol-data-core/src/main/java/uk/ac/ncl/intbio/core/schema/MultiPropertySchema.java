package uk.ac.ncl.intbio.core.schema;

import java.util.List;

public interface MultiPropertySchema extends PropertySchema  {
	public static final class OrderedPair implements MultiPropertySchema
	{
		public final List<TypeSchema> firstProperty;
		public final List<TypeSchema> secondProperty;
		public final Ordering ordering;
		
		public OrderedPair(List<TypeSchema> firstProperty,
				List<TypeSchema> secondProperty, Ordering ordering) {
			super();
			this.firstProperty = firstProperty;
			this.secondProperty = secondProperty;
			this.ordering = ordering;
		}

		public List<TypeSchema> getFirstProperty() {
			return firstProperty;
		}
		
		public List<TypeSchema> getSecondProperty() {
			return secondProperty;
		}
		
		public Ordering getOrdering() {
			return ordering;
		}
		
	}
}
