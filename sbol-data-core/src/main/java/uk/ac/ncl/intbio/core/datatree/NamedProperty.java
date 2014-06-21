package uk.ac.ncl.intbio.core.datatree;

import java.net.URI;

public interface NamedProperty<N, P extends PropertyValue> {
	public P getValue();
	public N getName();
}
