package uk.ac.ncl.intbio.core.schema;

import java.util.List;

public interface NamedPropertySchema extends PropertySchema {
	public List<TypeSchema> getTypeSchemas(); 
	public List<Cardinality> getCardinalities();
	public List<PropertyValueSchema> getValueSchemas();
}
