package uk.ac.ncl.intbio.core.datatree;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.List;

/**
 * Static utilities for working with the datatree types.
 *
 * @author Matthew Pocock
 */
public final class Datatree
{
  public static interface TopLevelDocuments<N> {
    public List<TopLevelDocument<N>> getDocuments();
  }

  @SafeVarargs
  public static <N> TopLevelDocuments<N> TopLevelDocuments(final TopLevelDocument<N> ... documents) {
    return new TopLevelDocuments<N>() {
      @Override
      public List<TopLevelDocument<N>> getDocuments() {
        return Arrays.asList(documents);
      }
    };
  }

  public static <N> TopLevelDocument<N> TopLevelDocument(final N type,
                                                         final N identity,
                                                         final NamedProperties<N, PropertyValue> properties) {
    return new TopLevelDocument<N>() {
      @Override
      public N getType() {
        return type;
      }

      @Override
      public N getIdentity() {
        return identity;
      }

      @Override
      public List<NamedProperty<N, PropertyValue>> getProperties() {
        return properties.getProperties();
      }
    };
  }

  public static interface NamedProperties<N, P extends PropertyValue> {
    public List<NamedProperty<N, P>> getProperties();
  }

  @SafeVarargs
  public static <N> NamedProperties<N, PropertyValue> NamedProperties(final NamedProperty<N, PropertyValue> ... properties) {
    return new NamedProperties<N, PropertyValue>() {
      @Override
      public List<NamedProperty<N, PropertyValue>> getProperties() {
        return Arrays.asList(properties);
      }
    };
  }

  @SafeVarargs
  public static <N> NamedProperties<N, Literal> LiteralProperties(final NamedProperty<N, Literal> ... properties) {
    return new NamedProperties<N, Literal>() {
      @Override
      public List<NamedProperty<N, Literal>> getProperties() {
        return Arrays.asList(properties);
      }
    };
  }

  public static <N> DocumentRoot<N> DocumentRoot(final TopLevelDocuments<N> documents,
                                                 final NamedProperties<N, Literal> properties) {
    return new DocumentRoot<N>() {
      @Override
      public List<TopLevelDocument<N>> getTopLevelDocuments() {
        return documents.getDocuments();
      }

      @Override
      public List<NamedProperty<N, Literal>> getProperties() {
        return properties.getProperties();
      }
    };
  }

  public static <N> NamedProperty<N, Literal> NamedLiteralProperty(final N name, final String value) {
    return new NamedProperty<N, Literal>() {
      @Override
      public Literal.StringLiteral getValue() {
        return new Literal.StringLiteral() {
          @Override
          public String getValue() {
            return value;
          }
        };
      }

      @Override
      public N getName() {
        return name;
      }
    };
  }

  public static <N> NamedProperty<N, PropertyValue> NamedProperty(final N name, final String value) {
    return new NamedProperty<N, PropertyValue>() {
      @Override
      public Literal.StringLiteral getValue() {
        return new Literal.StringLiteral() {
          @Override
          public String getValue() {
            return value;
          }
        };
      }

      @Override
      public N getName() {
        return name;
      }
    };
  }


  public static QName QName(String localPart) {
    return new QName(localPart);
  }

  public static QName QName(String namespaceURI, String localPart) {
    return new QName(namespaceURI, localPart);
  }

  public static QName QName(String namespaceURI, String localPart, String prefix) {
    return new QName(namespaceURI, localPart, prefix);
  }
}
