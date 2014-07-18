package uk.ac.ncl.intbio.core.io.rdf;

import javax.xml.namespace.QName;

import uk.ac.ncl.intbio.core.datatree.NamespaceBinding;

public class RdfTerms
{
	  public static final NamespaceBinding rdf = new NamespaceBinding("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "rdf");
	  public static final QName RDF = rdf.withLocalPart("RDF");
	  public static final QName rdfResource = rdf.withLocalPart("resource");
	  public static final QName rdfAbout = rdf.withLocalPart("about");
	  public static final QName rdfType = rdf.withLocalPart("type");
	
}
