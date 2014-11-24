package sbol2;


import static uk.ac.ncl.intbio.core.datatree.Datatree.*;

import java.io.OutputStreamWriter;
import java.net.URI;

import javax.xml.namespace.QName;

import uk.ac.ncl.intbio.core.datatree.DocumentRoot;
import uk.ac.ncl.intbio.core.datatree.NamespaceBinding;
import uk.ac.ncl.intbio.core.datatree.TopLevelDocument;
import uk.ac.ncl.intbio.core.io.rdf.RdfTerms;
import uk.ac.ncl.intbio.examples.Sbol2Terms;
import uk.ac.ncl.intbio.examples.SbolTerms;
import uk.ac.ncl.intbio.examples.WriteRdfFromDatatree;

public class AnnotationExample2
{
	  private static final NamespaceBinding sbolExample = NamespaceBinding("http://sbolstandard.org/annotationExample1#", "example");
	  private static final NamespaceBinding grn = NamespaceBinding("http://urn:bbn.com:tasbe:grn/", "grn");
	  
	  

	public static DocumentRoot<QName>  getSBOLDocument()
	{
		            
	    return DocumentRoot(NamespaceBindings(RdfTerms.rdf, SbolTerms.sbol2, grn),
	    		TopLevelDocuments(	    				
	    				   TopLevelDocument(
					            Sbol2Terms.component.componentDefinition,
					            sbolExample.namespacedUri("componentDefinition/part_1"),
					            NamedProperties(
					                    NamedProperty(Sbol2Terms.documented.name, "Promoter 1"),
					                    NamedProperty(Sbol2Terms.documented.displayId, "part_1"),
					                    NamedProperty(RdfTerms.rdfType, URI.create("DNA")),
					                    NamedProperty(Sbol2Terms.component.role, URI.create("http://purl.org/obo/owl/SO#SO_0000167")),
					                    NamedProperty(grn.withLocalPart("design"),  
					                    		NestedDocument(
					                    				Sbol2Terms.identified.identified, 
					                    				sbolExample.namespacedUri("componentDefinition/part_1/design"), 
					                    				NamedProperties(
					                    						NamedProperty(grn.withLocalPart("logicalType"), "boolean"),
					                    						NamedProperty(grn.withLocalPart("logicalValue"), "false"),
					                    						NamedProperty(RdfTerms.rdfType, grn.namespacedUri("DataType"))					                    						
					                    						)
					                    				)),
					                    NamedProperty(grn.withLocalPart("regulation"),  
									       		NestedDocument(
									                    Sbol2Terms.identified.identified, 
									                    				sbolExample.namespacedUri("componentDefinition/part_1/regulation"), 
									                    				NamedProperties(
									                    						NamedProperty(RdfTerms.rdfType, grn.namespacedUri("Regulation")),
									                    						NamedProperty(grn.withLocalPart("repression"), "false"),									                    						
									                    						NamedProperty(grn.withLocalPart("species"), 
									                    								NestedDocument(Sbol2Terms.identified.identified, 
									                    										sbolExample.namespacedUri("componentDefinition/part_1/regulation/species1"),
									                    										NamedProperties(
									                    												NamedProperty(RdfTerms.rdfType, grn.namespacedUri("ChemicalSpecies")),
									                    												NamedProperty(grn.withLocalPart("uid"), "Echo"),
									                    												NamedProperty(grn.withLocalPart("design"), 
									                    														NestedDocument(Sbol2Terms.identified.identified, 
									                    																sbolExample.namespacedUri("componentDefinition/part_1/regulation/species1/datatype"),
									                    																 NamedProperties(
									                    																		 NamedProperty(RdfTerms.rdfType, grn.namespacedUri("DataType")),
									                    																		 NamedProperty(grn.withLocalPart("logicalType"), "boolean"))
									                    																 )
									                    														
									                    														),
									                    														NamedProperty(grn.withLocalPart("property"), 
											                    														NestedDocument(Sbol2Terms.identified.identified, 
											                    																sbolExample.namespacedUri("componentDefinition/part_1/regulation/species1/family"),
											                    																 NamedProperties(
											                    																		 NamedProperty(RdfTerms.rdfType, grn.namespacedUri("Family")),
											                    																		 NamedProperty(grn.withLocalPart("name"), "Echo"))
											                    																 )
											                    														
											                    														)	
									                    												)
									                    										
									                    										)
									                    								
									                    								
									                    								)					                    						
									                    						)
									                    				))				
					                    		
					            ))					           
	    		
	    				)
	    				
	    				
	    		
	    		);
	    
	    /*        NamespaceBindings(RdfTerms.rdf, SbolTerms.sbol2, dcmi, bqbiol), TopLevelDocuments()
	            
	    		);*/
	}
	
	public static void main(String[] args) throws Exception
	{
		DocumentRoot<QName> document=getSBOLDocument();
		WriteRdfFromDatatree.write(new OutputStreamWriter(System.out), document);
		
	}
}

