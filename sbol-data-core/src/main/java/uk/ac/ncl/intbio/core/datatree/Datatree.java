package uk.ac.ncl.intbio.core.datatree;

import javax.xml.namespace.QName;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
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
    return TopLevelDocument(NamespaceBindings(), type, identity, properties);
  }
  
  public static interface NamespaceBindings {
	  public List<NamespaceBinding> getBindings();
  }
  
  public static NamespaceBindings NamespaceBindings(final NamespaceBinding ...bindings) {
	  return new NamespaceBindings() {
	    public List<NamespaceBinding> getBindings() {
		  return Arrays.asList(bindings);
	    }
	  };
  }

  public static <N> TopLevelDocument<N> TopLevelDocument(final NamespaceBindings namespaceBindings,
		                                                 final N type,
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
    	if (properties!=null)
        {
        	return properties.getProperties();	
        }
        else
        {
        	return Collections.<NamedProperty<N, PropertyValue>>emptyList();
        }
      }

	  @Override
	  public List<uk.ac.ncl.intbio.core.datatree.NamespaceBinding> getNamespaceBindings()
	  {
		// TODO Auto-generated method stub
		return namespaceBindings.getBindings();
	  }
    };
  }

  public static interface NestedDocuments<N>  extends PropertyValue {
	    public List<NestedDocument<N>> getDocuments();
  }
  
  @SafeVarargs
  public static <N> NestedDocuments<N> NestedDocuments(final NestedDocument<N> ... documents) {
    return new NestedDocuments<N>() {
      @Override
      public List<NestedDocument<N>> getDocuments() {
        return Arrays.asList(documents);
      }
    };
  }
  
  public static <N> NestedDocument<N> NestedDocument(final N type, final N identity, final NamedProperties<N, PropertyValue> properties) {
	  return NestedDocument(NamespaceBindings(), type, identity, properties);
  }
  
	public static <N> NestedDocument<N> NestedDocument(final NamespaceBindings bindings, final N type, final N identity, final NamedProperties<N, PropertyValue> properties)
	{
		return new NestedDocument<N>()
		{
			@Override
			public N getType()
			{
				return type;
			}

			@Override
			public N getIdentity()
			{
				return identity;
			}

			@Override
			public List<NamedProperty<N, PropertyValue>> getProperties()
			{
				if (properties!=null)
		        {
		        	return properties.getProperties();	
		        }
		        else
		        {
		        	return Collections.<NamedProperty<N, PropertyValue>>emptyList();
		        }
			}

			@Override
			public List<uk.ac.ncl.intbio.core.datatree.NamespaceBinding> getNamespaceBindings()
			{
				return bindings.getBindings();
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
	  return DocumentRoot(NamespaceBindings(), documents, properties);
  }
  
  public static <N> DocumentRoot<N> DocumentRoot(
		  final NamespaceBindings bindings,
		  final TopLevelDocuments<N> documents,
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
      
      @Override
      public List<NamespaceBinding> getNamespaceBindings() {
    	  return bindings.getBindings();
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
  
  public static <N> NamedProperty<N, PropertyValue> NamedProperty(final N name, final int value) {
	    return new NamedProperty<N, PropertyValue>() {
	      @Override
	      public Literal.IntegerLiteral getValue() {
	        return new Literal.IntegerLiteral() {
	          @Override
	          public Integer getValue() {
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
  
  public static <N> NamedProperty<N, PropertyValue> NamedProperty(final N name, final QName value) {
	    return new NamedProperty<N, PropertyValue>() {
	      @Override
	      public Literal.QNameLiteral getValue() {
	        return new Literal.QNameLiteral()
			{							
	          @Override
	          public QName getValue() {
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
  

  public static <N> NamedProperty<N, PropertyValue> NamedProperty(final N name, final NestedDocuments<N> value) {
	    return new NamedProperty<N, PropertyValue>() {
	      @Override
	      public NestedDocuments<N> getValue() {
	        return value;
	      }	        

	      @Override
	      public N getName() {
	        return name;
	      }
	    };
	  }

  
  public static <N> NamedProperty<N, PropertyValue> NamedProperty(final N name, final URI value) {
	    return new NamedProperty<N, PropertyValue>() {
	      @Override
	      public Literal.UriLiteral getValue() {
	        return new Literal.UriLiteral() {
	          @Override
	          public URI getValue() {
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

  public static NamespaceBinding NamespaceBinding(String namespaceUri, String prefix) {
	  return new NamespaceBinding(namespaceUri, prefix);
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
