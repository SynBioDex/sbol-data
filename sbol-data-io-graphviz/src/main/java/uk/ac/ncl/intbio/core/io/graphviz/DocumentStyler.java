package uk.ac.ncl.intbio.core.io.graphviz;

import uk.ac.ncl.intbio.core.datatree.IdentifiableDocument;
import uk.ac.ncl.intbio.core.datatree.PropertyValue;

import javax.xml.namespace.QName;
import java.util.Map;

/**
 * Styler for things rendered as nodes within graphviz.
 *
 * @author Matthew Pocock
 */
public interface DocumentStyler {
    void applyStyle(Map<String, String> styleMap, IdentifiableDocument<QName, ? extends PropertyValue> document);
}
