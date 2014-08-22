package uk.ac.ncl.intbio.core.datatree;

/**
 * Root type for things that can be values of a {@link uk.ac.ncl.intbio.core.datatree.NamedProperty}.
 *
 * <p>
 *   There are two immediate sub-types of PropertyValue. {@link uk.ac.ncl.intbio.core.datatree.NestedDocument}
 *   can be used as a value to build tree structures. {@link uk.ac.ncl.intbio.core.datatree.Literal} is used to provide
 *   data values. Instances are created through {@link Datatree} factory methods.
 * </p>
 *
 * @author Matthew Pocock
 */
public interface PropertyValue {

  /**
   * Type-safe visitor to allow code to drill down to more concrete value types.
   *
   * <p>Use:</p>
   * <pre><code>
   *   new Visitor() { ... }.visit(propertyValue);
   * </code></pre>
   *
   * Implementations must implement each of the type-specific visit methods.
   *
   * @author Matthew Pocock
   * @param <N>   the property name type
   */
  public static abstract class Visitor<N> {
    public final void visit(PropertyValue v) {
      try {
        if(v instanceof NestedDocument) visit((NestedDocument<N>) v);
        if(v instanceof Literal) visit((Literal) v);
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    }

    public abstract void visit(NestedDocument<N> v) throws Exception;
    public abstract void visit(Literal v) throws Exception;
  }
}
