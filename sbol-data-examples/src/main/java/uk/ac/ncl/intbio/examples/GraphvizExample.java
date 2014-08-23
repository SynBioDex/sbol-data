package uk.ac.ncl.intbio.examples;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javanet.staxutils.IndentingXMLStreamWriter;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import uk.ac.ncl.intbio.core.datatree.DocumentRoot;
import uk.ac.ncl.intbio.core.datatree.IdentifiableDocument;
import uk.ac.ncl.intbio.core.datatree.Literal;
import uk.ac.ncl.intbio.core.datatree.PropertyValue;
import uk.ac.ncl.intbio.core.io.graphviz.DocumentStyler;
import uk.ac.ncl.intbio.core.io.graphviz.GraphvizIo;
import uk.ac.ncl.intbio.core.io.graphviz.Styler;
import uk.ac.ncl.intbio.core.io.rdf.RdfIo;
import uk.ac.ncl.intbio.core.io.rdf.RdfTerms;
/**
 * Provides an example for exporting {@link DocumentRoot} objects using the JSON format.
 * In the example, {@link DocumentRoot} objects with both SBOL1.1.0 and SBOL2.0 data are exported in the GraphViz format.
 * @param args
 * @throws Exception
 */
public class GraphvizExample
{
	/**
	 * Exports two example DocumentRoot objects (SBOL 1.1.0 and SBOL 2.0 objects) in the GraphViz format. 
	 * @param args
	 * @throws Exception
	 */
  public static void main( String[] args ) throws Exception
  {
    System.out.println("SBOL 1 example");
    write(new OutputStreamWriter(System.out), DataTreeCreator.makeDocument());

    System.out.println();
    System.out.println("----------------------------------------");
    System.out.println();

    System.out.println("SBOL 2 example");
    write(new OutputStreamWriter(System.out), DataTreeCreator.makeSBOL2Document());
  }

  private static void write(Writer stream, DocumentRoot<QName> document) throws Exception
  {
    PrintWriter writer = new PrintWriter(stream);
    GraphvizIo io = new GraphvizIo();

    io.setDocumentStyler(Styler.document.all(
            io.getDocumentStyler(),
            Styler.document.apply(Styler.mapMod.set("shape", "folder")),
            sbolPartTypeStyler));
    io.setLiteralStyler(Styler.literal.all(
            io.getLiteralStyler(),
            Styler.literal.apply(Styler.mapMod.set("shape", "note"))));

    io.createIoWriter(writer).write(document);
    writer.flush();
  }

  public static DocumentStyler sbolPartTypeStyler = new DocumentStyler() {
    @Override
    public void applyStyle(Map<String, String> styleMap, IdentifiableDocument<QName> document) {
      if(document.getType() == SbolTerms.dnaComponent) {
        for(PropertyValue<QName> pv : document.getPropertyValues(RdfTerms.rdfType)) {
          if (pv instanceof Literal.UriLiteral) {
            Literal.UriLiteral<QName> luri = (Literal.UriLiteral<QName>) pv;
            String shape = graphvizShapeForSo.get(luri.getValue());
            if (shape != null) {
              styleMap.put("shape", shape);
            }
          }
        }
      }
    }
  };
  
  /**
   * A map defining the mapping between SO terms and respective icons to be displayed when a DocumentRoot is exported.
   */
  public static final Map<URI, String> graphvizShapeForSo;

  static {
    graphvizShapeForSo = new HashMap<>();

    graphvizShapeForSo.put(URI.create("http://purl.obolibrary.org/obo/SO_0000139"), "ribosite");
    graphvizShapeForSo.put(URI.create("http://purl.obolibrary.org/obo/SO_0000316"), "cds");
  }
}
