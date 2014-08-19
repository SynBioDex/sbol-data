package uk.ac.ncl.intbio.core.datatree;

import javax.xml.namespace.QName;
import java.util.List;

public interface Document<N, P extends PropertyValue> {
	public List<NamedProperty<N, P>> getProperties();
	public List<NamespaceBinding> getNamespaceBindings();

    P getProperty(N rdfType);

    public static abstract class Abstract<N, P extends PropertyValue>  implements Document<N, P> {
        @Override
        public P getProperty(N propety) {
            for(NamedProperty<N, P> p : getProperties()) {
                if(p.getName().equals(propety)) {
                    return p.getValue();
                }
            }
            return null;
        }
    }
}
