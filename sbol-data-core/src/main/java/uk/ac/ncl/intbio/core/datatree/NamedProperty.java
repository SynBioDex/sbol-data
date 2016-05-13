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
 */
public interface NamedProperty<N> {
  /**
   * Get the value of this named property.
   *
   * @return the value
   */
	public PropertyValue<N> getValue();

  /**
   * Get the name of this named property.
   *
   * @return the name
   */
	public N getName();


  class Impl<N> implements NamedProperty<N> {
    private final PropertyValue<N> value;
    private final N name;

    Impl(N name, PropertyValue<N> value) {
      if(name == null) throw new IllegalArgumentException("Can't create NamedProperty with null name");
      if(value == null) throw new IllegalArgumentException("Can't create NamedProperty with null value");

      this.name = name;
      this.value = value;
    }

    @Override
    public PropertyValue<N> getValue() {
      return value;
    }

    @Override
    public N getName() {
      return name;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Impl<?> impl = (Impl<?>) o;

      if (!value.equals(impl.value)) return false;
      return name.equals(impl.name);

    }

    @Override
    public int hashCode() {
      int result = value.hashCode();
      result = 31 * result + name.hashCode();
      return result;
    }

    @Override
    public String toString() {
      return "NamedProperty{" +
              "value=" + value +
              ", name=" + name +
              '}';
    }
  }

}
