package uk.ac.ncl.intbio.core.io.json;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import javax.xml.stream.XMLStreamException;

import uk.ac.ncl.intbio.core.datatree.*;
import uk.ac.ncl.intbio.core.io.CoreIoException;
import uk.ac.ncl.intbio.core.io.IoReader;
import uk.ac.ncl.intbio.core.io.IoWriter;
import uk.ac.ncl.intbio.core.io.rdf.RdfTerms;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonIo
{
  private String rdfAbout = StringifyQName.qname2string.transformName(RdfTerms.rdfAbout);
  private String rdfResource = StringifyQName.qname2string.transformName(RdfTerms.rdfResource);

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
				writer.write(rdfAbout, doc.getIdentity().toString());
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
        switch (json.getValueType()) {
          case ARRAY:
            return Datatree.DocumentRoot(
                    Datatree.TopLevelDocuments(readTLDs((JsonArray) json)),
                    Datatree.<String>LiteralProperties());
          default:
            throw new IllegalArgumentException("Expecting json Array. Got " + json.getValueType());
        }
      }

      public List<TopLevelDocument<String>> readTLDs(JsonArray jsonArray) {
        List<TopLevelDocument<String>> docs = new ArrayList<>(jsonArray.size());
        for(JsonObject object : jsonArray.getValuesAs(JsonObject.class)) {
          docs.add(readTLD(object));
        }
        return docs;
      }

      public TopLevelDocument<String> readTLD(JsonObject object) {
        String type = null;
        URI identity = null;
        List<NamedProperty<String, PropertyValue>> properties = new ArrayList<>();

        for(Map.Entry<String, JsonValue> me : object.entrySet()) {
          type = me.getKey();
          for(Map.Entry<String, JsonValue> nme : ((JsonObject) me.getValue()).entrySet()) {
            if(nme.getKey().equals(rdfAbout)) {
              identity = URI.create(((JsonString) nme.getValue()).getString());
            } else {
              properties.add(Datatree.NamedProperty(nme.getKey(), readPV(nme.getValue())));
            }
          }
        }

        return Datatree.TopLevelDocument(
                type,
                identity,
                Datatree.NamedProperties(properties));
      }

      public NestedDocument<String> readND(JsonObject object) {
        // todo: refactor TopLevelDocument and NestedDocument to share code
        String type = null;
        URI identity = null;
        List<NamedProperty<String, PropertyValue>> properties = new ArrayList<>();

        for(Map.Entry<String, JsonValue> me : object.entrySet()) {
          type = me.getKey();
          if(!me.getValue().getValueType().equals(JsonValue.ValueType.OBJECT)) {
            throw new IllegalArgumentException("Expecting object, got: " + me.getValue());
          }
          for(Map.Entry<String, JsonValue> nme : ((JsonObject) me.getValue()).entrySet()) {
            if(nme.getKey().equals(rdfAbout)) {
              identity = URI.create(((JsonString) nme.getValue()).getString());
            } else {
              properties.add(Datatree.NamedProperty(nme.getKey(), readPV(nme.getValue())));
            }
          }
        }

        return Datatree.NestedDocument(
                type,
                identity,
                Datatree.NamedProperties(properties));
      }

      public PropertyValue readPV(JsonValue value) {
        switch(value.getValueType()) {
          case ARRAY:
            throw new IllegalArgumentException("Can't process array at this position");
          case TRUE:
            return Datatree.Literal(true);
          case FALSE:
            return Datatree.Literal(false);
          case NUMBER:
            // fixme: add cases for various numeric types
            return Datatree.Literal(((JsonNumber) value).intValue());
          case STRING:
            return Datatree.Literal(((JsonString) value).getString());
          case NULL:
            throw new IllegalArgumentException("Can't process NULL at this position");
          case OBJECT:
            return readNdOrUri((JsonObject) value);
          default:
            throw new IllegalArgumentException("Unable to process value " + value);
        }
      }

      public PropertyValue readNdOrUri(JsonObject value) {
        JsonValue res = value.get(rdfResource);
        if(res != null) {
          return Datatree.Literal(URI.create(((JsonString) res).getString()));
        } else {
          return Datatree.NestedDocuments(readND(value));
        }
      }
    };
  }
}
