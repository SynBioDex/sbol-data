package uk.ac.ncl.intbio.core.datatree;

import java.util.List;

/**
 * A document root that contains a list of top-level documents.
 *
 * <p>
 *   This represents the root of a datatree structure. It will usually correspond to the content of a single file.
 * </p>
 *
 * @author Matthew Pocock
 * @param <N>   the property name type
 */
public interface DocumentRoot<N> extends Document {
  /**
   * Get the top-level documents under this root.
   *
   * @return the top-level documents
   */
	public List<TopLevelDocument<N>> getTopLevelDocuments();

  class Impl<N> implements DocumentRoot<N> {

    private final List<NamespaceBinding> bindings;
    private final List<TopLevelDocument<N>> documents;

    public Impl(List<NamespaceBinding> bindings, List<TopLevelDocument<N>> documents) {
      if(bindings == null) throw new NullPointerException("Unable to create DocumentRoot with null bindings");
      if(documents == null) throw new NullPointerException("Unable to create DocumentRoot with null documents");

      this.bindings = bindings;
      this.documents = documents;
    }

    @Override
    public List<TopLevelDocument<N>> getTopLevelDocuments() {
      return documents;
    }

    @Override
    public List<NamespaceBinding> getNamespaceBindings() {
      return bindings;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Impl<?> impl = (Impl<?>) o;

      if (!bindings.equals(impl.bindings)) return false;
      return documents.equals(impl.documents);

    }

    @Override
    public int hashCode() {
      int result = bindings.hashCode();
      result = 31 * result + documents.hashCode();
      return result;
    }

    @Override
    public String toString() {
      return "Impl{" +
              "bindings=" + bindings +
              ", documents=" + documents +
              '}';
    }
  }

}
