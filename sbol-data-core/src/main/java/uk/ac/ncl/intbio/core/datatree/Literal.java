package uk.ac.ncl.intbio.core.datatree;

import java.net.URI;

import javax.xml.namespace.QName;

public interface Literal extends PropertyValue
{
	public Object getValue();

  public static interface StringLiteral extends Literal {
    @Override
    String getValue();
  }

  public static interface UriLiteral extends Literal {
    @Override
    URI getValue();
  }

  public static interface IntegerLiteral extends Literal {
    @Override
    Integer getValue();
  }

  public static interface DoubleLiteral extends Literal {
    @Override
    Double getValue();
  }

  public static interface TypedLiteral extends Literal {
    @Override
    String getValue();
    String getType();
  }

  public static interface BooleanLiteral extends Literal {
    @Override
    Boolean getValue();
  }

  public static abstract class Visitor {
    public final void visit(Literal l) {
      if(l instanceof StringLiteral) visit((StringLiteral) l);
      if(l instanceof UriLiteral) visit((UriLiteral) l);
      if(l instanceof IntegerLiteral) visit((IntegerLiteral) l);
      if(l instanceof DoubleLiteral) visit((DoubleLiteral) l);
      if(l instanceof TypedLiteral) visit((TypedLiteral) l);
      if(l instanceof BooleanLiteral) visit((BooleanLiteral) l);
    }

    public abstract void visit(StringLiteral l);
    public abstract void visit(UriLiteral l);
    public abstract void visit(IntegerLiteral l);
    public abstract void visit(DoubleLiteral l);
    public abstract void visit(TypedLiteral l);
    public abstract void visit(BooleanLiteral l);
  }
}
