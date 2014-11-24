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

public class TopLevelAnnotation
{
	  private static final NamespaceBinding dcmi = NamespaceBinding("http://dublincore.org/documents/2012/06/14/dcmi-terms/", "dcmi");
	  private static final NamespaceBinding bqmodel = NamespaceBinding("http://biomodels.net/model-qualifiers/", "bqmodel");
	  private static final NamespaceBinding bqbiol = NamespaceBinding("http://biomodels.net/biology-qualifiers/", "bqbiol");	  
	  private static final NamespaceBinding sbolExample = NamespaceBinding("http://sbolstandard.org/annotationExample1#", "example");
	  private static final NamespaceBinding visualiser = NamespaceBinding("http://www.sometool.org/", "visualiser");
	  private static final NamespaceBinding partsRegistry = NamespaceBinding("http://partsregistry.org/", "pr");

	public static DocumentRoot<QName>  getSBOLDocument()
	{
		            
	    return DocumentRoot(NamespaceBindings(RdfTerms.rdf, SbolTerms.sbol2, dcmi, bqmodel),
	    		TopLevelDocuments(
	    				  TopLevelDocument(
	    				            Sbol2Terms.documented.documented,
	    				            //sbolExample.namespacedUri(""),
	    				            URI.create(""),
	    				            NamedProperties(
	    				                    NamedProperty(new QName(dcmi.getNamespaceURI(), "createdBy", dcmi.getPrefix()), "Goksel Misirli"),
	    				                    NamedProperty(new QName(bqmodel.getNamespaceURI(), "isDescribedBy", bqmodel.getPrefix()), URI.create("http://identifiers.org/pubmed/xxxxxx"))
	    				            )),
	    				
	    				   TopLevelDocument(
					            Sbol2Terms.component.componentDefinition,
					            sbolExample.namespacedUri("sequenceComponent/pLac"),
					            NamedProperties(
					                    NamedProperty(Sbol2Terms.documented.name, "pLac"),
					                    NamedProperty(Sbol2Terms.documented.displayId, "pLac"),
					                    NamedProperty(RdfTerms.rdfType, URI.create("DNA")),
					                    NamedProperty(Sbol2Terms.component.role, URI.create("http://purl.org/obo/owl/SO#SO_0000167")),
					                    NamedProperty(new QName(bqbiol.getNamespaceURI(), "is", bqbiol.getPrefix()),  partsRegistry.namespacedUri("BBa_xxxxx")),
					                    NamedProperty(new QName(visualiser.getNamespaceURI(), "layout", visualiser.getPrefix()),  sbolExample.namespacedUri("sequenceComponent/pLac/layout"))					                    
					            )),
					            TopLevelDocument(
							            Sbol2Terms.documented.documented,
							            sbolExample.namespacedUri("sequenceComponent/pLac/layout"),
							            NamedProperties(
							                    NamedProperty(new QName(visualiser.getNamespaceURI(), "colour", visualiser.getPrefix()), "blue"),
							                    NamedProperty(new QName(visualiser.getNamespaceURI(), "X", visualiser.getPrefix()), 4),
							                    NamedProperty(new QName(visualiser.getNamespaceURI(), "Y", visualiser.getPrefix()), 7),
							                    NamedProperty(new QName(visualiser.getNamespaceURI(), "icon", visualiser.getPrefix()), URI.create("http://www.sometool.org/icons/smallpromoter"))
							                    
							                    
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
