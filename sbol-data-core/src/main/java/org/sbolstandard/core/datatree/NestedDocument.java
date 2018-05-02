package org.sbolstandard.core.datatree;

import java.net.URI;
import java.util.List;

/**
 * A document that can be nested under another document as a property value but can not appear at the top level.
 *
 * @author Matthew Pocock
 * @param <N>   the property name type
 */
public interface NestedDocument<N> extends IdentifiableDocument<N>, PropertyValue<N>  {

  class Impl<N> extends IdentifiableDocument.Abstract<N> implements NestedDocument<N> {
    private final List<NamespaceBinding> bindings;
    private final N type;
    private final URI identity;
    private final List<NamedProperty<N>> properties;

    Impl(List<NamespaceBinding> bindings, N type, URI identity, List<NamedProperty<N>> properties) {
      if (bindings == null)
        throw new NullPointerException("Can't create a NestedDocument with null bindings");
      if (type == null)
        throw new NullPointerException("Can't create a NestedDocument with null type");
      if (identity == null)
        throw new NullPointerException("Can't create a NestedDocument with null identity");
      if (properties == null)
        throw new NullPointerException("Can't create a NestedDocument with null properties");

      this.bindings = bindings;
      this.type = type;
      this.identity = identity;
      this.properties = properties;
    }

    @Override
    public N getType() {
      return type;
    }

    @Override
    public URI getIdentity() {
      return identity;
    }

    @Override
    public List<NamedProperty<N>> getProperties() {
      return properties;
    }

    @Override
    public List<NamespaceBinding> getNamespaceBindings() {
      return bindings;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Impl<?> nd = (Impl<?>) o;

      if (!bindings.equals(nd.bindings)) return false;
      if (!type.equals(nd.type)) return false;
      if (!identity.equals(nd.identity)) return false;
      return properties.equals(nd.properties);

    }

    @Override
    public int hashCode() {
      int result = bindings.hashCode();
      result = 31 * result + type.hashCode();
      result = 31 * result + identity.hashCode();
      result = 31 * result + properties.hashCode();
      return result;
    }

    @Override
    public String toString() {
      return "NestedDocument{" +
              "bindings=" + bindings +
              ", type=" + type +
              ", identity=" + identity +
              ", properties=" + properties +
              '}';
    }
  }
}
