package uk.ac.ncl.intbio.examples;

import javax.xml.namespace.QName;

import uk.ac.ncl.intbio.core.datatree.NamespaceBinding;

public class Sbol2Terms
{
	  public static final NamespaceBinding sbol2 = new NamespaceBinding("http://sbols.org/v2#", "sbol2");
	  
	 
	  
	  public static final class component {
		  public static final QName dnaComponent = sbol2.withLocalPart("DnaComponent");
		  public static final QName dnaSequence = sbol2.withLocalPart("dnaSequence");
		  public static final QName annotation = sbol2.withLocalPart("annotation");
		  public static final QName sequenceAnnotation = sbol2.withLocalPart("SequenceAnnotation");
		  public static final QName bioStart = sbol2.withLocalPart("bioStart");
		  public static final QName bioEnd = sbol2.withLocalPart("bioEnd");
		  public static final QName strand = sbol2.withLocalPart("strand");
		  public static final QName subComponent = sbol2.withLocalPart("subComponent");
		  public static final QName nucleotides = sbol2.withLocalPart("nucleotides");		  		  
	  }
	  
	  public static final class documented {
		  public static final QName name = sbol2.withLocalPart("name");
		  public static final QName description = sbol2.withLocalPart("description");		  
		  public static final QName displayId = sbol2.withLocalPart("displayId");
	  }
	  
	  public static final class module {
		  public static final QName module=sbol2.withLocalPart("Module");
		  public static final QName interaction=sbol2.withLocalPart("Interaction");
		  public static final QName hasInteraction=sbol2.withLocalPart("interaction");
		  public static final QName participation=sbol2.withLocalPart("Participation");
		  public static final QName hasParticipation=sbol2.withLocalPart("participation");
		  public static final QName role=sbol2.withLocalPart("role");
		  public static final QName participant=sbol2.withLocalPart("participant");
		  public static final QName hasModel=sbol2.withLocalPart("model");	  
	  }
	  
	  public static final class instantiation {
		  public static final QName componentInstantiation=sbol2.withLocalPart("ComponentInstantiation");
		  public static final QName hasComponentInstantiation=sbol2.withLocalPart("componentInstantiation");		  		  
	  }
	  
	  public static final class model {
		  public static final QName model=sbol2.withLocalPart("Model");
		  public static final QName source=sbol2.withLocalPart("source");
		  public static final QName language=sbol2.withLocalPart("language");
		  public static final QName framework=sbol2.withLocalPart("framework");
		  public static final QName role=sbol2.withLocalPart("role");
		
	  }
	  
	 
	  
}
