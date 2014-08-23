package uk.ac.ncl.intbio.core.datatree;

/**
 * A document that can be nested under another document as a property value but can not appear at the top level.
 *
 * @author Matthew Pocock
 * @param <N>   the property name type
 */
public interface NestedDocument<N> extends IdentifiableDocument<N>, PropertyValue<N>  {
}
