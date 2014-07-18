package uk.ac.ncl.intbio.core.schema;

import java.util.List;

public interface IdentifiableDocumentSchema {
  public List<IdentifiableDocumentSchema> getExtends();
  public List<TypeSchema> getTypeSchemas();
  public List<PropertySchema> getPropertySchemas();
  public List<IdentifierSchema> getIdentifierSchemas();
  
  
}
