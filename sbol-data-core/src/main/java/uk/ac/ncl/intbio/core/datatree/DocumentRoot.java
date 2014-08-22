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
public interface DocumentRoot<N> extends Document<N, Literal> {
  /**
   * Get the top-level documents under this root.
   *
   * @return the top-level documents
   */
	public List<TopLevelDocument<N>> getTopLevelDocuments();
	
	
}
