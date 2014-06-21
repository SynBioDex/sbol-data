package uk.ac.ncl.intbio.examples;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import com.sun.xml.internal.txw2.output.IndentingXMLStreamWriter;
import uk.ac.ncl.intbio.core.datatree.Datatree;
import uk.ac.ncl.intbio.core.datatree.DocumentRoot;
import uk.ac.ncl.intbio.core.io.rdf.RdfIo;
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
  private static String exampleNS = "http://example/bla#";
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

  public static DocumentRoot<QName> makeDocument() {
    return DocumentRoot(
            TopLevelDocuments(
                    TopLevelDocument(
                            RdfIo.dnaComponent,
                            QName(exampleNS, "dc1"),
                            NamedProperties(
                                    NamedProperty(RdfIo.name, "dna component 1")
                            ))
            ),
            Datatree.LiteralProperties(
                    NamedLiteralProperty(QName(dctermsNS, "creator", dctermsPF), "Matthew Pocock")
            ));
  }
}
