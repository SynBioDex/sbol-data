package uk.ac.ncl.intbio.examples;


import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javanet.staxutils.IndentingXMLStreamWriter;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import uk.ac.ncl.intbio.core.datatree.Datatree;
import uk.ac.ncl.intbio.core.datatree.DocumentRoot;
import uk.ac.ncl.intbio.core.datatree.NamedProperty;
import uk.ac.ncl.intbio.core.datatree.NestedDocument;
import uk.ac.ncl.intbio.core.datatree.PropertyValue;
import uk.ac.ncl.intbio.core.datatree.TopLevelDocument;
import uk.ac.ncl.intbio.core.io.rdf.RdfIo;
import uk.ac.ncl.intbio.core.io.rdf.RdfTerms;
import uk.ac.ncl.intbio.core.schema.Cardinality;
import uk.ac.ncl.intbio.core.schema.IdentifiableDocumentSchema;
import uk.ac.ncl.intbio.core.schema.MultiPropertySchema;
import uk.ac.ncl.intbio.core.schema.NamedPropertySchema;
import uk.ac.ncl.intbio.core.schema.Ordering;
import uk.ac.ncl.intbio.core.schema.PropertySchema;
import uk.ac.ncl.intbio.core.schema.PropertyValueSchema;
import uk.ac.ncl.intbio.core.schema.PropertyValueSchema.DocumentValue;
import uk.ac.ncl.intbio.core.schema.PropertyValueSchema.ReferenceValue;
import uk.ac.ncl.intbio.core.schema.SchemaCatalog;
import uk.ac.ncl.intbio.core.schema.PropertyValueSchema.XsdValue;
import uk.ac.ncl.intbio.core.schema.Schema.PropertyValueSchemas;
import uk.ac.ncl.intbio.core.schema.Schema.cardinality;
import uk.ac.ncl.intbio.core.schema.Schema.propertyType;
import uk.ac.ncl.intbio.core.schema.TypeSchema.ExactType;
import uk.ac.ncl.intbio.core.schema.TypeSchema;
import uk.ac.ncl.intbio.core.schema.TypeSchema.HasLocalName;
import uk.ac.ncl.intbio.core.schema.TypeSchema.HasPrefix;
import static uk.ac.ncl.intbio.core.schema.Schema.*;
import static uk.ac.ncl.intbio.core.datatree.Datatree.*;

public class SchemaExample {
	  private static String dctermsNS = "http://purl.org/dc/terms/";
	  private static String dctermsPF = "dcterms";

