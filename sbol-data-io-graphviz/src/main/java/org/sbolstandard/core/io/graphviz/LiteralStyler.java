package org.sbolstandard.core.io.graphviz;

import org.sbolstandard.core.datatree.IdentifiableDocument;
import org.sbolstandard.core.datatree.Literal;
import org.sbolstandard.core.datatree.PropertyValue;

import javax.xml.namespace.QName;
import java.util.Map;

/**
 * Styler for things rendered as nodes within graphviz.
 *
 * @author Matthew Pocock
 */
public interface LiteralStyler {
    void applyStyle(Map<String, String> styleMap, Literal<QName> value);
}
