package org.sbolstandard.core.schema;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;

import org.sbolstandard.core.schema.PropertyValueSchema.*;


public class Schema {
	
	public static interface Extends {
		public List<URI> getExtends();
	}
	
	public static Extends Extends(final URI ... schemas) {
		return new Extends() {

			@Override
			public List<URI> getExtends() {
				return Arrays.asList(schemas);
			}
			
		};
	}
	
	public static interface IdentifierSchemas {
		public List<IdentifierSchema> getIdentifierSchemas();
	}
	
	public static IdentifierSchemas IdentifierSchemas(final IdentifierSchema ... schemas) {
		return new IdentifierSchemas() {

			@Override
			public List<IdentifierSchema> getIdentifierSchemas() {
				return Arrays.asList(schemas);
			}
			
		};
	}
	
	public static interface TypeSchemas {
		public List<TypeSchema> getTypeSchemas();
	}
	
	
	public static TypeSchemas TypeSchemas(final TypeSchema ... schemas) {
		return new TypeSchemas() {

			@Override
			public List<TypeSchema> getTypeSchemas() {
				return Arrays.asList(schemas);
			}
			
		};
	}
	
	public static interface PropertySchemas {
		public List<PropertySchema> getPropertySchemas();
	}
	
	public static PropertySchemas PropertySchemas(final PropertySchema ... schemas) {
		return new PropertySchemas() {

			@Override
			public List<PropertySchema> getPropertySchemas() {
				return Arrays.asList(schemas);
			}
			
		};
	}
	
	public static SchemaCatalog SchemaCatalog(final URI identifier, 
			final ImportedSchemas importedSchemas, 
			final DocumentSchemas schemas)
	{
		return new SchemaCatalog()
		{
			
			@Override
			public List<IdentifiableDocumentSchema> getSchemas()
			{				
				return schemas.getDocumentSchemas();
			}
			
			@Override
			public List<URI> getImportedSchemas()
			{
				return importedSchemas.getSchemas();
			}
			
			@Override
			public URI getIdentifier()
			{
				return identifier;
			}
		};		
	}
	
	public static interface ImportedSchemas
	{
		List<URI> getSchemas();
	}
	
	public static ImportedSchemas ImportedSchemas(final URI...schemas)
	{
		return new ImportedSchemas()
		{
			
			@Override
			public List<URI> getSchemas()
			{
				return Arrays.asList(schemas);
			}
		};
	}
	
	public static interface DocumentSchemas
	{
		List<IdentifiableDocumentSchema> getDocumentSchemas();
	}
	
	public static DocumentSchemas DocumentSchemas(final IdentifiableDocumentSchema...documentSchemas)
	{
		return new DocumentSchemas()
		{

			@Override
			public List<IdentifiableDocumentSchema> getDocumentSchemas()
			{
				return Arrays.asList(documentSchemas);
			}
		};
	}
	
	public static IdentifiableDocumentSchema DocumentSchema(
			final URI identifier,
			final Extends ext,
			final IdentifierSchemas iss,
			final TypeSchemas tss,
			final PropertySchemas pss
			)
	{
		return new IdentifiableDocumentSchema() {

			@Override
			public List<URI> getExtends() {
				return ext.getExtends();
			}

			@Override
			public List<TypeSchema> getTypeSchemas() {
				return tss.getTypeSchemas();
			}

			@Override
			public List<PropertySchema> getPropertySchemas() {
				return pss.getPropertySchemas();
			}

			@Override
			public List<IdentifierSchema> getIdentifierSchemas() {
				return iss.getIdentifierSchemas();
			}

			@Override
			public URI getIdentifier()
			{
				return identifier;
			}
			
		};
	}
	
	public static interface Cardinalities {
		public List<Cardinality> getCardinalities();
	}
	
	public static Cardinalities Cardinalities(final Cardinality ... cardinalities) {
		return new Cardinalities() {

			@Override
			public List<Cardinality> getCardinalities() {
				return Arrays.asList(cardinalities);
			}
			
		};
	}
	
	public static interface PropertyValueSchemas {
		public List<PropertyValueSchema> getValueSchemas();
	}
	
	public static PropertyValueSchemas PropertyValueSchemas(final PropertyValueSchema ... valueSchemas) {
		return new PropertyValueSchemas() {

			@Override
			public List<PropertyValueSchema> getValueSchemas() {
				return Arrays.asList(valueSchemas);
			}
			
		};
	}
	
	
	public static NamedPropertySchema PropertySchema (
			final TypeSchemas types,
			final Cardinalities cardinalities,
			final PropertyValueSchemas valueSchemas)
	{
		return new NamedPropertySchema() {

			@Override
			public List<org.sbolstandard.core.schema.TypeSchema> getTypeSchemas() {
				return types.getTypeSchemas();
			}

			@Override
			public List<Cardinality> getCardinalities() {
				return cardinalities.getCardinalities();
			}

			@Override
			public List<PropertyValueSchema> getValueSchemas() {
				return valueSchemas.getValueSchemas();
			}
			
		};
	}
	
	public static TypeSchema TypeSchema(QName exactType)
	{
		return new TypeSchema.ExactType(exactType);
	}
	
	public static Cardinality Cardinality(final int b, final Ordering ord) {
		return new Cardinality() {

			@Override
			public int getBounds() {
				return b;
			}

			@Override
			public Ordering getOrdering() {
				return ord;
			}
			
		};
	}
	
	public static final class cardinality
	{
		public static final Cardinality gteq_0 = Cardinality(0, Ordering.GREATER_THAN_OR_EQUAL);
		public static final Cardinality lteq_1 = Cardinality(1, Ordering.LESS_THAN_OR_EQUAL);
		public static final Cardinality gteq_1 = Cardinality(1, Ordering.GREATER_THAN_OR_EQUAL);
		
		public static final Cardinalities optional=Cardinalities(gteq_0, lteq_1);
		public static final Cardinalities required=Cardinalities(gteq_1, lteq_1);
		public static final Cardinalities many = Cardinalities();
		public static final Cardinalities atLeastOne = Cardinalities(gteq_1);
	}
	
	
	public static final class propertyType
	{
		public static final PropertyValueSchema XsdValue(String xsdType) { return new PropertyValueSchema.XsdValue(xsdType); }
		public static final PropertyValueSchema string = XsdValue("xsd:string");
		public static final PropertyValueSchema integer = XsdValue("xsd:integer");
		public static final PropertyValueSchema oneOf(String ... values) {
			//TODO: Replace with the xsd:oneOf type
			return string;
		}
		
		
	}
	
	public static final MultiPropertySchema MultiPropertySchema (
			TypeSchemas firstProperty,
			TypeSchemas secondProperty, 
			Ordering ordering)
	{
		return new MultiPropertySchema.OrderedPair(firstProperty.getTypeSchemas(), secondProperty.getTypeSchemas(), ordering);
	}
	
	public static final ReferenceValue ReferenceValue(final URI documentType)
	{
		return new ReferenceValue(documentType);
	}
	
	public static final DocumentValue DocumentValue(final IdentifiableDocumentSchema documentType)
	{
		return new DocumentValue(documentType);
	}
}
