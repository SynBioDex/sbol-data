package uk.ac.ncl.intbio.core.io.graphviz;

import uk.ac.ncl.intbio.core.datatree.Literal;

import java.util.Map;

/**
 * Styler for things rendered as nodes within graphviz.
 *
 * @author Matthew Pocock
 */
public interface LinkerStyler {
    void applyStyle(Map<String, String> styleMap, Literal.UriLiteral link);
}
