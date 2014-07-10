package uk.ac.ncl.intbio.examples;
import java.net.URI;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import javanet.staxutils.IndentingXMLStreamWriter;
import uk.ac.ncl.intbio.core.datatree.Datatree;
import uk.ac.ncl.intbio.core.datatree.DocumentRoot;
import uk.ac.ncl.intbio.core.datatree.NamespaceBinding;
import uk.ac.ncl.intbio.core.datatree.NestedDocument;
import uk.ac.ncl.intbio.core.datatree.TopLevelDocument;
import uk.ac.ncl.intbio.core.io.rdf.RdfIo;
import uk.ac.ncl.intbio.core.io.rdf.RdfTerms;
import static uk.ac.ncl.intbio.core.datatree.Datatree.*;

/**
 * Write a demo document out as RDF.
 *
 * @author Matthew Pocock
 * @author Goksel Misirli
 */
public class WriteRdfFromDatatree
{
  private static String sbol2NS = "http://sbol.org/sbolv2#";
  public static final NamespaceBinding partsRegistry = new NamespaceBinding("http://partsregistry.org/", "pr");
  public static final NamespaceBinding sbolExample = new NamespaceBinding("http://sbolstandard.org/example#", "sbolexample");
  
  
  
 
  private static String dctermsNS = "http://purl.org/dc/terms/";
  private static String dctermsPF = "dcterms";

  public static void main( String[] args ) throws Exception
  {
    XMLStreamWriter xmlWriter = new IndentingXMLStreamWriter(
            XMLOutputFactory.newInstance().createXMLStreamWriter(System.out));
    RdfIo rdfIo = new RdfIo();
    //rdfIo.createIoWriter(xmlWriter).write(makeDocument());
    rdfIo.createIoWriter(xmlWriter).write(makeSBOL2Document());    
    xmlWriter.flush();
    xmlWriter.close();
  }

	public static DocumentRoot<QName> makeDocument()
	{
		return DocumentRoot(
				NamespaceBindings(SbolTerms.sbol2),
				TopLevelDocuments(
						TopLevelDocument(
								SbolTerms.dnaComponent,
								partsRegistry.withLocalPart("Part:BBa_I0462"),
								NamedProperties(
										NamedProperty(SbolTerms.name, "I0462"),
										NamedProperty(SbolTerms.description, "LuxR protein generator"),
										NamedProperty(SbolTerms.dnaSequence, partsRegistry.withLocalPart("Part:BBa_I0462/sequence")),
										NamedProperty(
												SbolTerms.annotation,
												NestedDocuments(
														NestedDocument(
																SbolTerms.sequenceAnnotation,
																partsRegistry.withLocalPart("Part:BBa_I0462/anot/1234567"),
																NamedProperties(NamedProperty(SbolTerms.bioStart, 1), NamedProperty(SbolTerms.bioEnd, 12),
																		NamedProperty(SbolTerms.strand, "+"),
																		NamedProperty(SbolTerms.subComponent, partsRegistry.withLocalPart("Part:BBa_B0034")))),
														NestedDocument(
																SbolTerms.sequenceAnnotation,
																partsRegistry.withLocalPart("Part:BBa_I0462/annotation/2345678"),
																NamedProperties(NamedProperty(SbolTerms.bioStart, 19), NamedProperty(SbolTerms.bioEnd, 774),
																		NamedProperty(SbolTerms.subComponent, partsRegistry.withLocalPart("Part:BBa_C0062")))))))),
						TopLevelDocument(
								SbolTerms.dnaSequence,
								partsRegistry.withLocalPart("Part:BBa_I0462/sequence"),
								NamedProperties(NamedProperty(
										SbolTerms.nucleotides,
										"aaagaggagaaatactagatgaaaaacataaatgccgacgacacatacagaataattaataaaattaaagcttgtagaagcaataatgatattaatcaatgcttatctgatatgactaaaatggtacattgtgaatattatttactcgcgatcatttatcctcattctatggttaaatctgatatttcaatcctagataattaccctaaaaaatggaggcaatattatgatgacgctaatttaataaaatatgatcctatagtagattattctaactccaatcattcaccaattaattggaatatatttgaaaacaatgctgtaaataaaaaatctccaaatgtaattaaagaagcgaaaacatcaggtcttatcactgggtttagtttccctattcatacggctaacaatggcttcggaatgcttagttttgcacattcagaaaaagacaactatatagatagtttatttttacatgcgtgtatgaacataccattaattgttccttctctagttgataattatcgaaaaataaatatagcaaataataaatcaaacaacgatttaaccaaaagagaaaaagaatgtttagcgtgggcatgcgaaggaaaaagctcttgggatatttcaaaaatattaggttgcagtgagcgtactgtcactttccatttaaccaatgcgcaaatgaaactcaatacaacaaaccgctgccaaagtatttctaaagcaattttaacaggagcaattgattgcccatactttaaaaattaataacactgatagtgctagtgtagatcactactagagccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttata"
										))),
						
						TopLevelDocument(
								SbolTerms.dnaComponent,
								partsRegistry.withLocalPart("Part:BBa_B0034"),
								NamedProperties(
										NamedProperty(SbolTerms.name, "I0462"),
										NamedProperty(SbolTerms.displayId, "BBa_B0034"),
										NamedProperty(RdfTerms.rdfType, URI.create("http://purl.obolibrary.org/obo/SO_0000139"))																				
								)),
								TopLevelDocument(
										SbolTerms.dnaComponent,
										partsRegistry.withLocalPart("Part:BBa_C0062"),
										NamedProperties(
												NamedProperty(SbolTerms.name, "luxR"),
												NamedProperty(SbolTerms.displayId, "BBa_C0062"),
												NamedProperty(RdfTerms.rdfType, URI.create("http://purl.obolibrary.org/obo/SO_0000316"))																				
										))
						),
				Datatree.LiteralProperties(NamedLiteralProperty(QName(dctermsNS, "creator", dctermsPF), "Matthew Pocock")));
	}
	
