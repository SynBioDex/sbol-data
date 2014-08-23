package uk.ac.ncl.intbio.core.io.rdf;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.XMLEvent;

import uk.ac.ncl.intbio.core.datatree.*;
import uk.ac.ncl.intbio.core.datatree.Datatree.NamedProperties;
import uk.ac.ncl.intbio.core.datatree.Datatree.NamespaceBindings;
import uk.ac.ncl.intbio.core.io.CoreIoException;
import uk.ac.ncl.intbio.core.io.IoReader;
import uk.ac.ncl.intbio.core.io.IoWriter;
import static uk.ac.ncl.intbio.core.io.rdf.RdfTerms.*;

/** 
 * The IO layer to read and write {@link DocumentRoot}s using RDF/XML.
 * <p>
 * Documents are serialised using nesting, in which {@link TopLevelDocument}s embed {@link NestedDocument}s. 
 * </p>
 *
 * <p>
 * Both {@link TopLevelDocument}s and {@link NestedDocument}s are represented as RDF resources, and 
 * {@link NamedProperty} objects are serialised as statements for these RDF resources. 
 * </p>
 */
public class RdfIo{
  
  /**
   * Creates an {@link IoWriter} using the given {@link XMLStreamWriter} object.
   * 
   * <p>This {@link IoWriter} provides a method to write {@link DocumentRoot} objects in RDF/XML format. 
   * During the serialisation, the RDF namespace is added if it is not provided in the {@link NamespaceBinding}s property of a {@link DocumentRoot}.
   * </p>
   * @param writer The {@link XMLStreamWriter} writer to serialise a {@link DocumentRoot}.
   * @return {@link IoWriter}
   */
  public IoWriter<QName> createIoWriter(final XMLStreamWriter writer)
  {
    return new IoWriter<QName>() {

      @Override
      public void write(DocumentRoot<QName> document) throws CoreIoException {
        try
        {
          writer.writeStartDocument();

          writeStartElement(RDF);
          setPrefix(rdf);

          if(!document.getNamespaceBindings().contains(rdf)) {
            writeNamespace(rdf);
          }

          for(NamespaceBinding nb : document.getNamespaceBindings()) {
            setPrefix(nb);
            writeNamespace(nb);
          }

          for (TopLevelDocument<QName> child : document.getTopLevelDocuments())
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

      private void write(IdentifiableDocument<QName> doc) throws XMLStreamException {
        writeStartElement(doc.getType());
        writeAttribute(rdfAbout, doc.getIdentity().toString());

        for (NamedProperty<QName> property : doc.getProperties()) {
          write(property);
        }

        writer.writeEndElement();
      }

      private void write(final NamedProperty<QName> property) {
        new PropertyValue.Visitor<QName>() {
          @Override
          public void visit(NestedDocument<QName> v) throws XMLStreamException {
            writeStartElement(property.getName());
            write(v);
            writer.writeEndElement();
          }

          @Override
          public void visit(Literal<QName> v) throws XMLStreamException {
            if(isEmptyElementValue(v)) {
              writeEmptyElement(property.getName());
              write(v);
            } else {
              writeStartElement(property.getName());
              write(v);
              writer.writeEndElement();
            }
          }
        }.visit(property.getValue());
      }

      private boolean isEmptyElementValue(Literal<QName> literal) {
        return /* literal instanceof Literal.QNameLiteral || */ literal instanceof Literal.UriLiteral;
      }

      private void write(Literal<QName> literal) {
        new Literal.Visitor<QName>() {
          @Override
          public void visit(Literal.StringLiteral<QName> l) throws XMLStreamException {
            writer.writeCharacters(l.getValue());
          }

          @Override
          public void visit(Literal.UriLiteral<QName> l) throws XMLStreamException {
            writeAttribute(rdfResource, l.getValue().toString());
          }

          @Override
          public void visit(Literal.IntegerLiteral<QName> l) throws XMLStreamException {
            writer.writeCharacters(l.getValue().toString());
          }

          @Override
          public void visit(Literal.DoubleLiteral<QName> l) throws XMLStreamException {
            writer.writeCharacters(l.getValue().toString());
          }

          @Override
          public void visit(Literal.TypedLiteral<QName> l) throws XMLStreamException {
            writer.writeCharacters(l.getValue() + "^^" + l.getType().getPrefix() + ":" + l.getType().getLocalPart());
          }

          @Override
          public void visit(Literal.BooleanLiteral<QName> l) throws XMLStreamException {
            writer.writeCharacters(l.getValue().toString());
          }
        }.visit(literal);
      }

      private void writeEmptyElement(QName tagName) throws XMLStreamException {
        writer.writeEmptyElement(tagName.getPrefix(), tagName.getLocalPart(), tagName.getNamespaceURI());
      }

      private void writeStartElement(QName tagName) throws XMLStreamException {
        writer.writeStartElement(tagName.getPrefix(), tagName.getLocalPart(), tagName.getNamespaceURI());
      }

      private void setPrefix(NamespaceBinding binding) throws XMLStreamException {
        writer.setPrefix(binding.getPrefix(), binding.getNamespaceURI());
      }

      private void writeNamespace(NamespaceBinding binding) throws XMLStreamException {
        writer.writeNamespace(binding.getPrefix(), binding.getNamespaceURI());
      }

      private void writeAttribute(QName attrName, String attrValue) throws XMLStreamException {
        writer.writeAttribute(
                attrName.getPrefix(),
                attrName.getNamespaceURI(),
                attrName.getLocalPart(),
                attrValue);
      }

    };
  }
  
  /**
   * Creates an {@link IoReader} using the given {@link XMLStreamReader}.
   * <p>
   * This {@link IoReader} provides a method to read data in RDF/XML format and deserialise it into a {@link DocumentRoot} object.
   * </p>
   * @param xmlReader The {@link XMLStreamReader} reader to read RDF/XML data.
   * @return {@link IoReader}
   * @throws XMLStreamException  when the underlying reader raises an exception
   */
  public IoReader<QName> createIoReader(final XMLStreamReader xmlReader) throws XMLStreamException
  {
    return new IoReader<QName>() {

      @Override
      public DocumentRoot<QName> read () throws CoreIoException
      {
        try {
          while (xmlReader.hasNext())
          {
            int eventType = xmlReader.next();
            switch (eventType) {
              case  XMLEvent.START_ELEMENT:
                NamespaceBindings bindings = readBindings();
                Datatree.TopLevelDocuments<QName> topLevelDocuments= readTopLevelDocuments();
                return Datatree.DocumentRoot(bindings, topLevelDocuments);
            }
          }
        } catch (XMLStreamException e) {
          throw new CoreIoException(e);
        }
        return null;
      }

      private Datatree.NamespaceBindings readBindings() throws XMLStreamException {
        NamespaceBinding[] bindings = new NamespaceBinding[xmlReader.getNamespaceCount()];

        for(int i = 0; i < xmlReader.getNamespaceCount(); i++) {
          bindings[i] = Datatree.NamespaceBinding(xmlReader.getNamespaceURI(i), xmlReader.getNamespacePrefix(i));
        }
        return Datatree.NamespaceBindings(bindings);
      }

      /**
       * Used to store documents and properties when reading XML in the readTopLevelDocuments method
       */
      private Stack<Object> documentStack=new Stack<Object>() ;

      /**
       * Used as a buffer to read XML characters in the readTopLevelDocuments method
       */
      private StringBuilder currentText;

      /**
       * Used to store the TopLevelDocument objects in the readTopLevelDocuments method
       */
      private List<TopLevelDocument<QName>> topLevelDocuments=null;

      /**
       * Reads RDF document and returns TopLevelDocuments.
       *
       * <p>
       * Properties and
       * documents are stored in a Stack object and populated as more data
       * become available The stack object holds one TopLevelDocument at a
       * time. Once a TopLevelDocument is read it is added to the
       * topLevelDocuments collection. For triples within a
       * TopLevelDocument the following rules apply:
       * </p>
       * Starting tags:
       * <p>
       * If a triple contains rdf:about attribute it is assumed that the
       * tag is the start of a NestedDocument. An empty Nested document is
       * added to the stack.
       * If a triple contains rdf:resource, a NamedProperty with a URI
       * value is created and added to the stack.
       * Otherwise a NamedProperty without a value is added to the stack
       * </p>
       *
       * End tags:
       * <p>
       * For each end tag, an object is taken from the stack.
       * If the object is a property The property is removed from the
       * stack The XML value (if the value exists) is used to set the
       * value of that property. The property is then added to the recent
       * document in the stack. This document can be a NestedDocument or a
       * TopLevelDocument.
       * If the object is a NestedDocument, the document is removed from
       * the stack. The property identifying the document in this case is
       * the most recent object in the stack and it is also removed from
       * the stack. The NestedDocument is then added to the parent
       * document (it can be a another NestedDocument or a
       * TopLevelDocument using the property relationship. This parent
       * document is the most recent object after removing the property.
       * If the object is TopLevelDocument, the object is removed from the
       * stack and added to the topLevelDocuments collection
       * </p>
       *
       * @return
       * @throws XMLStreamException
       */
      private Datatree.TopLevelDocuments<QName> readTopLevelDocuments()
              throws XMLStreamException {
        topLevelDocuments = new ArrayList<TopLevelDocument<QName>>();
        while (xmlReader.hasNext()) {

          int eventType = xmlReader.next();
          switch (eventType) {
            case XMLEvent.START_ELEMENT:
              currentText = new StringBuilder(256);
              QName elementURI = Datatree.QName(xmlReader.getNamespaceURI(), xmlReader.getLocalName(), xmlReader.getPrefix());
              addToStack(elementURI);
              break;
            case XMLEvent.END_ELEMENT:
              String literalValue = null;
              if (currentText != null) {
                literalValue = currentText.toString();
                currentText = null;
              }
              updateDocumentInStack(literalValue);
              break;
            case XMLEvent.CHARACTERS:
              String characters = xmlReader.getText();
              if (currentText != null) {
                currentText.append(characters);
              }
              break;
          }
        }
        Datatree.TopLevelDocuments<QName> documents = Datatree
                .TopLevelDocuments(topLevelDocuments.toArray(new TopLevelDocument[topLevelDocuments.size()]));
        return documents;
        // return Datatree.<QName> TopLevelDocuments();
      }

      private void addToStack(QName elementURI) throws XMLStreamException
      {
        URI identity = null;
        URI resourceURI = null;

        int attributes = xmlReader.getAttributeCount();
        for (int i = 0; i < attributes; ++i)
        {
          if ("about".equals(xmlReader.getAttributeLocalName(i)) && "rdf".equals(xmlReader.getAttributePrefix(i)))
          {
            identity = URI.create(xmlReader.getAttributeValue(i));
          }
          if ("resource".equals(xmlReader.getAttributeLocalName(i)) && "rdf".equals(xmlReader.getAttributePrefix(i)))
          {
            resourceURI = URI.create(xmlReader.getAttributeValue(i));
          }
        }

        if (identity != null)
        {
          Datatree.NamespaceBindings bindings = readBindings();
          IdentifiableDocument<QName> document = null;
          if (documentStack.isEmpty())
          {
            document = Datatree.TopLevelDocument(bindings, elementURI, identity, null);
          }
          else
          {
            document = Datatree.NestedDocument(bindings, elementURI, identity, null);
          }
          documentStack.add(document);
        }
        else
        {
          NamedProperty<QName> property = null;
          if (resourceURI != null)
          {
            property = Datatree.NamedProperty(elementURI, resourceURI);
          }
          else
          {
            // TODO Make sure this is ok. The value type is not known yet!
            property = Datatree.NamedProperty(elementURI, "");
          }
          documentStack.add(property);
        }
      }


      private void updateDocumentInStack(String literalValue) throws XMLStreamException
      {
        // Get the object in the stack
        if (!documentStack.isEmpty())
        {
          Object stackObject = documentStack.pop();

          if (stackObject instanceof NamedProperty)
          {
            NamedProperty<QName> property = (NamedProperty<QName>) stackObject;
            // Set its property value
            if (literalValue != null && literalValue.length() > 0)
            {
              property = Datatree.NamedProperty(property.getName(), literalValue);
            }
            updateDocumentInStackWithProperty(property);
          }
          else if (stackObject instanceof NestedDocument)
          {
            NestedDocument<QName> document = (NestedDocument<QName>) stackObject;

            // Get the property for the nested document
            NamedProperty<QName> property = (NamedProperty<QName>) documentStack
                    .pop();
            property = Datatree.NamedProperty(property.getName(), document);
            updateDocumentInStackWithProperty(property);

            // Skip the ending of the property tag. The nested
            // document is attached to the parent using the property
            // already.
            while (xmlReader.hasNext())
            {
              int eventType = xmlReader.next();

              if (eventType == XMLEvent.END_ELEMENT)
              {
                String elementURI = xmlReader.getNamespaceURI() + xmlReader.getLocalName();
                if (elementURI.equals(property.getName().getNamespaceURI() + property.getName().getLocalPart()))
                {
                  break;
                }
              }
            }
          }
          else if (stackObject instanceof TopLevelDocument)
          {
            topLevelDocuments.add((TopLevelDocument<QName>) stackObject);
          }
        }
      }

      private void updateDocumentInStackWithProperty(NamedProperty<QName> property)
      {
        //Add it to the document in the stack
        IdentifiableDocument<QName> documentInStack=(IdentifiableDocument<QName>)documentStack.pop();
        documentInStack=addProperty(documentInStack, property);

        //Put the document back to the stack
        documentStack.add(documentInStack);
      }

      private IdentifiableDocument<QName> addProperty(
              IdentifiableDocument<QName> document, NamedProperty<QName> property)
      {

        List<NamedProperty<QName>> properties = new ArrayList<>();
        if (document.getProperties() == null || document.getProperties().size() == 0)
        {
          properties = Datatree.NamedProperties(property).getProperties();
        }
        else
        {
          properties.addAll(document.getProperties());
          // TODO if the Property value is a NestedDocument then add
          // the property using the same property key, still works without this though!
          properties.add(property);
        }
        NamedProperty<QName>[] propertyArray = properties.toArray(new NamedProperty[properties.size()]);
        NamedProperties<QName> namedProperties = Datatree.NamedProperties(propertyArray);
        NamespaceBindings bindings = Datatree.NamespaceBindings(
                (NamespaceBinding[]) document.getNamespaceBindings().toArray());

        if (document instanceof TopLevelDocument)
        {
          document = Datatree.TopLevelDocument(bindings,
                  document.getType(),
                  document.getIdentity(),
                  namedProperties);
        }
        else
        {
          document = Datatree.NestedDocument(
                  bindings,
                  document.getType(),
                  document.getIdentity(),
                  namedProperties);
        }

        return document;
      }

    };
  }
}
