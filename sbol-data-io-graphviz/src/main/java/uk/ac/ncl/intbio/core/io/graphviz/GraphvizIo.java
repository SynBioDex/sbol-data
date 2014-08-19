package uk.ac.ncl.intbio.core.io.graphviz;

import java.io.PrintWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import uk.ac.ncl.intbio.core.datatree.Datatree;
import uk.ac.ncl.intbio.core.datatree.DocumentRoot;
import uk.ac.ncl.intbio.core.datatree.IdentifiableDocument;
import uk.ac.ncl.intbio.core.datatree.Literal;
import uk.ac.ncl.intbio.core.datatree.NamedProperty;
import uk.ac.ncl.intbio.core.datatree.NestedDocument;
import uk.ac.ncl.intbio.core.datatree.PropertyValue;
import uk.ac.ncl.intbio.core.datatree.TopLevelDocument;
import uk.ac.ncl.intbio.core.io.CoreIoException;
import uk.ac.ncl.intbio.core.io.IoWriter;

public class GraphvizIo
{
    private DocumentStyler documentStyler = Styler.document.identityAsLabel;
    private LiteralStyler literalStyler = Styler.literal.valueAslabel;
    private EdgeStyler edgeStyler = Styler.edge.nameAsLabel;
    private LinkerStyler linkStyler = Styler.linker.nonConstraint;

    private String applyStyle(IdentifiableDocument<QName, ? extends PropertyValue> doc) {
        Map<String, String> styles = new HashMap<>();
        documentStyler.applyStyle(styles, doc);
        return flatten(styles);
    }

    private String applyStyle(Literal value) {
        Map<String, String> styles = new HashMap<>();
        literalStyler.applyStyle(styles, value);
        return flatten(styles);
    }

    private String applyStyle(IdentifiableDocument<QName, ? extends PropertyValue> from,
                              PropertyValue to,
                              QName name) {
        Map<String, String> styles = new HashMap<>();
        edgeStyler.applyStyle(styles, from, to, name);
        return flatten(styles);
    }

    private String applyStyle(Literal.UriLiteral uriLiteral) {
        Map<String, String> styles = new HashMap<>();
        linkStyler.applyStyle(styles, uriLiteral);
        return flatten(styles);
    }

    private String flatten(Map<String, String> styles) {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, String> me : styles.entrySet()) {
            if(sb.length() > 0) sb.append(", ");
            sb.append(me.getKey());
            sb.append("=\"");
            sb.append(me.getValue());
            sb.append("\"");
        }
        return sb.toString();
    }

    public DocumentStyler getDocumentStyler() {
        return documentStyler;
    }

    public void setDocumentStyler(DocumentStyler documentStyler) {
        this.documentStyler = documentStyler;
    }

    public LiteralStyler getLiteralStyler() {
        return literalStyler;
    }

    public void setLiteralStyler(LiteralStyler literalStyler) {
        this.literalStyler = literalStyler;
    }

    public EdgeStyler getEdgeStyler() {
        return edgeStyler;
    }

    public void setEdgeStyler(EdgeStyler edgeStyler) {
        this.edgeStyler = edgeStyler;
    }

    public LinkerStyler getLinkStyler() {
        return linkStyler;
    }

    public void setLinkStyler(LinkerStyler linkStyler) {
        this.linkStyler = linkStyler;
    }

    public IoWriter<QName> createIoWriter(final PrintWriter writer)
	{
		return new IoWriter<QName>()
		{

			@Override
			public void write(DocumentRoot<QName> document) throws CoreIoException
			{
				writer.println("digraph {");
                writer.println("rankdir=\"LR\";");
				for (TopLevelDocument<QName> child : document.getTopLevelDocuments())
				{
					write(child);
				}
				writer.println("}");
			}

			private void write(IdentifiableDocument<QName, PropertyValue> doc)
			{
				writer.println("\"" + doc.getIdentity() + "\" [" + applyStyle(doc) + "];");

				for (NamedProperty<QName, PropertyValue> property : doc.getProperties())
				{
					write(doc, property);
				}

			}

			private void write(IdentifiableDocument<QName, PropertyValue> parent, NamedProperty<QName, PropertyValue> property)
			{
				if (property.getValue() instanceof Datatree.NestedDocuments)
				{
					Datatree.NestedDocuments<QName> docs = (Datatree.NestedDocuments<QName>) property.getValue();
					for (NestedDocument<QName> doc : docs.getDocuments())
					{
						writer.println("\"" + parent.getIdentity() + "\" -> \"" + doc.getIdentity() + "\" [" +
                                applyStyle(parent, docs, property.getName()) + "];");
						write(doc);
					}
				}
				else
				{
					Literal value = (Literal) property.getValue();
					writer.println("\"" + parent.getIdentity() + "\" -> \"" + value.toString() + "\" [" +
                            applyStyle(parent, value, property.getName()) + "];");
					write(value);
				}
			}

			private void write(Literal literal)
			{
				writer.println("\"" + literal.toString() + "\" [" +
                        applyStyle(literal) + "];");

                if(literal instanceof Literal.UriLiteral) {
                    Literal.UriLiteral uriL = (Literal.UriLiteral) literal;

                    writer.println("\"" + uriL.toString() + "\" -> \""  + uriL.getValue().toString() + "\" [" +
                      applyStyle(uriL) + "];");
                }
			}
		};

	}
}
