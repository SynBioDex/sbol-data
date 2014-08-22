package uk.ac.ncl.intbio.core.datatree;

/**
 * A named property.
 *
 * <p>
 *   In RDF, this corresponds to a (predicate, object) pair.
 *   The implied subject is the identity of the IdentifiedDocument that holds this in its properties list.
 * </p>
 *
 * <p>
 *   Instances of this interface are created using factory methods on {@link Datatree}.
 * </p>
 *
 *
 * @param <N>   the property name type
 * @param <P>   the value range
 */
public interface NamedProperty<N, P extends PropertyValue> {
  /**
   * Get the value of this named property.
   *
   * @return the value
   */
	public P getValue();

  /**
   * Get the name of this named property.
   *
   * @return the name
   */
	public N getName();
}
