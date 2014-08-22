package uk.ac.ncl.intbio.examples;

import javax.xml.namespace.QName;

import uk.ac.ncl.intbio.core.datatree.NamespaceBinding;
import static uk.ac.ncl.intbio.core.datatree.Datatree.NamespaceBinding;
/**
 * Provides qualified names for SBOL2.0 objects
 *
 */
public class Sbol2Terms
{
	/**
	 * The namespace binding for SBOL2.0 
	 */
	  public static final NamespaceBinding sbol2 = NamespaceBinding("http://sbols.org/v2#", "sbol2");
	  
	  /**
	   * A group of qualified terms for component related SBOL objects
	   *
	   */
	  public static final class component {
		  public static final QName sequenceComponent = sbol2.withLocalPart("SequenceComponent");
		  public static final QName sequence = sbol2.withLocalPart("Sequence");
		  public static final QName hasSequence = sbol2.withLocalPart("sequence");		  
		  public static final QName annotation = sbol2.withLocalPart("annotation");
		  public static final QName sequenceAnnotation = sbol2.withLocalPart("SequenceAnnotation");
		  public static final QName start = sbol2.withLocalPart("start");
		  public static final QName end = sbol2.withLocalPart("end");
		  public static final QName orientation = sbol2.withLocalPart("orientation");
		  public static final QName component = sbol2.withLocalPart("component");
		  
		  public static final QName elements = sbol2.withLocalPart("elements");	
		  public static final QName sequenceType = sbol2.withLocalPart("sequenceType");	
		  
	  }
	  
	  /**
	   * A group of qualified terms for the SBOL documented interface
	   *
	   */
	  public static final class documented {
		  public static final QName name = sbol2.withLocalPart("name");
		  public static final QName description = sbol2.withLocalPart("description");		  
		  public static final QName displayId = sbol2.withLocalPart("displayId");
	  }
	    
	  /**
	   * A group of qualified terms for module related SBOL objects
	   *
	   */
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
	  
	  /**
	   * A group of qualified terms for instantiation related SBOL objects
	   *
	   */
	  public static final class instantiation {
		  public static final QName componentInstantiation=sbol2.withLocalPart("ComponentInstantiation");
		  public static final QName hasComponentInstantiation=sbol2.withLocalPart("componentInstantiation");	
		  public static final QName subComponentInstantiation = sbol2.withLocalPart("subComponentInstantiation");
		  
	  }
	  
	  /**
	   * A group of qualified terms for model related SBOL objects
	   *
	   */
	  public static final class model {
		  public static final QName model=sbol2.withLocalPart("Model");
		  public static final QName source=sbol2.withLocalPart("source");
		  public static final QName language=sbol2.withLocalPart("language");
		  public static final QName framework=sbol2.withLocalPart("framework");
		  public static final QName role=sbol2.withLocalPart("role");
		
	  }
	  
}
