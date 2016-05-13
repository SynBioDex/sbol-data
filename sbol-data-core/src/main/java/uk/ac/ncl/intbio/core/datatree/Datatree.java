package uk.ac.ncl.intbio.core.datatree;

import javax.xml.namespace.QName;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Static utilities for working with the datatree types.
 *
 * <p>
 *   This provides factory methods for the creation of instances of all datatree types.
 *   It is designed to be statically imported and then used as if it were constructors, but without the "new" keyword.
 * </p>
 *
 * <p>
 *   In addition to the primary datatree types, this class has several nested types that provide type-safe
 *   representations of collections.
 *   This enables a very natural syntax for declaratively expressing nested data values.
 * </p>
 *
 * <p>
 *   Example use:
 * </p>
 * <pre><code>
 * import static uk.ac.ncl.intbio.core.datatree.Datatree.*;
 *
 * NamespaceBinding foaf = NamespaceBinding("http://xmlns.com/foaf/0.1/", "foaf");
 *
 * NamedProperty spidermanName = NamedProperty(foaf.withLocalPart("name"), "Peter Parker");
 * </code></pre>
 *
 * @author Matthew Pocock
 */
public final class Datatree
{
  // prevent instantiation
  private Datatree() {}

  /**
   * A list of top-level documents.
   *
   * <p>
   *   Used by:
   * </p>
   * <ul>
   *   <li>{@link #DocumentRoot(uk.ac.ncl.intbio.core.datatree.Datatree.NamespaceBindings, uk.ac.ncl.intbio.core.datatree.Datatree.TopLevelDocuments)}</li>
   *   <li>{@link #DocumentRoot(uk.ac.ncl.intbio.core.datatree.Datatree.TopLevelDocuments)}</li>
   * </ul>
   *
   * @author Matthew Pocock
   * @param <N>  the name type of the documents
   */
  public interface TopLevelDocuments<N> {
    /**
     * Get the wrapped documents.
     *
     * @return the wrapped documents.
     */
    List<TopLevelDocument<N>> getDocuments();

    class Impl<N> implements TopLevelDocuments<N> {
      private final List<TopLevelDocument<N>> documents;

      Impl(List<TopLevelDocument<N>> documents) {
        if (documents == null)
          throw new NullPointerException("");
        this.documents = documents;
      }

      @Override
      public List<TopLevelDocument<N>> getDocuments() {
        return documents;
      }

      @Override
      public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Impl impl = (Impl) o;

        return documents.equals(impl.documents);

      }

      @Override
      public int hashCode() {
        return documents.hashCode();
      }


