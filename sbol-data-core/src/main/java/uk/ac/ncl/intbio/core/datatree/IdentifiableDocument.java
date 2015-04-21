package uk.ac.ncl.intbio.core.datatree;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Those documents that are associated with an identifier.
 *
 * <p>
 *   These documents have a type and an identity.
 * </p>
 * <p>
 *   The type represents the data domain-specific type that this document represents.
 *   Types should be taken from a terminology, controlled vocabulary or ontology.
 * </p>
 * <p>
 *   Identifiers are globally unique identifiers for the document.
 *   Anything wishing to refer to the document can use the identifying URI.
 * </p>
 *
 * <p>
 * Identifiable documents provide named properties. The values of these properties can be literals or nested documents.
 * </p>
 *
 * @author Matthew Pocock
 * @param <N>   the property name type
 */
public interface IdentifiableDocument<N> extends Document {
  /**
   * Get the type of this document.
   *
   * @return the document type
   */
  public N getType();

  /**
   * Get the identity of this document
   *
   * @return the document identity
   */
  public URI getIdentity();

  /**
   * Get the named properties of this document.
   *
   * <p>
   *   Documents are annotated with any number of named properties. Many properties can be present for the same property
   *   name.
   * </p>
   *
   * <p>Specific sub-classses of document will refine the property type.</p>
   *
   * <p>
   *   The list should not be modified.
   * </p>
   *
   * @return a list of named properties
   */
	public List<NamedProperty<N>> getProperties();

  /**
   * Look up all property values by name.
   *
   * <p>
   *   This returns all values for property values with a matching name.
   * </p>
   *
   * @param propertyName  the name to look up
   * @return  a list (possibly empty) of all values for that name
   */
  List<PropertyValue<N>> getPropertyValues(N propertyName);

  List<String> getStringPropertyValues(N propertyName);

  String getStringPropertyValue(N propertyName);

  String getOptionalStringPropertyValue(N propertyName);

  List<URI> getUriPropertyValues(N propertyName);

  URI getUriPropertyValue(N propertyName);

  URI getOptionalUriPropertyValue(N propertyName);

  // Package private, used in Datatree only.
  static abstract class Abstract<N>  implements IdentifiableDocument<N> {
    @Override
    public List<PropertyValue<N>> getPropertyValues(N propertyName) {
      List<PropertyValue<N>> values = null;
      for(NamedProperty<N> p : getProperties()) {
        if(p.getName().equals(propertyName)) {
          if(values == null) {
            values = new ArrayList<>();
          }
          values.add(p.getValue());
        }
      }

      if(values == null) {
        values = Collections.emptyList();
      }

      return values;
    }

    public List<String> getStringPropertyValues(N propertyName) {
      final List<String> values = new ArrayList<String>();

      for(PropertyValue<N> pv: getPropertyValues(propertyName)) {
        new PropertyValue.LiteralVisitor<N>(new Literal.Visitor<N>() {
          @Override
          public void visit(Literal.StringLiteral<N> l) throws Exception {
            values.add(l.getValue());
          }
        }
        ).visit(pv);
      }

      return values;
    }

    public String getStringPropertyValue(N propertyName) {
      List<String> values = getStringPropertyValues(propertyName);

      if(values.size() == 1)
        return values.get(0);
      else throw new IllegalArgumentException(
              "Required single value property had " + values.size() + " values");
    }

    public String getOptionalStringPropertyValue(N propertyName) {
      List<String> values = getStringPropertyValues(propertyName);
      if(values.isEmpty())
        return null;
      else if (values.size() == 1)
        return values.get(0);
      else throw new IllegalArgumentException(
                "Optional property with name " + propertyName + " had " + values.size() + " values");
    }

    public List<URI> getUriPropertyValues(N propertyName) {
      final List<URI> values = new ArrayList<URI>();

      for(PropertyValue<N> pv: getPropertyValues(propertyName)) {
        new PropertyValue.LiteralVisitor<N>(new Literal.Visitor<N>() {
          @Override
          public void visit(Literal.UriLiteral<N> l) throws Exception {
            values.add(l.getValue());
          }
        }
        ).visit(pv);
      }

      return values;
    }

    public URI getUriPropertyValue(N propertyName) {
      List<URI> values = getUriPropertyValues(propertyName);

      if(values.size() == 1)
        return values.get(0);
      else throw new IllegalArgumentException(
              "Required single value property had " + values.size() + " values");
    }

    @Override
    public URI getOptionalUriPropertyValue(N propertyName) {
      List<URI> values = getUriPropertyValues(propertyName);
      if(values.isEmpty())
        return null;
      else if (values.size() == 1)
        return values.get(0);
      else throw new IllegalArgumentException(
                "Optional property with name " + propertyName + " had " + values.size() + " values");
    }
  }



}
