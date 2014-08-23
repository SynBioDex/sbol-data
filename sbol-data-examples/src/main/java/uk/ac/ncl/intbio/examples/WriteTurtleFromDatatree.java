package uk.ac.ncl.intbio.examples;

import uk.ac.intbio.core.io.turtle.TurtleIo;
import uk.ac.ncl.intbio.core.datatree.DocumentRoot;

import javax.xml.namespace.QName;

import java.io.*;

/**
 * Provides an example for importing and exporting {@link DocumentRoot} objects using the RDF Turtle format.
 * In the example a {@link DocumentRoot} including SBOL2.0 objects (with data for Module, Interaction, Participation, ComponentInstantiation and Model objects from the SBOL2.0 proposed data model)
 * is initially written to the console and also to an in-memory {@link String}, which is then read into another {@link DocumentRoot}
 */
public class WriteTurtleFromDatatree
{

  public static void main( String[] args ) throws Exception
  {

	  DocumentRoot<QName> originalDocument = DataTreeCreator.makeSBOL2Document();
	  write(new OutputStreamWriter(System.out), originalDocument);
	  StringWriter stringWriter=new StringWriter();
	  write (stringWriter, originalDocument);
	  
	  System.out.println("------------------------------------------------------");
	  
	  String output = stringWriter.getBuffer().toString();
	  stringWriter.close();

	  DocumentRoot<QName> document = read(new StringReader(output));

	  write(new OutputStreamWriter(System.out), document);
  }
  
	private static void write(Writer stream, DocumentRoot<QName> document) throws Exception
	{
		PrintWriter printWriter = new PrintWriter(stream);
		TurtleIo turtleIo = new TurtleIo();
		turtleIo.createIoWriter(printWriter).write(document);
		printWriter.flush();
	}
	
	private static DocumentRoot<QName> read(Reader reader) throws Exception
	{
		TurtleIo turtleIo = new TurtleIo();
		return turtleIo.createIoReader(reader).read();
	}
}
