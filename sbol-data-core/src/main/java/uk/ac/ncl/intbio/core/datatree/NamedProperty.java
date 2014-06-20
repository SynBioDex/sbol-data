package uk.ac.ncl.intbio.core.datatree;

import java.net.URI;

public interface NamedProperty<P extends PropertyValue> {
	public P getValue();
	public URI getName();
}
