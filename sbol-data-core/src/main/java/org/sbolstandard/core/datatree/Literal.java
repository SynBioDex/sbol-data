package org.sbolstandard.core.datatree;

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
 *   {@link org.sbolstandard.core.datatree.Literal.Visitor}.
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
  public static class StringLiteral<N> extends Literal.Abstract<N> {
    private final String value;

    public StringLiteral(String value) {
      if(value == null) throw new NullPointerException("Can't create a StringLiteral with null value");
      this.value = value;
    }

    @Override
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return "StringLiteral{value=" + value +
      "}";
    }
  }

  /**
   * Literals providing a URI.
   *
   * @author Matthew Pocock
   */
  public static class UriLiteral<N> extends Literal.Abstract<N> {
    private final URI value;

    public UriLiteral(URI value) {
      if(value == null) throw new NullPointerException("Can't create a UriLiteral with null value");
      this.value = value;
    }

    @Override
    public URI getValue() {
      return value;
    }

    @Override
    public String toString() {
      return "UriLiteral{value=" + value +
      "}";
    }
  }

  /**
   * Literals providing an int.
   *
   * @author Matthew Pocock
   */
  public static class IntegerLiteral<N> extends Literal.Abstract<N> {
    private final Integer value;

    public IntegerLiteral(Integer value) {
      if(value == null) throw new NullPointerException("Can't create a IntegerLiteral with null value");
      this.value = value;
    }

    @Override
    public Integer getValue() {
      return value;
    }

    @Override
    public String toString() {
      return "IntegerLiteral{value=" + value +
      "}";
    }
  }

  /**
   * Literals providing a double.
   *
   * @author Matthew Pocock
   */
  public static class DoubleLiteral<N> extends Literal.Abstract<N> {
    private final Double value;

    public DoubleLiteral(Double value) {
      if(value == null) throw new NullPointerException("Can't create a DoubleLiteral with null value");
      this.value = value;
    }

    @Override
    public Double getValue() {
      return value;
    }

    @Override
    public String toString() {
      return "DoubleLiteral{value=" + value +
              "}";
    }
  }

  /**
   * Literals providing a boolean.
   *
   * @author Matthew Pocock
   */
  public static class BooleanLiteral<N> extends Literal.Abstract<N> {
    private final Boolean value;

    public BooleanLiteral(Boolean value) {
      if(value == null) throw new NullPointerException("Can't create a BooleanLiteral with null value");
      this.value = value;
    }

    @Override
    public Boolean getValue() {
      return value;
    }


    @Override
    public String toString() {
      return "BooleanLiteral{value=" + value +
              "}";
    }
  }

  /**
   * Literals providing a typed literal.
   *
   * @author Matthew Pocock
   */
  public static class TypedLiteral<N> extends Literal.Abstract<N> {
    private final String value;
    private final QName type;

    public TypedLiteral(String value, QName type) {
      if(value == null) throw new NullPointerException("Can't create a TypedLiteral with null value");
      if(type == null) throw new NullPointerException("Can't create a TypedLiteral with null type");

      this.value = value;
      this.type = type;
    }

    @Override
    public String getValue() {
      return value;
    }

    /**
     * The type of the typed literal.
     *
     * <p>This is used to indicate how the value string is encoded. Values should be taken from or derive from the
     * xsd types.</p>
     *
     * @return tye type
     */
    public QName getType() {
      return getType();
    }

    @Override
    public String toString() {
      return "TypedLiteral{" +
              "value='" + value + '\'' +
              ", type=" + type +
              '}';
    }
  }

  abstract class Abstract<N> implements Literal<N> {
    @Override
    public int hashCode() {
      return getValue().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      if(this == obj) return true;

      if(obj == null || !(obj instanceof Literal<?>)) return false;
      Literal<?> l = (Literal<?>) obj;

      return getValue().equals(l.getValue());
    }
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

    public abstract void visit(StringLiteral<N> l) throws Exception;
    public abstract void visit(UriLiteral<N> l) throws Exception;
    public abstract void visit(IntegerLiteral<N> l) throws Exception;
    public abstract void visit(DoubleLiteral<N> l) throws Exception;
    public abstract void visit(TypedLiteral<N> l) throws Exception;
    public abstract void visit(BooleanLiteral<N> l) throws Exception;
  }
}
