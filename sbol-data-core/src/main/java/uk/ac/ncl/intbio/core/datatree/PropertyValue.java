package uk.ac.ncl.intbio.core.datatree;

public interface PropertyValue {
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
