package uk.ac.ncl.intbio.core.schema;

import java.net.URI;
import java.util.List;

public interface IdentifiableDocumentSchema {
  public URI getIdentifier();
  public List<URI> getExtends();
  public List<TypeSchema> getTypeSchemas();
  public List<PropertySchema> getPropertySchemas();
  public List<IdentifierSchema> getIdentifierSchemas();
  
  
}
