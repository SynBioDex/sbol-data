package uk.ac.ncl.intbio.core.io.rdf;

import javax.xml.namespace.QName;

import static uk.ac.ncl.intbio.core.datatree.Datatree.NamespaceBinding;
import uk.ac.ncl.intbio.core.datatree.DocumentRoot;
import uk.ac.ncl.intbio.core.datatree.NamespaceBinding;
/**
 * Provides RDF terms that are used when reading and writing {@link DocumentRoot}s 
 * @author goksel
 *
 */
public class RdfTerms
{
	/**
	 * The {@link NamespaceBinding} object for the RDF namespace and its prefix.  
	 */
	  public static final NamespaceBinding rdf = NamespaceBinding("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "rdf");
	  
	  /**
	   * The "RDF" term to represent RDF documents
	   */
	  public static final QName RDF = rdf.withLocalPart("RDF");
	  
	  /**
	   * The qualified name object for the RDF "resource" term
	   */
	  public static final QName rdfResource = rdf.withLocalPart("resource");
	  
	  /**
	   * The qualified name object for the RDF "about" term
	   */
	  public static final QName rdfAbout = rdf.withLocalPart("about");
	  
	  /**
	   * The qualified name object for the RDF "type" term
	   */
	  public static final QName rdfType = rdf.withLocalPart("type");
	
}