	public static void main (String[] args) throws Exception
	{
		
		DocumentRoot originalDocument = makeDocument(makeCoreSchemaCatalog());
		//write(new OutputStreamWriter(System.out), originalDocument);
		originalDocument = makeDocument(makeComponentSchemaCatalog());
		//write(new OutputStreamWriter(System.out), originalDocument);
		originalDocument = makeDocument(makeInstantiationSchemaCatalog());
		write(new OutputStreamWriter(System.out), originalDocument);
		
		 
		//  String test="";
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
	
	private static void write(Writer stream, DocumentRoot<QName> document) throws Exception
	{
		XMLStreamWriter xmlWriter = new IndentingXMLStreamWriter(XMLOutputFactory.newInstance().createXMLStreamWriter(stream));
		RdfIo rdfIo = new RdfIo();
		rdfIo.createIoWriter(xmlWriter).write(document);
		xmlWriter.flush();
		xmlWriter.close();
	}
	
	private static TopLevelDocument<QName>[] getArray( List<TopLevelDocument<QName>> documents)
	{
		 TopLevelDocument<QName>[] topLevelDocuments=(TopLevelDocument<QName>[]) Array.newInstance(TopLevelDocument.class, documents.size());
	     for (int i=0;i<documents.size();i++)
	     {
	    	 topLevelDocuments[i]=documents.get(i);
	     }
	     return topLevelDocuments;
	}
	
	private static NamedProperty<QName,PropertyValue>[] getPropertyArray( List<NamedProperty<QName,PropertyValue>> properties)
	{
		NamedProperty<QName,PropertyValue>[] propertyArray=(NamedProperty<QName,PropertyValue>[]) Array.newInstance(NamedProperty.class, properties.size());
	     for (int i=0;i<properties.size();i++)
	     {
	    	 propertyArray[i]=properties.get(i);
	     }
	     return propertyArray;
	}
	
	public static DocumentRoot<QName> makeDocument(final SchemaCatalog ... schemaCatalogs) {
		List<TopLevelDocument<QName>> schemaCatalogDocuments=new ArrayList<TopLevelDocument<QName>>();
		for (SchemaCatalog catalog:schemaCatalogs)
		{
			schemaCatalogDocuments.add(getSchemaCatalogDocument(catalog));
		}
		
		return DocumentRoot(
				NamespaceBindings(SbolTerms.sbol2),
				TopLevelDocuments(getArray(schemaCatalogDocuments)),
				Datatree.LiteralProperties(NamedLiteralProperty(QName(dctermsNS, "creator", dctermsPF), "Goksel Misirli"))
				);
	}
	
	public static List<NamedProperty<QName, PropertyValue>> merge(List<NamedProperty<QName, PropertyValue>> ... lists){
        List<NamedProperty<QName, PropertyValue>> mergeList= new ArrayList<NamedProperty<QName, PropertyValue>>();
        for (List<NamedProperty<QName, PropertyValue>> list: lists)
        {
        	for( NamedProperty<QName, PropertyValue> e:list){
        		mergeList.add(e);
            }
        } 
        return mergeList;
    }
	
	public static TopLevelDocument<QName> getSchemaCatalogDocument(SchemaCatalog catalog)
	{
		return TopLevelDocument(
				NamespaceBindings(SbolTerms.sbol2),
				Sbol2SchemaTerms.catalog, 
				catalog.getIdentifier(), 
				NamedProperties(
						merge(
								getNamedURIProperties(catalog.getImportedSchemas(),Sbol2SchemaTerms.importedSchema),
								getDocumentSchemas(catalog.getSchemas())
						)
					)
			);
	}
	
	public static List<NamedProperty<QName,PropertyValue>> getDocumentSchemas(List<IdentifiableDocumentSchema> schemas)
	{
		List<NestedDocument<QName>> schemaDocuments=new ArrayList<NestedDocument<QName>>();
		for (IdentifiableDocumentSchema schema:schemas)
		{
			schemaDocuments.add(getDocumentSchema(schema));
		}
		List<NamedProperty<QName,PropertyValue>> properties=new ArrayList<NamedProperty<QName,PropertyValue>>();
		properties.add(NamedProperty(Sbol2SchemaTerms.hasSchema,NestedDocuments(schemaDocuments)));
		return properties;
	}
	
	public static NestedDocument<QName> getDocumentSchema(IdentifiableDocumentSchema schema)
	{
		return NestedDocument(
				NamespaceBindings(SbolTerms.sbol2),
				Sbol2SchemaTerms.schema, 
				schema.getIdentifier(), 
				NamedProperties(
						merge(
							getNamedURIProperties(schema.getExtends(), Sbol2SchemaTerms.hasExtend),
							getTypeSchemaProperties(schema.getTypeSchemas()),
							getPropertySchemas(schema.getPropertySchemas())
						)
						
				)
		);		
	}
	
	
	public static List<NamedProperty<QName,PropertyValue>>  getPropertySchemas(List<PropertySchema> propertySchemas)
	{
		List<NestedDocument<QName>> propertySchemaDocuments=new ArrayList<NestedDocument<QName>>();
		for (PropertySchema schema:propertySchemas)
		{
			propertySchemaDocuments.add(getPropertySchemaDocument(schema));
		}
		List<NamedProperty<QName,PropertyValue>> properties=new ArrayList<NamedProperty<QName,PropertyValue>>();
		if (propertySchemaDocuments.size()>0)
		{
			properties.add(NamedProperty(Sbol2SchemaTerms.hasSchema,NestedDocuments(propertySchemaDocuments)));
		}
		return properties;
	}
	
	private static NestedDocument<QName> getPropertySchemaDocument(PropertySchema schema)
	{
		return NestedDocument(
				NamespaceBindings(SbolTerms.sbol2),
				Sbol2SchemaTerms.propertySchema, 
				URI.create(schema.toString()),
				NamedProperties(
						merge(
								getTypeSchemaProperties(((NamedPropertySchema)schema).getTypeSchemas()),
								getCardinalities(((NamedPropertySchema)schema).getCardinalities()),
								getPropertyValueSchemaProperties(((NamedPropertySchema)schema).getValueSchemas())
								
						)
				)
			);
	}
	
	public static List<NamedProperty<QName,PropertyValue>> getPropertyValueSchemaProperties(List<PropertyValueSchema> valueSchemas)
	{
		List<NamedProperty<QName,PropertyValue>> properties=new ArrayList<NamedProperty<QName,PropertyValue>>();
		for (PropertyValueSchema valueSchema:valueSchemas)
		{
			if (valueSchema instanceof XsdValue)
			{
				String value =((XsdValue) valueSchema).getXsdType();
				properties.add(NamedProperty(Sbol2SchemaTerms.xsdType,value));				
			}
			else if (valueSchema instanceof DocumentValue){
				IdentifiableDocumentSchema value =((DocumentValue) valueSchema).getDocumentType();
				properties.add(NamedProperty(Sbol2SchemaTerms.documentValue,NestedDocuments(getDocumentSchema(value))));				
			}
			else if (valueSchema instanceof ReferenceValue){
				URI value =((ReferenceValue) valueSchema).getDocumentType();
				properties.add(NamedProperty(Sbol2SchemaTerms.referenceValue,value));						
			}
			
		}
		return properties;
	}
	
	public static List<NamedProperty<QName,PropertyValue>>  getCardinalities(List<Cardinality> cardinalities)
	{
		List<NestedDocument<QName>> documents=new ArrayList<NestedDocument<QName>>();
		for (Cardinality cardinality:cardinalities)
		{
			documents.add(getCardinalityDocument(cardinality));
		}
		List<NamedProperty<QName,PropertyValue>> properties=new ArrayList<NamedProperty<QName,PropertyValue>>();
		if (documents.size()>0)
		{
			properties.add(NamedProperty(Sbol2SchemaTerms.hasCardinality,NestedDocuments(documents)));
		}
		return properties;
	}
	
	private static NestedDocument<QName> getCardinalityDocument(Cardinality cardinality)
	{
		return NestedDocument(
				NamespaceBindings(SbolTerms.sbol2),
				Sbol2SchemaTerms.cardinality, 
				URI.create(cardinality.toString()),
				NamedProperties(
						NamedProperty(Sbol2SchemaTerms.bounds,cardinality.getBounds()),
						NamedProperty(Sbol2SchemaTerms.ordering,cardinality.getOrdering().toString())
				)
			);
	}
	
	public static List<NamedProperty<QName,PropertyValue>> getNamedURIProperties(List<URI> importedSchemas,QName propertyName)
	{
		List<NamedProperty<QName,PropertyValue>> schemas=new ArrayList<NamedProperty<QName,PropertyValue>>();
		for (URI schema:importedSchemas)
		{
			schemas.add(NamedProperty(propertyName, schema));
		}
		return schemas;
	}
	private static URI getURI(QName qname)
	{
		return URI.create(qname.getNamespaceURI() + qname.getLocalPart());
	}
	public static List<NamedProperty<QName,PropertyValue>> getTypeSchemaProperties(List<TypeSchema> typeSchemas)
	{
		List<NamedProperty<QName,PropertyValue>> properties=new ArrayList<NamedProperty<QName,PropertyValue>>();
		for (TypeSchema schema:typeSchemas)
		{
			if (schema instanceof ExactType)
			{
				QName qname=((ExactType) schema).getType();
				properties.add(NamedProperty(Sbol2SchemaTerms.exactType,getURI(qname)));				
			}
			else if (schema instanceof HasPrefix){
				properties.add(NamedProperty(Sbol2SchemaTerms.hasPrefix,((HasPrefix) schema).getPrefix()));				
			}
			else if (schema instanceof HasLocalName){
				properties.add(NamedProperty(Sbol2SchemaTerms.hasLocalName,((HasLocalName) schema).getLocalName()));						
			}
		}
		return properties;
	}
	
	
	public static SchemaCatalog makeCoreSchemaCatalog()
	{
		return SchemaCatalog(
				Sbol2Terms.sbol2.namespacedUri("/schemaexample/core"),
				ImportedSchemas(),
				DocumentSchemas(
						DocumentSchema(
								Sbol2Terms.sbol2.namespacedUri("/schema/identified"),
								Extends(),
								IdentifierSchemas(),
								TypeSchemas(),
								PropertySchemas()
						),
						DocumentSchema(
								Sbol2Terms.sbol2.namespacedUri("/schema/documented"),
								Extends(Sbol2Terms.sbol2.namespacedUri("/schema/identified")),
								IdentifierSchemas(),
								TypeSchemas(),
								PropertySchemas(
										PropertySchema(
												TypeSchemas(
														TypeSchema(Sbol2Terms.documented.displayId)
												), 
												cardinality.required, 
												PropertyValueSchemas(propertyType.string)
										),
										PropertySchema(
												TypeSchemas(
														TypeSchema(Sbol2Terms.documented.name)
												), 
												cardinality.optional, 
												PropertyValueSchemas(propertyType.string)
										),
										PropertySchema(
												TypeSchemas(
														TypeSchema(Sbol2Terms.documented.description)
												), 
												cardinality.optional, 
												PropertyValueSchemas(propertyType.string)
										)
								)
						)
				)
			);
	}
	

	public static SchemaCatalog makeInstantiationSchemaCatalog()
	{
		return SchemaCatalog(
				Sbol2Terms.sbol2.namespacedUri("/schemaexample/instantiation"),
				ImportedSchemas(),
				DocumentSchemas(
						DocumentSchema(
								Sbol2Terms.sbol2.namespacedUri("/schema/component_instantiation"),
								Extends(),
								IdentifierSchemas(),
								TypeSchemas(
										TypeSchema(Sbol2Terms.instantiation.componentInstantiation)
										),
								PropertySchemas(
										PropertySchema(
												TypeSchemas(
														TypeSchema(Sbol2Terms.instantiation.hasComponentInstantiation)
												), 
												cardinality.required, 
												PropertyValueSchemas(ReferenceValue(Sbol2Terms.sbol2.namespacedUri("/schema/sequence_component")))
										)
								)
						)
					)
			);
	}

	
	
	public static SchemaCatalog makeComponentSchemaCatalog()
	{
		return SchemaCatalog(
				Sbol2Terms.sbol2.namespacedUri("/schemaexample/component"),
				ImportedSchemas(
						Sbol2Terms.sbol2.namespacedUri("/schema/core"),
						Sbol2Terms.sbol2.namespacedUri("/schema/instantiation")
				),
				DocumentSchemas(
						DocumentSchema(
								Sbol2Terms.sbol2.namespacedUri("/schema/sequence"),
								Extends(),
								IdentifierSchemas(),
								TypeSchemas(
										TypeSchema(Sbol2Terms.component.sequence)
									),
								PropertySchemas(
										PropertySchema(
												TypeSchemas(
														TypeSchema(Sbol2Terms.component.elements)
												), 
												cardinality.required, 
												PropertyValueSchemas(propertyType.string)
										)
								)
						),
						DocumentSchema(
								Sbol2Terms.sbol2.namespacedUri("/schema/sequence_component"),
								Extends(Sbol2Terms.sbol2.namespacedUri("/schema/documented")), 
									IdentifierSchemas(),
									TypeSchemas(
											TypeSchema(Sbol2Terms.component.sequenceComponent)
											),
									PropertySchemas(
											PropertySchema(
													TypeSchemas(
															TypeSchema(Sbol2Terms.component.hasSequence)
															),
													cardinality.optional,
													PropertyValueSchemas(ReferenceValue(Sbol2Terms.sbol2.namespacedUri("/schema/sequence")))	
											),
											PropertySchema(
													TypeSchemas(
															TypeSchema(Sbol2Terms.component.annotation)
															),
													cardinality.many,
													PropertyValueSchemas(
															DocumentValue(
																DocumentSchema(
																		Sbol2Terms.sbol2.namespacedUri("/schema/sequence_annotation"),
																		Extends(Sbol2Terms.sbol2.namespacedUri("/schema/documented")), 
																		IdentifierSchemas(),
																		TypeSchemas(
																			TypeSchema(Sbol2Terms.component.sequenceComponent)
																		),
																		PropertySchemas(
																				PropertySchema(
																						TypeSchemas(
																								TypeSchema(Sbol2Terms.instantiation.subComponentInstantiation)
																								),
																						cardinality.required,
																						PropertyValueSchemas(ReferenceValue(Sbol2Terms.sbol2.namespacedUri("/schema/component_instantiation")))	
																				),
																				PropertySchema(
																						TypeSchemas(
																								TypeSchema(Sbol2Terms.component.orientation)
																								),
																						cardinality.required,
																						PropertyValueSchemas(propertyType.oneOf("inline","reverse_compliment"))	
																				),
																				PropertySchema(
																						TypeSchemas(
																								TypeSchema(Sbol2Terms.component.start)
																								),
																						cardinality.optional,
																						PropertyValueSchemas(propertyType.integer)	
																				),
																				PropertySchema(
																						TypeSchemas(
																								TypeSchema(Sbol2Terms.component.end)
																								),
																						cardinality.optional,
																						PropertyValueSchemas(propertyType.integer)	
																				)
																		)			
																)
															)
													)	
											)
											
										
									)
										
								)
					)
			);
	}

}
