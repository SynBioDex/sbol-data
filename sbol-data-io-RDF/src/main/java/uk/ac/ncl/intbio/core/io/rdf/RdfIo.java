package uk.ac.ncl.intbio.core.io.rdf;

import java.io.Reader;
import java.net.URI;
import java.util.List;
import java.util.Stack;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.XMLEvent;

import uk.ac.ncl.intbio.core.datatree.*;
import uk.ac.ncl.intbio.core.datatree.Datatree.NamedProperties;
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
        writeAttribute(rdfAbout, doc.getIdentity());

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
    	} else if(property.getValue() instanceof Datatree.NestedDocuments) {
    		Datatree.NestedDocuments<QName> docs = (Datatree.NestedDocuments<QName>) property.getValue();
    		for(NestedDocument<QName> doc : docs.getDocuments()) {
                writeStartElement(property.getName());
    			write(doc);
                writer.writeEndElement();
    		}
    	} else {
    		throw new IllegalStateException("Unknown type of property value for: " + property.getValue());
    	}
      }
      
      private boolean isEmptyElementValue(Literal literal) {
    	  return literal instanceof Literal.QNameLiteral || literal instanceof Literal.UriLiteral;
      }

      private void write(Literal literal) throws XMLStreamException {
        if(literal instanceof Literal.StringLiteral) {
          writer.writeCharacters(((Literal.StringLiteral) literal).getValue());
        } else if (literal instanceof Literal.IntegerLiteral) {
        	writer.writeCharacters(((Literal.IntegerLiteral) literal).getValue().toString());
        } else if(literal instanceof Literal.QNameLiteral) {
        	Literal.QNameLiteral ql = (Literal.QNameLiteral) literal;
        	writeAttribute(rdfResource, ql.getValue());
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

      private void writeAttribute(QName attrName, QName attrValue) throws XMLStreamException {
        writer.writeAttribute(
                attrName.getPrefix(),
                attrName.getNamespaceURI(),
                attrName.getLocalPart(),
                attrValue.getNamespaceURI() + attrValue.getLocalPart());
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
						System.out.println("start:----------" + xmlReader.getName());
						Datatree.NamespaceBindings bindings = readBindings();
						Datatree.TopLevelDocuments<QName> topLevelDocuments= readTopLevelDocuments();
						
						return Datatree.DocumentRoot(bindings, topLevelDocuments, Datatree.<QName>LiteralProperties());
						//break;
					/*case XMLEvent.NAMESPACE:
						System.out.println(xmlReader.getName());*/
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
			
			private Stack<Object> documentStack=new Stack<Object>() ;
			
			private StringBuilder currentText;
			private Datatree.TopLevelDocuments<QName> readTopLevelDocuments() throws XMLStreamException {
				while (xmlReader.hasNext())
				{

					int eventType = xmlReader.next();
					switch (eventType) {
					case  XMLEvent.START_ELEMENT:
						currentText = new StringBuilder(256);
						QName elementURI=new QName(xmlReader.getNamespaceURI() + xmlReader.getLocalName());						

						QName identity=null;
						URI resourceURI=null;
						
						  int attributes = xmlReader.getAttributeCount();
		                  for(int i=0;i<attributes;++i) {
		                       
		                    	if ("about".equals(xmlReader.getAttributeLocalName(i)) && "rdf".equals(xmlReader.getAttributePrefix(i)))
		                    	{
		                    		identity= new QName(xmlReader.getAttributeValue(i));
		                    		//System.out.println("Attribute:----------" + xmlReader.getAttributeValue(i));		                    		
		                    	}
		                    	if ("resource".equals(xmlReader.getAttributeLocalName(i)) && "rdf".equals(xmlReader.getAttributePrefix(i)))
		                    	{
		                    		resourceURI= URI.create(xmlReader.getAttributeValue(i));
		                    		//System.out.println("Attribute:----------" + xmlReader.getAttributeValue(i));		                    		
		                    	}
		                    }
		                   
		                    
		                  if (identity!=null)
		                  {
		                	  Datatree.NamespaceBindings bindings = readBindings();
		                	  if (documentStack.size()==0)
								{
									TopLevelDocument<QName> document=Datatree.TopLevelDocument(bindings,elementURI, identity, null);
									documentStack.add(document);
								}
		                	  else
		                	  {
		                		  NestedDocument<QName> document=Datatree.NestedDocument(bindings,elementURI, identity, null);
		                		  documentStack.add(document);
		                	  }
		                  }
		                  else
		                  {
		                	  if (resourceURI!=null)
		                	  {
		                		  NamedProperty<QName, PropertyValue> property=Datatree.NamedProperty(elementURI, resourceURI);
		                		  documentStack.add(property);
		                	  }
		                	  else
		                	  {
		                		  NamedProperty<QName, PropertyValue> property=Datatree.NamedProperty(elementURI, "");//TODO Make sure this is ok. The value type is not known yet!
		                		  documentStack.add(property);
		                	  }
		                  }
		                  
		                    break;
					case  XMLEvent.END_ELEMENT:
						if (currentText!=null)
						{
							String literalValue=currentText.toString();
							currentText=null;
							NamedProperty<QName, PropertyValue> propertyInStack=(NamedProperty<QName, PropertyValue>)documentStack.pop();
							
							//TODO: Add a method to set the value
							NamedProperty<QName, PropertyValue> currentProperty=Datatree.NamedProperty(propertyInStack.getName(), literalValue);
							/* TODO Does not work at the moment:
							Document<QName, PropertyValue> documentInStack=(Document<QName, PropertyValue>)documentStack.pop();
							if (documentInStack instanceof TopLevelDocument)
							{
								//TODO Add a method to add a property to a document
								TopLevelDocument<QName> tldocumentInStack=(TopLevelDocument<QName>) documentInStack;
								// TODO: This line causes an exception: List<NamedProperty<QName, PropertyValue>> properties=tldocumentInStack.getProperties();
								//TODO: Open properties.add(currentProperty);
								
								//TODO This line doen not work yet TopLevelDocument currentDocument=Datatree.TopLevelDocument(tldocumentInStack.getNamespaceBindings(), tldocumentInStack.getType(), tldocumentInStack.getIdentity(), properties);
							}*/
							
							
						}

						//System.out.println("end:----------" + xmlReader.getName());
						
						break;
					case XMLEvent.CHARACTERS:
						String characters=xmlReader.getText();
						if (currentText!=null)
						{
							currentText.append(characters);
							//System.out.println("Characters:----------" + characters);
						}
						
						break;
					}
				}
				return Datatree.<QName>TopLevelDocuments();
			}
			
		};
	}
}
