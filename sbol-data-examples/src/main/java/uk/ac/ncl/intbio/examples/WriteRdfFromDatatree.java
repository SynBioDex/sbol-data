package uk.ac.ncl.intbio.examples;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;

import javax.sql.rowset.spi.XmlReader;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
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
/**
 * Provides an example for importing and exporting {@link DocumentRoot} objects using the RDF/XML format.
 * In the example a {@link DocumentRoot} including SBOL2.0 objects (with data for Module, Interaction, Participation, ComponentInstantiation and Model objects from the SBOL2.0 proposed data model)
 * is initially written to the console and also to an in-memory {@link String}, which is then read into another {@link DocumentRoot}.
 */
public class WriteRdfFromDatatree
{

  public static void main( String[] args ) throws Exception
  {

	  DocumentRoot<QName> originalDocument = DataTreeCreator.makeSBOL2Document();
	  write(new OutputStreamWriter(System.out), originalDocument);
	  StringWriter stringWriter=new StringWriter();
	  write (stringWriter, originalDocument);
	  
	  System.out.println("------------------------------------------------------");
	  
	  String output=stringWriter.getBuffer().toString();
	  stringWriter.close();
	  
	  System.out.print(output);
	  
	  DocumentRoot<QName> document= read(new StringReader(output));
	  
	  System.out.print(document);
	  write(new OutputStreamWriter(System.out), document);
	  
	  
	  
  }
  
	private static void write(Writer stream, DocumentRoot<QName> document) throws Exception
	{
		XMLStreamWriter xmlWriter = new IndentingXMLStreamWriter(XMLOutputFactory.newInstance().createXMLStreamWriter(stream));
		RdfIo rdfIo = new RdfIo();
		// rdfIo.createIoWriter(xmlWriter).write(makeDocument());
		rdfIo.createIoWriter(xmlWriter).write(document);
		xmlWriter.flush();
		xmlWriter.close();
	}
	
	private static DocumentRoot<QName> read(Reader reader) throws Exception
	{
		XMLStreamReader xmlReader=XMLInputFactory.newInstance().createXMLStreamReader(reader);
		RdfIo rdfIo = new RdfIo();
		return rdfIo.createIoReader(xmlReader).read();		
	}
}
