package uk.ac.ncl.intbio.examples;

import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javanet.staxutils.IndentingXMLStreamWriter;

import javax.json.Json;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import uk.ac.ncl.intbio.core.datatree.DocumentRoot;
import uk.ac.ncl.intbio.core.io.json.JsonIo;
import uk.ac.ncl.intbio.core.io.json.StringifyQName;
import uk.ac.ncl.intbio.core.io.rdf.RdfIo;

public class WriteJsonFromDataTree
{

	public static void main(String[] args) throws Exception
	{
		DocumentRoot<QName> originalDocument = DataTreeCreator.makeSBOL2Document();
		write(new OutputStreamWriter(System.out), originalDocument);
	}

	private static void write(Writer stream, DocumentRoot<QName> document) throws Exception
	{
		JsonGenerator writer = Json.createGenerator(stream);
		JsonIo jsonIo = new JsonIo();
		jsonIo.createIoWriter(writer).write(StringifyQName.INSTANCE.mapDR(document));
		writer.flush();
		writer.close();
	}

}
