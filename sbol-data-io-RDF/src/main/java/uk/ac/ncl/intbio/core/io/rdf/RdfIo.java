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

public class RdfIo{

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
          writeNamespace(rdf);//TODO: Don't do if rdf is already in the list

          for(NamespaceBinding nb : document.getNamespaceBindings()) {
            setPrefix(nb);
            writeNamespace(nb);
          }

          for (NamedProperty<QName, Literal> properties : document.getProperties()) {
            // todo: we do nothing with these right now
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

      private void write(IdentifiableDocument<QName, PropertyValue> doc) throws XMLStreamException {
        writeStartElement(doc.getType());
        writeAttribute(rdfAbout, doc.getIdentity().toString());

        for (NamedProperty<QName, PropertyValue> property : doc.getProperties()) {
          write(property);
        }

        writer.writeEndElement();
      }

      private void write(NamedProperty<QName, PropertyValue> property) throws XMLStreamException {
        if(property.getValue() instanceof Literal) {
          Literal value = (Literal) property.getValue();
          if(isEmptyElementValue(value)) {
            writeEmptyElement(property.getName());
            write(value);
          } else {
            writeStartElement(property.getName());
            write(value);
            writer.writeEndElement();
          }
        } else if(property.getValue() instanceof NestedDocument) {
          NestedDocument<QName> doc = (NestedDocument<QName>) property.getValue();
          writeStartElement(property.getName());
          write(doc);
          writer.writeEndElement();
        } else {
          throw new IllegalStateException("Unknown type of property value for: " + property.getValue());
        }
      }

      private boolean isEmptyElementValue(Literal literal) {
        return /* literal instanceof Literal.QNameLiteral || */ literal instanceof Literal.UriLiteral;
      }

      private void write(Literal literal) throws XMLStreamException {
        if(literal instanceof Literal.StringLiteral) {
          writer.writeCharacters(((Literal.StringLiteral) literal).getValue());
        } else if (literal instanceof Literal.IntegerLiteral) {
          writer.writeCharacters(((Literal.IntegerLiteral) literal).getValue().toString());
        }
        else if(literal instanceof Literal.UriLiteral) {
          Literal.UriLiteral ul = (Literal.UriLiteral) literal;
          writeAttribute(rdfResource, ul.getValue().toString());
        }

        else {
          throw new IllegalStateException("Unknown type of literal: " + literal.getClass().getName() +
                  " extends " + literal.getClass().getInterfaces()[0].getName());
        }
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

  public IoReader<QName> createIoReader(final XMLStreamReader xmlReader) throws XMLStreamException
  {
    return new IoReader<QName>() {

      @Override
      public DocumentRoot<QName> read () throws XMLStreamException
      {
        while (xmlReader.hasNext())
        {
          int eventType = xmlReader.next();
          switch (eventType) {
            case  XMLEvent.START_ELEMENT:
              Datatree.NamespaceBindings bindings = readBindings();
              Datatree.TopLevelDocuments<QName> topLevelDocuments= readTopLevelDocuments();
              return Datatree.DocumentRoot(bindings, topLevelDocuments, Datatree.<QName>LiteralProperties());
          }
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
       * Reads RDF document and returns TopLevelDocuments. Properties and
       * documents are stored in a Stack object and populated as more data
       * become available The stack object holds one TopLevelDocument at a
       * time. Once a TopLevelDocument is read it is added to the
       * topLevelDocuments collection. For triples within a
       * TopLevelDocument the following rules apply:<br/>
       * Starting tags:
       * <p>
       * <br/>
       * If a triple contains rdf:about attribute it is assumed that the
       * tag is the start of a NestedDocument. An empty Nested document is
       * added to the stack <br/>
       * If a triple contains rdf:resource, a NamedProperty with a URI
       * value is created and added to the stack <br/>
       * Otherwise a NamedProperty without a value is added to the stack
       * </p>
       * <br/>
       * End tags:
       * <p>
       * For each end tag, an object is taken from the stack. <br/>
       * If the object is a property The property is removed from the
       * stack The XML value (if the value exists) is used to set the
       * value of that property. The property is then added to the recent
       * document in the stack. This document can be a NestedDocument or a
       * TopLevelDocument <br/>
       * If the object is a NestedDocument, the document is removed from
       * the stack. The property identifying the document in this case is
       * the most recent object in the stack and it is also removed from
       * the stack. The NestedDocument is then added to the parent
       * document (it can be a another NestedDocument or a
       * TopLevelDocument using the property relationship. This parent
       * document is the most recent object after removing the property.<br/>
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
          IdentifiableDocument<QName, PropertyValue> document = null;
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
          NamedProperty<QName, PropertyValue> property = null;
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
            NamedProperty<QName, PropertyValue> property = (NamedProperty<QName, PropertyValue>) stackObject;
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
            NamedProperty<QName, PropertyValue> property = (NamedProperty<QName, PropertyValue>) documentStack
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

      private void updateDocumentInStackWithProperty(NamedProperty<QName, PropertyValue> property)
      {
        //Add it to the document in the stack
        IdentifiableDocument<QName, PropertyValue> documentInStack=(IdentifiableDocument<QName, PropertyValue>)documentStack.pop();
        documentInStack=addProperty(documentInStack, property);

        //Put the document back to the stack
        documentStack.add(documentInStack);
      }

      private IdentifiableDocument<QName, PropertyValue> addProperty(
              IdentifiableDocument<QName, PropertyValue> document, NamedProperty<QName, PropertyValue> property)
      {

        List<NamedProperty<QName, PropertyValue>> properties = new ArrayList<NamedProperty<QName, PropertyValue>>();
        if (document.getProperties() == null || document.getProperties().size() == 0)
        {
          properties = Datatree.NamedProperties(property).getProperties();
        }
        else
        {
          properties.addAll(document.getProperties());
          // TODO if the Property value is a NestedDocument then add
          // the property using the same property key, still works without this though!
          properties.add((NamedProperty<QName, PropertyValue>) property);
        }
        NamedProperty<QName, PropertyValue>[] propertyArray = properties.toArray(new NamedProperty[properties.size()]);
        NamedProperties<QName, PropertyValue> namedProperties = Datatree.NamedProperties(propertyArray);
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
