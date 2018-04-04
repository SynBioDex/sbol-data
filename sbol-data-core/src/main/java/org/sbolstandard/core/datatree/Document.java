package org.sbolstandard.core.datatree;

import java.util.List;

/**
 * Base type for documents.
 *
 * <p>
 *   Documents are a collection of namespace bindings and possibly other values.
 *   They can be nested to form a tree data-structure.
 * </p>
 *
 * <p>
 *   This interface exists to unify common functionality between {@link org.sbolstandard.core.datatree.DocumentRoot},
 *   {@link org.sbolstandard.core.datatree.TopLevelDocument} and {@link org.sbolstandard.core.datatree.NestedDocument}.
 * </p>
 */
public interface Document {
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
}
