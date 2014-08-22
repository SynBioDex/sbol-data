package uk.ac.ncl.intbio.core.datatree;

/**
 * A document that can appear at the top level, and can not be nested under another document as a property value.
 *
 * @author Matthew Pocock
 * @param <N>   the property name type
 */
public interface TopLevelDocument<N> extends IdentifiableDocument<N, PropertyValue> {
}
