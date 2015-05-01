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
public interface PropertyValue<N> {

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
    public final void visit(PropertyValue<N> v) {
      try {
        if(v instanceof NestedDocument) visit((NestedDocument<N>) v);
        if(v instanceof Literal) visit((Literal<N>) v);
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    }

    public void visit(NestedDocument<N> v) throws Exception {};
    public void visit(Literal<N> v) throws Exception {};
  }

  public static final class LiteralVisitor<N> extends Visitor<N> {
    private final Literal.Visitor<N> litVis;

    public LiteralVisitor(Literal.Visitor<N> litVis) {
      this.litVis = litVis;
    }

    @Override
    public final void visit(Literal<N> v) throws Exception {
      litVis.visit(v);
    }
  }

  public static abstract class NestedDocumentVisitor<N> extends Visitor<N> {
    @Override
    public final void visit(Literal<N> v) throws Exception {
      // no-op
    }
  }
}
