package org.sbolstandard.examples;


import org.sbolstandard.core.schema.IdentifiableDocumentSchema;
import org.sbolstandard.core.schema.MultiPropertySchema;
import org.sbolstandard.core.schema.Ordering;
import static org.sbolstandard.core.schema.Schema.*;

/**
 * The examples for the schemas are still in progress!
 */
public class SchemaExample {

	public static void main (String[] args)
	{
		/*IdentifiableDocumentSchema documentedSchema = DocumentSchema(
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
		
		*/
	}
}
