package uk.ac.ncl.intbio.core.datatree;

import java.util.List;

public interface Document<N, P extends PropertyValue> {
	public List<NamedProperty<N, P>> getProperties();
	public List<NamespaceBinding> getNamespaceBindings();

  P getProperty(N rdfType);

  public static abstract class Abstract<N, P extends PropertyValue>  implements Document<N, P> {
    @Override
    public P getProperty(N property) {
      for(NamedProperty<N, P> p : getProperties()) {
        if(p.getName().equals(property)) {
          return p.getValue();
        }
      }
      return null;
    }
  }
}
