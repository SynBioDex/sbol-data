package uk.ac.ncl.intbio.core.datatree;

import java.net.URI;

/**
 * Those documents that are associated with an identifier.
 *
 * <p>
 *   These documents have a type and an identity.
 * </p>
 * <p>
 *   The type represents the data domain-specific type that this document represents.
 *   Types should be taken from a terminology, controlled vocabulary or ontology.
 * </p>
 * <p>
 *   Identifiers are globally unique identifiers for the document.
 *   Anything wishing to refer to the document can use the identifying URI.
 * </p>
 *
 * @author Matthew Pocock
 */
public interface IdentifiableDocument<N, P extends PropertyValue> extends Document<N, P> {
  /**
   * Get the type of this document.
   *
   * @return the document type
   */
  public N getType();

  /**
   * Get the identity of this document
   *
   * @return the document identity
   */
  public URI getIdentity();
}
