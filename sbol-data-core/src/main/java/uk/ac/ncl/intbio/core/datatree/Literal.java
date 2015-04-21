package uk.ac.ncl.intbio.core.datatree;

import java.net.URI;

import javax.xml.namespace.QName;

/**
 * Root type for literals.
 *
 * <p>
 *   There are specific sub-types for literals with specific value types.
 *   Instances are created through {@link Datatree} factory methods.
 * </p>
 *
 * <p>
 *   The data-specific types of literal can be derived from a literal using a
 *   {@link uk.ac.ncl.intbio.core.datatree.Literal.Visitor}.
 * </p>
 *
 * @author Matthew Pocock
 */
public interface Literal<N> extends PropertyValue<N>
{
  /**
   * Get the value of this literal.
   *
   * <p>
   * More specific literals specialise this return type from Object to their type.
   * </p>
   *
   * @return the value
   */
	public Object getValue();

  /**
   * Literals providing a String.
   *
   * @author Matthew Pocock
   */
  public static interface StringLiteral<N> extends Literal<N> {
    @Override
    String getValue();
  }

  /**
   * Literals providing a URI.
   *
   * @author Matthew Pocock
   */
  public static interface UriLiteral<N> extends Literal<N> {
    @Override
    URI getValue();
  }

  /**
   * Literals providing an int.
   *
   * @author Matthew Pocock
   */
  public static interface IntegerLiteral<N> extends Literal<N> {
    @Override
    Integer getValue();
  }

  /**
   * Literals providing a double.
   *
   * @author Matthew Pocock
   */
  public static interface DoubleLiteral<N> extends Literal<N> {
    @Override
    Double getValue();
  }

  /**
   * Literals providing a typed literal.
   *
   * @author Matthew Pocock
   */
  public static interface TypedLiteral<N> extends Literal<N> {
    @Override
    String getValue();

    /**
     * The type of the typed literal.
     *
     * <p>This is used to indicate how the value string is encoded. Values should be taken from or derive from the
     * xsd types.</p>
     *
     * @return tye type
     */
    QName getType();
  }

  /**
   * Literals providing a boolean.
   *
   * @author Matthew Pocock
   */
  public static interface BooleanLiteral<N> extends Literal<N> {
    @Override
    Boolean getValue();
  }

  /**
   * Type-safe visitor to allow code to go from a generic literal to a type-specific literal.
   *
   * <p>Use:</p>
   * <pre><code>
   *   new Visitor() { ... }.visit(literal);
   * </code></pre>
   *
   * Implementations must implement each of the type-specific visit methods.
   *
   * @author Matthew Pocock
   */
  public abstract class Visitor<N> {
    public final void visit(Literal<N> l) {
      try {
        if (l instanceof StringLiteral) visit((StringLiteral<N>) l);
        if (l instanceof UriLiteral) visit((UriLiteral<N>) l);
        if (l instanceof IntegerLiteral) visit((IntegerLiteral<N>) l);
        if (l instanceof DoubleLiteral) visit((DoubleLiteral<N>) l);
        if (l instanceof TypedLiteral) visit((TypedLiteral<N>) l);
        if (l instanceof BooleanLiteral) visit((BooleanLiteral<N>) l);
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    }

    public void visit(StringLiteral<N> l) throws Exception {};
    public void visit(UriLiteral<N> l) throws Exception {};
    public void visit(IntegerLiteral<N> l) throws Exception {};
    public void visit(DoubleLiteral<N> l) throws Exception {};
    public void visit(TypedLiteral<N> l) throws Exception {};
    public void visit(BooleanLiteral<N> l) throws Exception {};
  }
}
