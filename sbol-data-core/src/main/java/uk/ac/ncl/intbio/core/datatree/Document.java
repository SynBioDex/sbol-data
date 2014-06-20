package uk.ac.ncl.intbio.core.datatree;

import java.util.List;

public interface Document<P extends PropertyValue> {
	public List<NamedProperty<P>> getProperties();
}
