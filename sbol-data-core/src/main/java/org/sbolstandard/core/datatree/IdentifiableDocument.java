package org.sbolstandard.core.datatree;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
 * <p>
 * Identifiable documents provide named properties. The values of these properties can be literals or nested documents.
 * </p>
 *
 * @author Matthew Pocock
 * @param <N>   the property name type
 */
public interface IdentifiableDocument<N> extends Document {
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

  /**
   * Get the named properties of this document.
   *
   * <p>
   *   Documents are annotated with any number of named properties. Many properties can be present for the same property
   *   name.
   * </p>
   *
   * <p>Specific sub-classses of document will refine the property type.</p>
   *
   * <p>
   *   The list should not be modified.
   * </p>
   *
   * @return a list of named properties
   */
	public List<NamedProperty<N>> getProperties();

  /**
   * Look up all property values by name.
   *
   * <p>
   *   This returns all values for property values with a matching name.
   * </p>
   *
   * @param propertyName  the name to look up
   * @return  a list (possibly empty) of all values for that name
   */
  List<PropertyValue<N>> getPropertyValues(N propertyName);

  // Package private, used in Datatree only.
  static abstract class Abstract<N>  implements IdentifiableDocument<N> {
    @Override
    public List<PropertyValue<N>> getPropertyValues(N propertyName) {
      List<PropertyValue<N>> values = null;
      for(NamedProperty<N> p : getProperties()) {
        if(p.getName().equals(propertyName)) {
          if(values == null) {
            values = new ArrayList<>();
          }
          values.add(p.getValue());
        }
      }

      if(values == null) {
        values = Collections.emptyList();
      }

      return values;
    }
  }

}
