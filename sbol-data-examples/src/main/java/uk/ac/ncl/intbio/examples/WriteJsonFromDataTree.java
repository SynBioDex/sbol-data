package uk.ac.ncl.intbio.examples;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javanet.staxutils.IndentingXMLStreamWriter;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import uk.ac.ncl.intbio.core.datatree.DocumentRoot;
import uk.ac.ncl.intbio.core.io.IoReader;
import uk.ac.ncl.intbio.core.io.json.JsonIo;
import uk.ac.ncl.intbio.core.io.json.StringifyQName;
import uk.ac.ncl.intbio.core.io.rdf.RdfIo;

public class WriteJsonFromDataTree
{

	public static void main(String[] args) throws Exception
	{
		DocumentRoot<QName> originalDocument = DataTreeCreator.makeSBOL2Document();
    StringWriter writer = new StringWriter();
		write(writer, originalDocument);

    System.out.println(writer.getBuffer().toString());
    System.out.println("-----------------------------------------");


    DocumentRoot<QName> readQNameDocument = read(new StringReader(writer.getBuffer().toString()));
    System.out.println(readQNameDocument);
    System.out.println("-----------------------------------------");
    write(new OutputStreamWriter(System.out), readQNameDocument);
	}

	private static void write(Writer stream, DocumentRoot<QName> document) throws Exception
	{
    Map<String, Object> config = new HashMap<>();
    config.put(JsonGenerator.PRETTY_PRINTING, true);
		JsonGenerator writer = Json.createGeneratorFactory(config).createGenerator(stream);
		JsonIo jsonIo = new JsonIo();
		jsonIo.createIoWriter(writer).write(StringifyQName.qname2string.mapDR(document));
		writer.flush();
		writer.close();
	}

  private static DocumentRoot<QName> read(Reader stream) throws Exception
  {
    JsonReader reader = Json.createReaderFactory(Collections.<String, Object>emptyMap()).createReader(stream);
    JsonIo jsonIo = new JsonIo();
    IoReader<String> ioReader = jsonIo.createIoReader(reader.read());
    DocumentRoot<String> root = ioReader.read();
    return StringifyQName.string2qname.mapDR(root);
  }
}
