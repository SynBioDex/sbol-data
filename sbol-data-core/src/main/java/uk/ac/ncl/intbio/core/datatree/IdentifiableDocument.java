package uk.ac.ncl.intbio.core.datatree;

import java.net.URI;

/**
 * @author Matthew Pocock
 */
public interface IdentifiableDocument<N, P extends PropertyValue> extends Document<N, P> {
  public N getType();
  public URI getIdentity();
}