	public static DocumentRoot<QName> makeSBOL2Document()
	{
		NestedDocument<QName> instantiationLacI=NestedDocument(
				Sbol2Terms.componentInstantiation, 
				sbolExample.withLocalPart("module_LacI_inverter/LacI_instantiation"), 
				NamedProperties(
						NamedProperty(Sbol2Terms.name, "LacI")					
				));
		
		NestedDocument<QName> instantiationIPTG=NestedDocument(
				Sbol2Terms.componentInstantiation, 
				sbolExample.withLocalPart("module_LacI_inverter/IPTG"), 
				NamedProperties(
						NamedProperty(Sbol2Terms.name, "IPTG")					
				));
		
		NestedDocument<QName> instantiationIPTGLacI=NestedDocument(
				Sbol2Terms.componentInstantiation, 
				sbolExample.withLocalPart("module_LacI_inverter/IPTGLacI_instantiation"), 
				NamedProperties(
						NamedProperty(Sbol2Terms.name, "IPTG LacI complex")					
				));
		NestedDocument<QName> instantiationpLac=NestedDocument(
				Sbol2Terms.componentInstantiation, 
				sbolExample.withLocalPart("module_LacI_inverter/pLac_instantiation"), 
				NamedProperties(
						NamedProperty(Sbol2Terms.name, "pLac promoter")					
				));
		NestedDocument<QName> instantiationcTetR=NestedDocument(
				Sbol2Terms.componentInstantiation, 
				sbolExample.withLocalPart("module_LacI_inverter/cTetR_instantiation"), 
				NamedProperties(
						NamedProperty(Sbol2Terms.name, "cTetR")					
				));
		NestedDocument<QName> instantiationTetR=NestedDocument(
				Sbol2Terms.componentInstantiation, 
				sbolExample.withLocalPart("module_LacI_inverter/TetR_instantiation"), 
				NamedProperties(
						NamedProperty(Sbol2Terms.name, "TetR")					
				));
		
		
		NestedDocument<QName> interactionIPTGBinding=NestedDocument(
				Sbol2Terms.interaction, 
				sbolExample.withLocalPart("module_LacI_inverter/interaction/IPTG_binding"), 
				NamedProperties(
						NamedProperty(Sbol2Terms.name, "IPTG Binding"),
						NamedProperty(RdfTerms.rdfType, URI.create("http://purl.obolibrary.org/obo/non_covalent_binding")),
						NamedProperty(Sbol2Terms.hasParticipation,
								NestedDocuments(
										NestedDocument(
												Sbol2Terms.participation,
												partsRegistry.withLocalPart("module_LacI_inverter/interaction/IPTG_Binding/LacI_participation"),
												NamedProperties(
														NamedProperty(Sbol2Terms.role,URI.create("http://purl.obolibrary.org/obo/reactant")),
														NamedProperty(Sbol2Terms.participant, instantiationLacI.getIdentity())
												)												
										),
										NestedDocument(
												Sbol2Terms.participation,
												partsRegistry.withLocalPart("module_LacI_inverter/interaction/IPTG_Binding/IPTGLacI_participation"),
												NamedProperties(
														NamedProperty(Sbol2Terms.role,URI.create("http://purl.obolibrary.org/obo/product")),
														NamedProperty(Sbol2Terms.participant, instantiationIPTGLacI.getIdentity())
												)												
										),
										NestedDocument(
												Sbol2Terms.participation,
												partsRegistry.withLocalPart("module_LacI_inverter/interaction/IPTG_Binding/IPTG_participation"),
												NamedProperties(
														NamedProperty(Sbol2Terms.role,URI.create("http://purl.obolibrary.org/obo/reactant")),
														NamedProperty(Sbol2Terms.participant, instantiationIPTG.getIdentity())
												)												
										)										
								)
						)
				));
		
		
		
		NestedDocument<QName> interactionLacIRepression=NestedDocument(
				Sbol2Terms.interaction, 
				sbolExample.withLocalPart("module_LacI_inverter/interaction/LacI_repression"), 
				NamedProperties(
						NamedProperty(Sbol2Terms.name, "LacI Repression"),
						NamedProperty(RdfTerms.rdfType, URI.create("http://purl.obolibrary.org/obo/repression")),
						NamedProperty(Sbol2Terms.hasParticipation,
								NestedDocuments(
										NestedDocument(
												Sbol2Terms.participation,
												partsRegistry.withLocalPart("module_LacI_inverter/interaction/LacI_repression/LacI"),
												NamedProperties(
														NamedProperty(Sbol2Terms.role,URI.create("http://purl.obolibrary.org/obo/repressor")),
														NamedProperty(Sbol2Terms.participant, instantiationLacI.getIdentity())
												)												
										),
										NestedDocument(
												Sbol2Terms.participation,
												partsRegistry.withLocalPart("module_LacI_inverter/interaction/LacI_repression/pLac"),
												NamedProperties(
														NamedProperty(Sbol2Terms.role,URI.create("http://purl.obolibrary.org/obo/repressed")),
														NamedProperty(Sbol2Terms.participant, instantiationpLac.getIdentity())
												)												
										)										
								)
						)
				));
		
		
		NestedDocument<QName> interactionTetRTranscriptionTranslation=NestedDocument(
				Sbol2Terms.interaction, 
				sbolExample.withLocalPart("module_LacI_inverter/interaction/TetR_transcription_translation"), 
				NamedProperties(
						NamedProperty(Sbol2Terms.name, "TetR Transcription Translation"),
						NamedProperty(RdfTerms.rdfType, URI.create("http://purl.obolibrary.org/obo/genetic_production")),
						NamedProperty(Sbol2Terms.hasParticipation,
								NestedDocuments(
										NestedDocument(
												Sbol2Terms.participation,
												partsRegistry.withLocalPart("module_LacI_inverter/interaction/TetR_transcription_translation/TetR_participation"),
												NamedProperties(
														NamedProperty(Sbol2Terms.role,URI.create("http://purl.obolibrary.org/obo/product")),
														NamedProperty(Sbol2Terms.participant, instantiationTetR.getIdentity())
												)												
										),
										NestedDocument(
												Sbol2Terms.participation,
												partsRegistry.withLocalPart("module_LacI_inverter/interaction/TetR_transcription_translation/cTetR_participation"),
												NamedProperties(
														NamedProperty(Sbol2Terms.role,URI.create("http://purl.obolibrary.org/obo/transcribed")),
														NamedProperty(Sbol2Terms.participant, instantiationcTetR.getIdentity())
												)												
										),
										NestedDocument(
												Sbol2Terms.participation,
												partsRegistry.withLocalPart("module_LacI_inverter/interaction/TetR_transcription_translation/pLac_participation"),
												NamedProperties(
														NamedProperty(Sbol2Terms.role,URI.create("http://purl.obolibrary.org/obo/modifier")),
														NamedProperty(Sbol2Terms.participant, instantiationpLac.getIdentity())
												)												
										)										
								)
						)
				));
		
		
	
		TopLevelDocument<QName> modelLacIInverter=TopLevelDocument(
				Sbol2Terms.model,
				sbolExample.withLocalPart("model/LacI_inverter"),
				NamedProperties(
						NamedProperty(Sbol2Terms.name, "LacI Inverter Model"),
						NamedProperty(Sbol2Terms.source,URI.create("http://www.async.ece.utah.edu/LacI_Inverter.xml")),
						NamedProperty(Sbol2Terms.language,"SBML"),
						NamedProperty(Sbol2Terms.framework,"ODE"),
						NamedProperty(Sbol2Terms.modelRole,"simulation")
						
						
				));
				
		TopLevelDocument<QName> moduleLacIInverter=TopLevelDocument(
				Sbol2Terms.module,
				sbolExample.withLocalPart("module/LacI_inverter"),
				NamedProperties(
						NamedProperty(Sbol2Terms.name, "LacI Inverter"),
						NamedProperty(Sbol2Terms.hasInteraction,NestedDocuments(interactionIPTGBinding,interactionLacIRepression,interactionTetRTranscriptionTranslation)),
						NamedProperty(Sbol2Terms.hasComponentInstantiation,
								NestedDocuments(instantiationLacI,instantiationIPTG,instantiationIPTGLacI,instantiationpLac,instantiationcTetR,instantiationTetR)),
						NamedProperty(Sbol2Terms.hasModel,modelLacIInverter.getIdentity())		
						
				));
		

		

		
				
		return DocumentRoot(
				NamespaceBindings(SbolTerms.sbol2),
				TopLevelDocuments(moduleLacIInverter,modelLacIInverter),
				Datatree.LiteralProperties(NamedLiteralProperty(QName(dctermsNS, "creator", dctermsPF), "Goksel Misirli"))
				);
	}
}
