package org.sbolstandard.core.io.graphviz;

import org.sbolstandard.core.datatree.Literal;

import javax.xml.namespace.QName;
import java.util.Map;

/**
 * Styler for things rendered as nodes within graphviz.
 *
 * @author Matthew Pocock
 */
public interface LinkerStyler {
    void applyStyle(Map<String, String> styleMap, Literal.UriLiteral<QName> link);
}
