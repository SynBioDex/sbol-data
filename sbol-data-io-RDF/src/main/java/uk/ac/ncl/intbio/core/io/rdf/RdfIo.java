package uk.ac.ncl.intbio.core.io.rdf;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import uk.ac.ncl.intbio.core.datatree.DocumentRoot;
import uk.ac.ncl.intbio.core.datatree.TopLevelDocument;
import uk.ac.ncl.intbio.core.io.CoreIoException;
import uk.ac.ncl.intbio.core.io.IoWriter;

public class RdfIo{

	public IoWriter createIoWriter(final XMLStreamWriter writer)
	{
		return new IoWriter() {
			
			@Override
			public void write(DocumentRoot document) throws CoreIoException {
				try
				{
					writer.writeStartDocument();

					writer.writeStartElement("rdf", "RDF", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
					writer.setPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
					writer.writeNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
					
					writer.setDefaultNamespace("http://sbols.org/v2#");
					writer.writeDefaultNamespace("http://sbols.org/v2#");
					
					for (TopLevelDocument child:document.getTopLevelDocuments())
					{
						write(child);
					}
					writer.writeEndElement();
					writer.writeEndDocument();
				}
				catch(XMLStreamException xse)
				{
					throw new CoreIoException(xse);
				}
				
			}

			private void write(TopLevelDocument child) {
				
				
			}
		};
	}
}
