/**
 * The entry point for this package is {@link uk.ac.ncl.intbio.core.io.rdf.RdfIo}, which provides methods to
 * read and write {@link uk.ac.ncl.intbio.core.datatree.DocumentRoot} objects in RDF/XML format. 
 * 
 * <p>
 * The read and write methods are accessed by creating {@link uk.ac.ncl.intbio.core.io.IoWriter} and {@link uk.ac.ncl.intbio.core.io.IoReader} objects using {@link uk.ac.ncl.intbio.core.io.rdf.RdfIo}.
 * 
 * The writer is created by passing an {@link javax.xml.stream.XMLStreamWriter} to the createIoWriter method of {@link uk.ac.ncl.intbio.core.io.rdf.RdfIo}.
 * <br></br>
 * The following example can be used to to serialise data using {@link uk.ac.ncl.intbio.core.io.rdf.RdfIo}.
 * <pre>
 * 		XMLStreamWriter xmlWriter = new IndentingXMLStreamWriter(XMLOutputFactory.newInstance().createXMLStreamWriter(stream));
 *		RdfIo rdfIo = new RdfIo();
 *		rdfIo.createIoWriter(xmlWriter).write(document);
 *		xmlWriter.flush();
 *		xmlWriter.close();
 *</pre>
 *</p>
 *<p>
 * Similarly, the reader is  created by passing an {@link javax.xml.stream.XMLStreamReader} to the createIoReader method of  {@link uk.ac.ncl.intbio.core.io.rdf.RdfIo}.
 * An example code to deserialise data using the reader is shown below.
 * <pre>
 *		XMLStreamReader xmlReader=XMLInputFactory.newInstance().createXMLStreamReader(reader);
 *		RdfIo rdfIo = new RdfIo();
 *		DocumentRoot<QName> document= rdfIo.createIoReader(xmlReader).read();
 *</pre>
 * </p>
 *  
 */
package uk.ac.ncl.intbio.core.io.rdf;

import javax.xml.namespace.QName;

import uk.ac.ncl.intbio.core.datatree.DocumentRoot;

