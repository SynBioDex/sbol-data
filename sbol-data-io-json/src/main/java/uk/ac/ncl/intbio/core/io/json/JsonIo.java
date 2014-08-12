package uk.ac.ncl.intbio.core.io.json;

import javax.json.JsonStructure;
import javax.json.stream.JsonGenerator;
import javax.xml.stream.XMLStreamException;

import uk.ac.ncl.intbio.core.datatree.Datatree;
import uk.ac.ncl.intbio.core.datatree.DocumentRoot;
import uk.ac.ncl.intbio.core.datatree.IdentifiableDocument;
import uk.ac.ncl.intbio.core.datatree.Literal;
import uk.ac.ncl.intbio.core.datatree.NamedProperty;
import uk.ac.ncl.intbio.core.datatree.NestedDocument;
import uk.ac.ncl.intbio.core.datatree.PropertyValue;
import uk.ac.ncl.intbio.core.datatree.TopLevelDocument;
import uk.ac.ncl.intbio.core.io.CoreIoException;
import uk.ac.ncl.intbio.core.io.IoReader;
import uk.ac.ncl.intbio.core.io.IoWriter;
import uk.ac.ncl.intbio.core.io.rdf.RdfTerms;

public class JsonIo
{
  private String rdfAbout = StringifyQName.INSTANCE.transformName(RdfTerms.rdfAbout);
  private String rdfResource = StringifyQName.INSTANCE.transformName(RdfTerms.rdfResource);

  public String getRdfAbout() {
    return rdfAbout;
  }

  public void setRdfAbout(String rdfAbout) {
    this.rdfAbout = rdfAbout;
  }

  public String getRdfResource() {
    return rdfResource;
  }

  public void setRdfResource(String rdfResource) {
    this.rdfResource = rdfResource;
  }

	public IoWriter<String> createIoWriter(final JsonGenerator writer)
	{
		return new IoWriter<String>()
		{

      @Override
			public void write(DocumentRoot<String> document) throws CoreIoException
			{
				writer.writeStartArray();
				for (TopLevelDocument<String> child : document.getTopLevelDocuments())
				{
					writer.writeStartObject();
					write(child);
					writer.writeEnd();
				}
				writer.writeEnd();
			}

			private void write(IdentifiableDocument<String, PropertyValue> doc)
			{
				writer.writeStartObject(doc.getType());
				writer.write(rdfAbout, doc.getIdentity());
				for (NamedProperty<String, PropertyValue> property : doc.getProperties())
				{
					write(property);
				}
				writer.writeEnd();
			}

			private void write(NamedProperty<String, PropertyValue> property)
			{
				if (property.getValue() instanceof Literal)
				{
					Literal value = (Literal) property.getValue();
					write(property.getName(), value);
				}
				else if (property.getValue() instanceof Datatree.NestedDocuments)
				{
					Datatree.NestedDocuments<String> docs = (Datatree.NestedDocuments<String>) property.getValue();
					for (NestedDocument<String> doc : docs.getDocuments())
					{
						writer.writeStartObject(property.getName());
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
					writer.write(
                  rdfResource,
                  StringifyQName.INSTANCE.transformName(ql.getValue())); // todo: do something with this
					writer.writeEnd();
				}
				else if (literal instanceof Literal.UriLiteral)
				{
					Literal.UriLiteral ul = (Literal.UriLiteral) literal;
					writer.writeStartObject(key).write(
                  rdfResource,
                  ul.getValue().toString());
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

  public IoReader<String> createIoReader(final JsonStructure json)
  {
    return new IoReader<String>() {
      @Override
      public DocumentRoot<String> read() throws XMLStreamException {
        return null;
      }
    };
  }
}