      @Override
      public String toString() {
        return "TopLevelDocuments{" +
                "documents=" + documents +
                '}';
      }
    }
  }

  /**
   * Factory for {@link TopLevelDocuments}.
   *
   * <p>
   *   Used by the {@link DocumentRoot} factory methods.
   * </p>
   *
   * @param documents   the documents to wrap
   * @param <N>         the property name type
   * @return  a new TopLevelDocuments that wraps up documents
   */
  @SafeVarargs
  public static <N> TopLevelDocuments<N> TopLevelDocuments(TopLevelDocument<N> ... documents) {
    return TopLevelDocuments(Arrays.asList(documents));
  }

  /**
   * Factory for {@link TopLevelDocuments}.
   *
   * <p>
   *   Used by the {@link DocumentRoot} factory methods.
   * </p>
   *
   * @param documents   the documents to wrap
   * @param <N>         the property name type
   * @return  a new TopLevelDocuments that wraps up documents
   */
  public static <N> TopLevelDocuments<N> TopLevelDocuments(final List<TopLevelDocument<N>> documents) {
    return new TopLevelDocuments.Impl<>(documents);
  }

  /**
   * A list of {@link NamespaceBinding}s.
   *
   * <p>
   *   Used by:
   * </p>
   * <ul>
   *   <li>{@link #TopLevelDocument(uk.ac.ncl.intbio.core.datatree.Datatree.NamespaceBindings, Object, java.net.URI, uk.ac.ncl.intbio.core.datatree.Datatree.NamedProperties)}</li>
   *   <li>{@link #NestedDocument(uk.ac.ncl.intbio.core.datatree.Datatree.NamespaceBindings, Object, java.net.URI, uk.ac.ncl.intbio.core.datatree.Datatree.NamedProperties)}</li>
   * </ul>
   *
   * @author Matthew Pocock
   */
  public interface NamespaceBindings {
    List<NamespaceBinding> getBindings();

    class Impl implements NamespaceBindings {
      private final List<NamespaceBinding> bindings;

      Impl(List<NamespaceBinding> bindings) {
        if(bindings == null)
          throw new NullPointerException("Can't create a NamespaceBinding instance with null bindings");
        this.bindings = bindings;
      }

      @Override
      public List<NamespaceBinding> getBindings() {
        return bindings;
      }

      @Override
      public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Impl impl = (Impl) o;

        return bindings.equals(impl.bindings);

      }

      @Override
      public int hashCode() {
        return bindings.hashCode();
      }

      @Override
      public String toString() {
        return "NamespaceBindings{" +
                "bindings=" + bindings +
                '}';
      }
    }

  }

  /**
   * Factory for {@link NamespaceBindings}.
   *
   * <p>
   *   Used by {@link uk.ac.ncl.intbio.core.datatree.TopLevelDocument} and {@link uk.ac.ncl.intbio.core.datatree.NestedDocument}
   *   factory methods.
   * </p>
   *
   * @param bindings  list of bindings
   * @return    a new NamespaceBindnigs wrapping the bindings list
   * @throws NullPointerException if bindings is null
   */
  public static NamespaceBindings NamespaceBindings(final List<NamespaceBinding> bindings) {
    return new NamespaceBindings.Impl(bindings);
  }

  /**
   * Factory for {@link NamespaceBindings}.
   *
   * <p>
   *   Used by {@link uk.ac.ncl.intbio.core.datatree.TopLevelDocument} and {@link uk.ac.ncl.intbio.core.datatree.NestedDocument}
   *   factory methods.
   * </p>
   *
   * @param bindings  bindings
   * @return    a new NamespaceBindnigs wrapping the bindings
   */
  public static NamespaceBindings NamespaceBindings(final NamespaceBinding ...bindings) {
    return NamespaceBindings(Arrays.asList(bindings));
  }

  /**
   * A list of {@link NamedProperty}s.
   *
   * <p>Used by</p>
   * <ul>
   *   <li>{@link #TopLevelDocument(Object, java.net.URI, uk.ac.ncl.intbio.core.datatree.Datatree.NamedProperties)}</li>
   *   <li>{@link #TopLevelDocument(uk.ac.ncl.intbio.core.datatree.Datatree.NamespaceBindings, Object, java.net.URI, uk.ac.ncl.intbio.core.datatree.Datatree.NamedProperties)}</li>
   *   <li>{@link #NestedDocument(Object, java.net.URI, uk.ac.ncl.intbio.core.datatree.Datatree.NamedProperties)}</li>
   *   <li>{@link #NestedDocument(uk.ac.ncl.intbio.core.datatree.Datatree.NamespaceBindings, Object, java.net.URI, uk.ac.ncl.intbio.core.datatree.Datatree.NamedProperties)}</li>
   * </ul>
   *
   * <p>
   *   The P parameter is used to restrict the property value range to either all possible values, or to literals only.
   * </p>
   *
   * @author Matthew Pocock
   * @param <N>   the property name type
   */
  public interface NamedProperties<N> {
    List<NamedProperty<N>> getProperties();

    class Impl<N> implements NamedProperties<N> {
        private final List<NamedProperty<N>> properties;

        Impl(List<NamedProperty<N>> properties) {
          if(properties == null)
            throw new NullPointerException("Can't create a NamedProperties with null properties");
          this.properties = properties;
        }

        @Override
        public List<NamedProperty<N>> getProperties() {
          return properties;
        }

      @Override
      public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Impl<?> impl = (Impl<?>) o;

        return properties.equals(impl.properties);

      }

      @Override
      public int hashCode() {
        return properties.hashCode();
      }

      @Override
      public String toString() {
        return "NamedProperties{" +
                "properties=" + properties +
                '}';
      }
    }
  }

  /**
   * Factory for {@link NamedProperties}.
   *
   * <p>
   *   Used by {@link uk.ac.ncl.intbio.core.datatree.NestedDocument}
   *   factory methods.
   * </p>
   *
   * @param properties  properties
   * @return    a new NamedProperties wrapping the properties
   */
  @SafeVarargs
  public static <N> NamedProperties<N> NamedProperties(final NamedProperty<N> ... properties) {
    return NamedProperties(Arrays.asList(properties));
  }

  /**
   * Factory for {@link NamedProperties}.
   *
   * <p>
   *   Used by {@link uk.ac.ncl.intbio.core.datatree.NestedDocument}
   *   factory methods.
   * </p>
   *
   * @param properties  properties list
   * @return    a new NamedProperties wrapping the properties
   */
  public static <N> NamedProperties<N> NamedProperties(final List<NamedProperty<N>> properties) {
    return new NamedProperties.Impl<>(properties);
  }

  /**
   * Factory for {@link TopLevelDocument}.
   *
   * <p>
   *   This builds a new TopLevelDocument using the supplied data.
   * </p>
   *
   * @param type        the type of the document
   * @param identity    the identity of the document
   * @param properties  the property list of the document
   * @param <N>         the property name type
   * @return  a new TopLevelDocument instance
   */
  public static <N> TopLevelDocument<N> TopLevelDocument(final N type,
                                                         final URI identity,
                                                         final NamedProperties<N> properties) {
    return TopLevelDocument(NamespaceBindings(), type, identity, properties);
  }

  /**
   * Factory for {@link TopLevelDocument}.
   *
   * <p>
   *   This builds a new TopLevelDocument using the supplied data.
   * </p>
   *
   * @param bindings    the namespace bindings
   * @param type        the type of the document
   * @param identity    the identity of the document
   * @param <N>         the property name type
   * @return  a new TopLevelDocument instance
   */
  public static <N> TopLevelDocument<N> TopLevelDocument(final NamespaceBindings bindings,
                                                         final N type,
                                                         final URI identity) {
    return TopLevelDocument(bindings, type, identity, Datatree.<N>NamedProperties());
  }

  /**
   * Factory for {@link TopLevelDocument}.
   *
   * <p>
   *   This builds a new TopLevelDocument using the supplied data.
   * </p>
   *
   * @param bindings    the namespace bindings
   * @param type        the type of the document
   * @param identity    the identity of the document
   * @param properties  the property list of the document
   * @param <N>         the property name type
   * @return  a new TopLevelDocument instance
   */
  public static <N> TopLevelDocument<N> TopLevelDocument(final NamespaceBindings bindings,
                                                         final N type,
                                                         final URI identity,
                                                         final NamedProperties<N> properties) {
    return new TopLevelDocument.Impl<>(
            bindings.getBindings(),
            type,
            identity,
            properties.getProperties());
  }

  /**
   * Factory for {@link NestedDocument}.
   *
   * <p>
   * This builds a new NestedDocument using the supplied data.
   * </p>
   *
   * @param type        the type of the document
   * @param identity    the identity of the document
   * @param properties  the property list of the document
   * @param <N>         the property name type
   * @return  a new TopLevelDocument instance
   */
  public static <N> NestedDocument<N> NestedDocument(final N type,
                                                     final URI identity,
                                                     final NamedProperties<N> properties) {
    return NestedDocument(NamespaceBindings(), type, identity, properties);
  }

  /**
   * Factory for {@link NestedDocument}.
   *
   * <p>
   * This builds a new NestedDocument using the supplied data.
   * </p>
   *
   * @param bindings    the namespace bindings
   * @param type        the type of the document
   * @param identity    the identity of the document
   * @param <N>         the property name type
   * @return  a new TopLevelDocument instance
   */
  public static <N> NestedDocument<N> NestedDocument(final NamespaceBindings bindings,
                                                     final N type,
                                                     final URI identity)
  {
    return NestedDocument(bindings, type, identity, Datatree.<N>NamedProperties());
  }

  /**
   * Factory for {@link NestedDocument}.
   *
   * <p>
   * This builds a new NestedDocument using the supplied data.
   * </p>
   *
   * @param bindings    the namespace bindings
   * @param type        the type of the document
   * @param identity    the identity of the document
   * @param properties  the property list of the document
   * @param <N>         the property name type
   * @return  a new TopLevelDocument instance
   */
  public static <N> NestedDocument<N> NestedDocument(final NamespaceBindings bindings,
                                                     final N type,
                                                     final URI identity,
                                                     final NamedProperties<N> properties)
  {
    return new NestedDocument.Impl<>(
            bindings.getBindings(),
            type,
            identity,
            properties.getProperties());
  }

  /**
   * Factory for {@link DocumentRoot}.
   *
   * <p>
   *   This builds a new DocumentRoot instance using the supplied data.
   * </p>
   *
   * @param documents   the top-level documents
   * @param <N>         the property name type
   * @return  a new DocumentRoot instance
   */
  public static <N> DocumentRoot<N> DocumentRoot(final TopLevelDocuments<N> documents) {
    return DocumentRoot(NamespaceBindings(), documents);
  }

  /**
   * Factory for {@link DocumentRoot}.
   *
   * <p>
   *   This builds a new DocumentRoot instance using the supplied data.
   * </p>
   *
   * @param bindings    the namespace bindings
   * @param documents   the top-level documents
   * @param <N>         the property name type
   * @return  a new DocumentRoot instance
   */
  public static <N> DocumentRoot<N> DocumentRoot(
          final NamespaceBindings bindings,
          final TopLevelDocuments<N> documents) {
    class DR implements DocumentRoot<N> {
      @Override
      public List<TopLevelDocument<N>> getTopLevelDocuments() {
        return documents.getDocuments();
      }

      @Override
      public List<NamespaceBinding> getNamespaceBindings() {
        return bindings.getBindings();
      }
    }

    return new DR();
  }

  /**
   * Create a value property from a literal value.
   *
   * @param name    the property name
   * @param value   the property value
   * @param <N>     the property name type
   * @return  a new NamedProperty with the supplied name and value
   */
  public static <N> NamedProperty<N> NamedProperty(final N name, final PropertyValue<N> value) {
    return new NamedProperty<N>() {
      @Override
      public PropertyValue<N> getValue() {
        return value;
      }

      @Override
      public N getName() {
        return name;
      }
    };
  }

  /**
   * Create a value property from a string value.
   *
   * @param name    the property name
   * @param value   the property value
   * @param <N>     the property name type
   * @return  a new NamedProperty with the supplied name and value
   */
  public static <N> NamedProperty<N> NamedProperty(final N name, final String value) {
    return NamedProperty(name, Datatree.<N>Literal(value));
  }

  /**
   * Create a value property from an int value.
   *
   * @param name    the property name
   * @param value   the property value
   * @param <N>     the property name type
   * @return  a new NamedProperty with the supplied name and value
   */
  public static <N> NamedProperty<N> NamedProperty(final N name, final int value) {
    return NamedProperty(name, Datatree.<N>Literal(value));
  }

  /**
   * Create a value property from a URI value.
   *
   * @param name    the property name
   * @param value   the property value
   * @param <N>     the property name type
   * @return  a new NamedProperty with the supplied name and value
   */
  public static <N> NamedProperty<N> NamedProperty(final N name, final URI value) {
    return NamedProperty(name, Datatree.<N>Literal(value));
  }
  
  /**
   * Create a value property from a Double value.
   *
   * @param name    the property name
   * @param value   the property value
   * @param <N>     the property name type
   * @return  a new NamedProperty with the supplied name and value
   */
  public static <N> NamedProperty<N> NamedProperty(final N name, final Double value) {
    return NamedProperty(name, Datatree.<N>Literal(value));
  }
  
  /**
   * Create a value property from a Boolean value.
   *
   * @param name    the property name
   * @param value   the property value
   * @param <N>     the property name type
   * @return  a new NamedProperty with the supplied name and value
   */
  public static <N> NamedProperty<N> NamedProperty(final N name, final Boolean value) {
    return NamedProperty(name, Datatree.<N>Literal(value));
  }

  /**
   * Create a value property from a nested document value.
   *
   * @param name    the property name
   * @param value   the property value
   * @param <N>     the property name type
   * @return  a new NamedProperty with the supplied name and value
   */
  public static <N> NamedProperty<N> NamedProperty(final N name, final NestedDocument<N> value) {
    return new NamedProperty.Impl<>(value, name);
  }

  /**
   * Create a string literal.
   *
   * @param value   the property value
   * @return  a new NamedProperty with the supplied name and value
   */
  public static <N> Literal.StringLiteral<N> Literal(final String value) {
    return new Literal.StringLiteral<>(value);
  }

  /**
   * Create an int literal.
   *
   * @param value   the property value
   * @return  a new NamedProperty with the supplied name and value
   */
  public static <N> Literal.IntegerLiteral<N> Literal(final int value) {
    return new Literal.IntegerLiteral<>(value);
  }

  /**
   * Create a double literal.
   *
   * @param value   the property value
   * @return  a new NamedProperty with the supplied name and value
   */
  public static <N> Literal.DoubleLiteral<N> Literal(final double value) {
    return new Literal.DoubleLiteral<>(value);
  }

  /**
   * Create a URI literal.
   *
   * @param value   the property value
   * @return  a new NamedProperty with the supplied name and value
   */
  public static <N> Literal.UriLiteral<N> Literal(final URI value) {
    return new Literal.UriLiteral<>(value);
  }

  /**
   * Create a typed literal.
   *
   * @param value   the property value
   * @return  a new NamedProperty with the supplied name and value
   */
  public static <N> Literal.TypedLiteral<N> Literal(final String value, final QName type) {
    return new Literal.TypedLiteral<>(value, type);
  }

  /**
   * Create a boolean literal.
   *
   * @param value   the property value
   * @return  a new NamedProperty with the supplied name and value
   */
  public static <N> Literal.BooleanLiteral<N> Literal(final boolean value) {
    return new Literal.BooleanLiteral<>(value);
  }

  /**
   * Create a namespace binding.
   *
   * @param namespaceUri  the namespace URI
   * @param prefix        the prefix
   * @return  a new namespace binding for the namespace and prefix
   */

  public static NamespaceBinding NamespaceBinding(String namespaceUri, String prefix) {
    return new NamespaceBinding(namespaceUri, prefix);
  }

  /**
   * Create a QName from a local part.
   *
   * @param localPart     the local part
   * @return  a QName with the supplied localPart
   */
  public static QName QName(String localPart) {
    return new QName(localPart);
  }

  /**
   * Create a QName from a namespace URI and local part.
   *
   * @param namespaceURI  the namespace URI
   * @param localPart     the local part
   * @return  a QName with the supplied namespace URI and localPart
   */
  public static QName QName(String namespaceURI, String localPart) {
    return new QName(namespaceURI, localPart);
  }

  /**
   * Create a QName from a namespace URI, local part and prefix.
   *
   * @param namespaceURI  the namespace URI
   * @param localPart     the local part
   * @param prefix        the prefix
   * @return  a QName with the supplied namespace URI, localPart and prefix
   */
  public static QName QName(String namespaceURI, String localPart, String prefix) {
    return new QName(namespaceURI, localPart, prefix);
  }
}
