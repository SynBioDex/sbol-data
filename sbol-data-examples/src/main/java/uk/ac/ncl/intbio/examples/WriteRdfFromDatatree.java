package uk.ac.ncl.intbio.examples;
import java.net.URI;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import javanet.staxutils.IndentingXMLStreamWriter;
import uk.ac.ncl.intbio.core.datatree.Datatree;
import uk.ac.ncl.intbio.core.datatree.DocumentRoot;
import uk.ac.ncl.intbio.core.datatree.NamespaceBinding;
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
  
  
 
  private static String dctermsNS = "http://purl.org/dc/terms/";
  private static String dctermsPF = "dcterms";

  public static void main( String[] args ) throws Exception
  {
    XMLStreamWriter xmlWriter = new IndentingXMLStreamWriter(
            XMLOutputFactory.newInstance().createXMLStreamWriter(System.out));
    RdfIo rdfIo = new RdfIo();
    rdfIo.createIoWriter(xmlWriter).write(makeDocument());
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
}
