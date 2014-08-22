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
public interface Literal extends PropertyValue
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
  public static interface StringLiteral extends Literal {
    @Override
    String getValue();
  }

  /**
   * Literals providing a URI.
   *
   * @author Matthew Pocock
   */
  public static interface UriLiteral extends Literal {
    @Override
    URI getValue();
  }

  /**
   * Literals providing an int.
   *
   * @author Matthew Pocock
   */
  public static interface IntegerLiteral extends Literal {
    @Override
    Integer getValue();
  }

  /**
   * Literals providing a double.
   *
   * @author Matthew Pocock
   */
  public static interface DoubleLiteral extends Literal {
    @Override
    Double getValue();
  }

  /**
   * Literals providing a typed literal.
   *
   * @author Matthew Pocock
   */
  public static interface TypedLiteral extends Literal {
    @Override
    String getValue();

    /**
     * The type of the typed literal.
     *
     * <p>This is used to indicate how the value string is encoded. Values should be taken from or derive from the
     * xsd types.</p>
     * @return
     */
    QName getType();
  }

  /**
   * Literals providing a boolean.
   *
   * @author Matthew Pocock
   */
  public static interface BooleanLiteral extends Literal {
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
  public static abstract class Visitor {
    public final void visit(Literal l) {
      try {
        if (l instanceof StringLiteral) visit((StringLiteral) l);
        if (l instanceof UriLiteral) visit((UriLiteral) l);
        if (l instanceof IntegerLiteral) visit((IntegerLiteral) l);
        if (l instanceof DoubleLiteral) visit((DoubleLiteral) l);
        if (l instanceof TypedLiteral) visit((TypedLiteral) l);
        if (l instanceof BooleanLiteral) visit((BooleanLiteral) l);
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    }

    public abstract void visit(StringLiteral l) throws Exception;
    public abstract void visit(UriLiteral l) throws Exception;
    public abstract void visit(IntegerLiteral l) throws Exception;
    public abstract void visit(DoubleLiteral l) throws Exception;
    public abstract void visit(TypedLiteral l) throws Exception;
    public abstract void visit(BooleanLiteral l) throws Exception;
  }
}
