package uk.ac.ncl.intbio.core.datatree;

public interface PropertyValue {
  public static abstract class Visitor<N> {
    public final void visit(PropertyValue v) {
      if(v instanceof NestedDocument) visit((NestedDocument<N>) v);
      if(v instanceof Literal) visit((Literal) v);
    }

    public abstract void visit(NestedDocument<N> v);
    public abstract void visit(Literal v);
  }
}
