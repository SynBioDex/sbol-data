package uk.ac.ncl.intbio.examples;

import uk.ac.ncl.intbio.core.schema.IdentifiableDocumentSchema;
import uk.ac.ncl.intbio.core.schema.MultiPropertySchema;
import uk.ac.ncl.intbio.core.schema.Ordering;
import static uk.ac.ncl.intbio.core.schema.Schema.*;

public class SchemaExample {

	public static void main (String[] args)
	{
		IdentifiableDocumentSchema documentedSchema = DocumentSchema(
				Extends(),
				IdentifierSchemas(),
				TypeSchemas(),
				PropertySchemas(
						PropertySchema(
								TypeSchemas(TypeSchema(Sbol2Terms.documented.name)),
								cardinality.optional, 
								PropertyValueSchemas(propertyType.string)),
						PropertySchema(
								TypeSchemas(TypeSchema(Sbol2Terms.documented.displayId)),
								cardinality.required, PropertyValueSchemas(propertyType.string)),
						PropertySchema(
								TypeSchemas(TypeSchema(Sbol2Terms.documented.description)),
								cardinality.optional, PropertyValueSchemas(propertyType.string)))
				);

		IdentifiableDocumentSchema annotationSchema = DocumentSchema(
				Extends(documentedSchema),
				IdentifierSchemas(),
				TypeSchemas(
						TypeSchema(Sbol2Terms.component.annotation)
						),
				PropertySchemas(
						PropertySchema(
								TypeSchemas(TypeSchema(Sbol2Terms.component.start)),
								cardinality.optional, PropertyValueSchemas(propertyType.integer)),
						PropertySchema(
								TypeSchemas(TypeSchema(Sbol2Terms.component.end)),
								cardinality.optional, PropertyValueSchemas(propertyType.integer)),
						PropertySchema(
								TypeSchemas(TypeSchema(Sbol2Terms.component.orientation)),
								cardinality.optional, PropertyValueSchemas(propertyType.oneOf("POSITIVE","NEGATIVE"))),
						MultiPropertySchema(
								TypeSchemas(TypeSchema(Sbol2Terms.component.start)),
								TypeSchemas(TypeSchema(Sbol2Terms.component.end)),
								Ordering.LESS_THAN_OR_EQUAL)	
							)
						
								
						
			
						
				);
		
		
	}
}
