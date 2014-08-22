package uk.ac.ncl.intbio.core.datatree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base type for documents.
 *
 * <p>
 *   Documents are a collection of named properties and namespace bindings. They can be nested to form a tree
 *   data-structure through properties that have documents as values.
 * </p>
 *
 * <p>
 *   Each document type fixes the property value range to either literals or all values.
 * </p>
 *
 * <p>
 *   This interface exists to unify common functionality between {@link uk.ac.ncl.intbio.core.datatree.DocumentRoot},
 *   {@link uk.ac.ncl.intbio.core.datatree.TopLevelDocument} and {@link uk.ac.ncl.intbio.core.datatree.NestedDocument}.
 * </p>
 * @param <N>   the property name type
 * @param <P>   the property value type
 */
public interface Document<N, P extends PropertyValue> {
  /**
   * Get the named properties of this document.
   *
   * <p>
   *   Documents are annotated with any number of named properties. Many properties can be present for the same property
   *   name.
   * </p>
   *
   * <p>Specific sub-classses of document will refine the property type.</p>
   *
   * <p>
   *   The list should not be modified.
   * </p>
   *
   * @return a list of named properties
   */
	public List<NamedProperty<N, P>> getProperties();

  /**
   * Get the namespace bindings introduced by this document.
   *
   * <p>
   *   Namespace bindings affect the interpretation of nested property names. A namespace binding is in effect for the
   *   document that refers to it, and for all child documents reached through property values.
   * </p>
   *
   * @return a list of namespace bindings
   */
	public List<NamespaceBinding> getNamespaceBindings();

  /**
   * Look up all property values by name.
   *
   * <p>
   *   This returns all values for property values with a matching name.
   * </p>
   *
   * @param propertyName  the name to look up
   * @return  a list (possibly empty) of all values for that name
   */
  List<P> getPropertyValues(N propertyName);

  // Package private, used in Datatree only.
  static abstract class Abstract<N, P extends PropertyValue>  implements Document<N, P> {
    @Override
    public List<P> getPropertyValues(N propertyName) {
      List<P> values = null;
      for(NamedProperty<N, P> p : getProperties()) {
        if(p.getName().equals(propertyName)) {
          if(values == null) {
            values = new ArrayList<>();
          }
          values.add(p.getValue());
        }
      }

      if(values == null) {
        values = Collections.emptyList();
      }

      return values;
    }
  }
}
