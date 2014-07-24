package uk.ac.ncl.intbio.core.io.json;

import javax.json.stream.JsonGenerator;
import javax.xml.namespace.QName;

import uk.ac.ncl.intbio.core.datatree.Datatree;
import uk.ac.ncl.intbio.core.datatree.DocumentRoot;
import uk.ac.ncl.intbio.core.datatree.IdentifiableDocument;
import uk.ac.ncl.intbio.core.datatree.Literal;
import uk.ac.ncl.intbio.core.datatree.NamedProperty;
import uk.ac.ncl.intbio.core.datatree.NestedDocument;
import uk.ac.ncl.intbio.core.datatree.PropertyValue;
import uk.ac.ncl.intbio.core.datatree.TopLevelDocument;
import uk.ac.ncl.intbio.core.io.CoreIoException;
import uk.ac.ncl.intbio.core.io.IoWriter;
import uk.ac.ncl.intbio.core.io.rdf.RdfTerms;

public class JsonIo
{
	public IoWriter<QName> createIoWriter(final JsonGenerator writer)
	{

		return new IoWriter<QName>()
		{

			@Override
			public void write(DocumentRoot<QName> document) throws CoreIoException
			{
				writer.writeStartArray();
				for (TopLevelDocument<QName> child : document.getTopLevelDocuments())
				{
					writer.writeStartObject();
					write(child);
					writer.writeEnd();
				}
				writer.writeEnd();
			}

			private void write(IdentifiableDocument<QName, PropertyValue> doc)
			{
				writer.writeStartObject(toString(doc.getType()));
				writer.write(toString(RdfTerms.rdfAbout), doc.getIdentity().getNamespaceURI() + doc.getIdentity().getLocalPart());
				for (NamedProperty<QName, PropertyValue> property : doc.getProperties())
				{
					write(property);
				}
				writer.writeEnd();
			}

			private String toString(QName qName)
			{
				return qName.getNamespaceURI() + qName.getLocalPart();
			}

			private void write(NamedProperty<QName, PropertyValue> property)
			{
				if (property.getValue() instanceof Literal)
				{
					Literal value = (Literal) property.getValue();
					write(property.getName().getNamespaceURI() + property.getName().getLocalPart(), value);
				}
				else if (property.getValue() instanceof Datatree.NestedDocuments)
				{
					Datatree.NestedDocuments<QName> docs = (Datatree.NestedDocuments<QName>) property.getValue();
					for (NestedDocument<QName> doc : docs.getDocuments())
					{
						writer.writeStartObject(toString(property.getName()));
						write(doc);
						writer.writeEnd();
					}
				}
				else
				{
					throw new IllegalStateException("Unknown type of property value for: " + property.getValue());
				}
			}

			private void write(String key, Literal literal)
			{
				if (literal instanceof Literal.StringLiteral)
				{
					writer.write(key, ((Literal.StringLiteral) literal).getValue());
				}
				else if (literal instanceof Literal.IntegerLiteral)
				{
					writer.write(key, ((Literal.IntegerLiteral) literal).getValue().toString());
				}
				else if (literal instanceof Literal.QNameLiteral)
				{
					Literal.QNameLiteral ql = (Literal.QNameLiteral) literal;
					writer.writeStartObject(key);
					writer.write(toString(RdfTerms.rdfResource), ql.getValue().getNamespaceURI() + ql.getValue().getLocalPart());
					writer.writeEnd();
				}
				else if (literal instanceof Literal.UriLiteral)
				{
					Literal.UriLiteral ul = (Literal.UriLiteral) literal;
					writer.writeStartObject(key).write(toString(RdfTerms.rdfResource), ul.getValue().toString());
					writer.writeEnd();
				}

				else
				{
					throw new IllegalStateException("Unknown type of literal: " + literal.getClass().getName() + " extends "
							+ literal.getClass().getInterfaces()[0].getName());
				}
			}

		};
	}
}
