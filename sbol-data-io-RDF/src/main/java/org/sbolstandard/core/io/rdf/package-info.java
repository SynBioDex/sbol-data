/**
 * The entry point for this package is {@link org.sbolstandard.core.io.rdf.RdfIo}, which provides methods to
 * read and write {@link org.sbolstandard.core.datatree.DocumentRoot} objects in RDF/XML format.
 *
 * <p>
 * The read and write methods are accessed by creating {@link org.sbolstandard.core.io.IoWriter} and {@link org.sbolstandard.core.io.IoReader} objects using {@link org.sbolstandard.core.io.rdf.RdfIo}.
 * </p>
 *
 * <p>
 * The writer is created by passing an {@link javax.xml.stream.XMLStreamWriter} to the createIoWriter method of {@link org.sbolstandard.core.io.rdf.RdfIo}.
 * </p>
 *
 * <p>
 * The writer is created by passing an {@link javax.xml.stream.XMLStreamWriter} to the createIoWriter method of {@link org.sbolstandard.core.io.rdf.RdfIo}.
 * The following example can be used to to serialise data using {@link org.sbolstandard.core.io.rdf.RdfIo}.
 * </p>
 * <pre><code>
 * 		XMLStreamWriter xmlWriter = new IndentingXMLStreamWriter(XMLOutputFactory.newInstance().createXMLStreamWriter(stream));
 *		RdfIo rdfIo = new RdfIo();
 *		rdfIo.createIoWriter(xmlWriter).write(document);
 *		xmlWriter.flush();
 *		xmlWriter.close();
 *</code></pre>
 *<p>
 * Similarly, the reader is  created by passing an {@link javax.xml.stream.XMLStreamReader} to the createIoReader method of  {@link org.sbolstandard.core.io.rdf.RdfIo}.
 * An example code to deserialise data using the reader is shown below.
 * </p>
 * <pre><code>
 *		XMLStreamReader xmlReader = XMLInputFactory.newInstance().createXMLStreamReader(reader);
 *		RdfIo rdfIo = new RdfIo();
 *		DocumentRoot&gt;QName&lt; document = rdfIo.createIoReader(xmlReader).read();
 * </code></pre>
 *
 *  @author Goksel Misirli
 *  @author Matthew Pocock
 */
package org.sbolstandard.core.io.rdf;
