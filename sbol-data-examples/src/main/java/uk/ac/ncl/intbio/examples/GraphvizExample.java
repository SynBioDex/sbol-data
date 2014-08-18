package uk.ac.ncl.intbio.examples;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javanet.staxutils.IndentingXMLStreamWriter;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import uk.ac.ncl.intbio.core.datatree.DocumentRoot;
import uk.ac.ncl.intbio.core.io.graphviz.GraphvizIo;
import uk.ac.ncl.intbio.core.io.rdf.RdfIo;

public class GraphvizExample
{
	public static void main( String[] args ) throws Exception
	  {
		  //DocumentRoot originalDocument = makeSBOL2Document();
		  DocumentRoot originalDocument = DataTreeCreator.makeDocument();
		  
		  write(new OutputStreamWriter(System.out), originalDocument);
		 /* StringWriter stringWriter=new StringWriter();
		  write (stringWriter, originalDocument);
		  
		  System.out.println("------------------------------------------------------");
		  
		  String output=stringWriter.getBuffer().toString();
		  stringWriter.close();
		  
		  System.out.print(output);
		  
		  DocumentRoot<QName> document= read(new StringReader(output));
		  
		  System.out.print(document);
		  write(new OutputStreamWriter(System.out), document);*/
		  
	  }
	
	private static void write(Writer stream, DocumentRoot<QName> document) throws Exception
	{
		PrintWriter writer = new PrintWriter(stream);
		GraphvizIo io = new GraphvizIo();
		io.createIoWriter(writer).write(document);
		writer.flush();
		writer.close();
	}
	
	
}
