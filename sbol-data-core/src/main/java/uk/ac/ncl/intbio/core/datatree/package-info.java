/**
 * The data-tree data model.
 *
 * <p>
 *   Data-tree represents semi-structured data as a tree of documents with bags of named values, which themselves can
 *   be documents.
 *   Documents have an associated type, allowing them to be tagged with a domain-specific type.
 *   Properties are linked by a name, allowing them to be tagged with a domain-specific property.
 *   These should be associated with an external terminology, controlled vocabulary or ontology.
 * </p>
 *
 * <p>
 *   This package API abstracts over the type of the property names, allowing users to use QNames, Strings or some other
 *   type, as required.
 * </p>
 *
 * @author Matthew Pocock
 * @author Goksel Misirli
 */
package uk.ac.ncl.intbio.core.datatree;