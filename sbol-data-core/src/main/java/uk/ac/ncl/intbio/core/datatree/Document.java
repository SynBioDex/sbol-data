package uk.ac.ncl.intbio.core.datatree;

import java.util.List;

public interface Document<N, P extends PropertyValue> {
	public List<NamedProperty<N, P>> getProperties();
}
