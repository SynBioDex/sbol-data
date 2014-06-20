package uk.ac.ncl.intbio;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import uk.ac.ncl.intbio.core.io.rdf.RdfIo;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
       RdfIo rdfIo=new RdfIo();
       XMLStreamWriter xmlWriter=XMLOutputFactory.newInstance().createXMLStreamWriter(System.out);
       rdfIo.createIoWriter(xmlWriter).write(null);
       xmlWriter.flush();
       xmlWriter.close();
       /*
       XMLOutputFactory f = XMLOutputFactory.newInstance();
       XMLStreamWriter writer = f.createXMLStreamWriter(System.out);
       writer.writeStartElement("p", "element1", "urn:ns1");
       writer.setPrefix("p", "urn:ns1");
       writer.writeNamespace("p", "urn:ns1");
       writer.writeEmptyElement("urn:ns1", "element1");
       writer.writeEmptyElement("urn:ns1", "element2");
       writer.writeEndElement();
       writer.flush();
       writer.close();*/
    }
}
