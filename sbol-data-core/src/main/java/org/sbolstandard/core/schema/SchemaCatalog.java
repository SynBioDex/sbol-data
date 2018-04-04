package org.sbolstandard.core.schema;

import java.net.URI;
import java.util.List;

public interface SchemaCatalog
{
	URI getIdentifier();
	List<URI> getImportedSchemas();
	List<IdentifiableDocumentSchema> getSchemas();
}
