package org.sbolstandard.core.schema;

import java.util.List;

public interface NamedPropertySchema extends PropertySchema {
	public List<TypeSchema> getTypeSchemas(); 
	public List<Cardinality> getCardinalities();
	public List<PropertyValueSchema> getValueSchemas();
}
