package uk.ac.ncl.intbio.examples;

import javax.xml.namespace.QName;

import uk.ac.ncl.intbio.core.datatree.NamespaceBinding;

public class SbolTerms
{
	  public static final NamespaceBinding sbol2 = new NamespaceBinding("http://sbols.org/v2#", "sbol2");
	  public static final QName dnaComponent = sbol2.withLocalPart("DnaComponent");
	  public static final QName name = sbol2.withLocalPart("name");
	  public static final QName description = sbol2.withLocalPart("description");
	  public static final QName dnaSequence = sbol2.withLocalPart("dnaSequence");
	  public static final QName annotation = sbol2.withLocalPart("annotation");
	  public static final QName sequenceAnnotation = sbol2.withLocalPart("SequenceAnnotation");
	  public static final QName bioStart = sbol2.withLocalPart("bioStart");
	  public static final QName bioEnd = sbol2.withLocalPart("bioEnd");
	  public static final QName strand = sbol2.withLocalPart("strand");
	  public static final QName subComponent = sbol2.withLocalPart("subComponent");
	  public static final QName nucleotides = sbol2.withLocalPart("nucleotides");
	  public static final QName displayId = sbol2.withLocalPart("displayId");
}
