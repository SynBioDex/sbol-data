package uk.ac.ncl.intbio.core.schema;

import java.net.URI;
import java.util.List;

public interface SchemaCatalog
{
	URI getIdentifier();
	List<URI> getImportedSchemas();
	List<IdentifiableDocumentSchema> getSchemas();
}
