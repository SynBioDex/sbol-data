package uk.ac.ncl.intbio.core.io.json;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import uk.ac.ncl.intbio.core.datatree.*;
import uk.ac.ncl.intbio.core.io.CoreIoException;
import uk.ac.ncl.intbio.core.io.IoReader;
import uk.ac.ncl.intbio.core.io.IoWriter;
import uk.ac.ncl.intbio.core.io.rdf.RdfTerms;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
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

        for (Map.Entry<String, List<PropertyValue>> properties : cholate(doc.getProperties()).entrySet())
        {
          writer.writeStartArray(properties.getKey());
          for(PropertyValue property : properties.getValue()) {
            write(property);
          }
          writer.writeEnd();
        }

        writer.writeEnd();
      }

      private void write(PropertyValue pValue)
      {
        new PropertyValue.Visitor<String>() {
          @Override
          public void visit(NestedDocument<String> v) {
            writer.writeStartObject();
            write((IdentifiableDocument<String, PropertyValue>) v);
            writer.writeEnd();
          }

          @Override
          public void visit(Literal v) {
            write(v);
          }
        }.visit(pValue);
      }

      private void write(Literal literal)
      {
        new Literal.Visitor() {
          @Override
          public void visit(Literal.StringLiteral l) {
            writer.write(l.getValue());
          }

          @Override
          public void visit(Literal.UriLiteral l) {
            writer.writeStartObject().write(
                    rdfResource,
                    l.getValue().toString());
            writer.writeEnd();
          }

          @Override
          public void visit(Literal.IntegerLiteral l) {
            writer.write(l.getValue().toString());
          }

          @Override
          public void visit(Literal.DoubleLiteral l) {
            writer.write(l.getValue().toString());
          }

          @Override
          public void visit(Literal.TypedLiteral l) {
            writer.write(l.getValue() + "^^" + l.getType().getPrefix() + ":" + l.getType().getLocalPart());
          }

          @Override
          public void visit(Literal.BooleanLiteral l) {
            writer.write(l.getValue().toString());
          }
        }.visit(literal);
      }

    };
  }

  private Map<String, List<PropertyValue>> cholate(List<NamedProperty<String, PropertyValue>> ps) {
    Map<String, List<PropertyValue>> res = new HashMap<>();

    for(NamedProperty<String, PropertyValue> np : ps) {
      List<PropertyValue> pl = res.get(np.getName());
      if(pl == null) {
        pl = new ArrayList<>();
      }
      pl.add(np.getValue());
      res.put(np.getName(), pl);
    }

    return res;
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
          if(!me.getValue().getValueType().equals(JsonValue.ValueType.OBJECT)) {
            throw new IllegalArgumentException("Expecting object, got: " + me.getValue());
          }
          for(Map.Entry<String, JsonValue> nme : ((JsonObject) me.getValue()).entrySet()) {
            if(nme.getKey().equals(rdfAbout)) {
              identity = URI.create(((JsonString) nme.getValue()).getString());
            } else {
              for(JsonValue jv : (JsonArray) nme.getValue()) {
                properties.add(Datatree.NamedProperty(nme.getKey(), readPV(jv)));
              }
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
              for(JsonValue jv : (JsonArray) nme.getValue()) {
                properties.add(Datatree.NamedProperty(nme.getKey(), readPV(jv)));
              }
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
          return readND(value);
        }
      }
    };
  }
}
