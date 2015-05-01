package uk.ac.ncl.intbio.core.datatree;

import java.net.URI;
import java.util.*;

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

  Properties<N> properties();

  public interface Properties<N> {

    public List<NamedProperty<N>> excluding(N ... names);
    public List<NamedProperty<N>> excluding(Collection<N> names);

    TypedProperty<N, String> string();
    TypedProperty<N, URI> uri();
    TypedProperty<N, NestedDocument<N>> nestedDocument();
  }

  public abstract class TypedProperty<N, T> {
    public abstract List<T> getValues(N propertyName);

    public final T getValue(N propertyName) {
      List<T> values = getValues(propertyName);

      if (values.size() == 1)
        return values.get(0);
      else throw new IllegalArgumentException(
              "Required single value property had " + values.size() + " values");
    }

    public final T getOptionalValue(N propertyName) {
      List<T> values = getValues(propertyName);
      if (values.isEmpty())
        return null;
      else if (values.size() == 1)
        return values.get(0);
      else throw new IllegalArgumentException(
                "Optional property with name " + propertyName + " had " + values.size() + " values");
    }
  }

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

    public Properties<N> properties() {
      return new Properties<N>() {
        @Override
        public List<NamedProperty<N>> excluding(N... names) {
          final Set<N> nameSet = new HashSet<>(Arrays.asList(names));
          return excluding(nameSet);
        }

        @Override
        public List<NamedProperty<N>> excluding(Collection<N> names) {
          final List<NamedProperty<N>> props = new ArrayList<>();

          for(NamedProperty<N> np : getProperties()) {
            if(!names.contains(np.getName())) {
              props.add(np);
            }
          }

          return props;
        }

        @Override
        public TypedProperty<N, String> string() {
          return new TypedProperty<N, String>() {
            @Override
            public List<String> getValues(N propertyName) {
              final List<String> values = new ArrayList<>();

              for(PropertyValue<N> pv: getPropertyValues(propertyName)) {
                new PropertyValue.LiteralVisitor<>(new Literal.Visitor<N>() {
                  @Override
                  public void visit(Literal.StringLiteral<N> l) throws Exception {
                    values.add(l.getValue());
                  }
                }
                ).visit(pv);
              }

              return values;
            }
          };
        }

        @Override
        public TypedProperty<N, URI> uri() {
          return new TypedProperty<N, URI>() {
            @Override
            public List<URI> getValues(N propertyName) {
              final List<URI> values = new ArrayList<>();

              for(PropertyValue<N> pv: getPropertyValues(propertyName)) {
                new PropertyValue.LiteralVisitor<>(new Literal.Visitor<N>() {
                  @Override
                  public void visit(Literal.UriLiteral<N> l) throws Exception {
                    values.add(l.getValue());
                  }
                }
                ).visit(pv);
              }

              return values;
            }
          };
        }

        @Override
        public TypedProperty<N, NestedDocument<N>> nestedDocument() {
          return new TypedProperty<N, NestedDocument<N>>() {
            @Override
            public List<NestedDocument<N>> getValues(N propertyName) {
              final List<NestedDocument<N>> values = new ArrayList<>();

              for(PropertyValue<N> pv: getPropertyValues(propertyName)) {
                new PropertyValue.NestedDocumentVisitor<N>() {
                  @Override
                  public void visit(NestedDocument<N> v) throws Exception {
                    values.add(v);
                  }
                }.visit(pv);
              }

              return values;
            }
          };
        }
      };
    };
  }

}
