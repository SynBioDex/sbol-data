package uk.ac.ncl.intbio.core.io.rdf;

import javax.lang.model.element.Name;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import uk.ac.ncl.intbio.core.datatree.*;
import uk.ac.ncl.intbio.core.io.CoreIoException;
import uk.ac.ncl.intbio.core.io.IoWriter;

public class RdfIo{
  public static final NamespaceBinding rdf = new NamespaceBinding("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "rdf");
  public static final QName RDF = rdf.withLocalPart("RDF");
  public static final QName rdfResource = rdf.withLocalPart("resource");

  public static final NamespaceBinding sbol2 = new NamespaceBinding("http://sbols.org/v2#", "sbol2");
  public static final QName dnaComponent = sbol2.withLocalPart("DnaComponent");
  public static final QName name = sbol2.withLocalPart("name");

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
					writeNamespace(rdf);
					
					setPrefix(sbol2);
					writeNamespace(sbol2);

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
        writeAttribute(rdfResource, doc.getIdentity());

        for (NamedProperty<QName, PropertyValue> property : doc.getProperties()) {
          write(property);
        }

        writer.writeEndElement();
			}

      private void write(NamedProperty<QName, PropertyValue> property) throws XMLStreamException {
        writeStartElement(property.getName());
        write(property.getValue());
        writer.writeEndElement();
      }

      private void write(PropertyValue value) throws XMLStreamException {
        if(value instanceof Literal) {
          write((Literal) value);
        } else if(value instanceof NestedDocument) {
          write((IdentifiableDocument<QName, PropertyValue>) value);
        } else {
          throw new IllegalStateException("Unknown type of property value: " + value);
        }
      }

      private void write(Literal literal) throws XMLStreamException {
        if(literal instanceof Literal.StringLiteral) {
          writer.writeCharacters(((Literal.StringLiteral) literal).getValue());
        } else {
          throw new IllegalStateException("Unknown type of literal: " + literal);
        }
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

      private void writeAttribute(QName attrName, QName attrValue) throws XMLStreamException {
        writer.writeAttribute(
                attrName.getPrefix(),
                attrName.getNamespaceURI(),
                attrName.getLocalPart(),
                attrValue.getPrefix() + ":" + attrValue.getLocalPart());
      }
		};
	}
}
