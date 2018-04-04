package org.sbolstandard.core.io.graphviz;

import java.io.PrintWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.sbolstandard.core.datatree.Datatree;
import org.sbolstandard.core.datatree.DocumentRoot;
import org.sbolstandard.core.datatree.IdentifiableDocument;
import org.sbolstandard.core.datatree.Literal;
import org.sbolstandard.core.datatree.NamedProperty;
import org.sbolstandard.core.datatree.NestedDocument;
import org.sbolstandard.core.datatree.PropertyValue;
import org.sbolstandard.core.datatree.TopLevelDocument;
import org.sbolstandard.core.io.CoreIoException;
import org.sbolstandard.core.io.IoWriter;

public class GraphvizIo
{
  private DocumentStyler documentStyler = Styler.document.identityAsLabel;
  private LiteralStyler literalStyler = Styler.literal.valueAslabel;
  private EdgeStyler edgeStyler = Styler.edge.nameAsLabel;
  private LinkerStyler linkStyler = Styler.linker.all(/*Styler.linker.nonConstraint,*/ Styler.linker.dashed);

  private String applyStyle(IdentifiableDocument<QName> doc) {
    Map<String, String> styles = new HashMap<>();
    documentStyler.applyStyle(styles, doc);
    return flatten(styles);
  }

  private String applyStyle(Literal<QName> value) {
    Map<String, String> styles = new HashMap<>();
    literalStyler.applyStyle(styles, value);
    return flatten(styles);
  }

  private String applyStyle(IdentifiableDocument<QName> from,
                            PropertyValue<QName> to,
                            QName name) {
    Map<String, String> styles = new HashMap<>();
    edgeStyler.applyStyle(styles, from, to, name);
    return flatten(styles);
  }

  private String applyStyle(Literal.UriLiteral<QName> uriLiteral) {
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

        writer.print("{rank=same ");
        for (TopLevelDocument<QName> child : document.getTopLevelDocuments())
        {
          writer.print("\"" + child.getIdentity().toString() + "\" ");
        }
        writer.println("}");

        writer.println("}");
      }

      private void write(IdentifiableDocument<QName> document)
      {
        writer.println("\"" + document.getIdentity() + "\" [" + applyStyle(document) + "];");

        for (NamedProperty<QName> property : document.getProperties())
        {
          write(document, property);
        }

        writer.print("{rank=same ");
        for (NamedProperty<QName> property : document.getProperties())
        {
          PropertyValue<QName> pv = property.getValue();
          if(pv instanceof NestedDocument) {
            NestedDocument<QName> doc = (NestedDocument<QName>) pv;
            writer.print("\"" + doc.getIdentity().toString() + "\" ");
          } else {
            Literal<QName> literal = (Literal<QName>) pv;
            writer.print("\"" + literal.toString() + "\" ");
          }
        }
        writer.println("}");
      }

      private void write(IdentifiableDocument<QName> parent, NamedProperty<QName> property)
      {
        if (property.getValue() instanceof NestedDocument)
        {
          NestedDocument<QName> doc = (NestedDocument<QName>) property.getValue();
          writer.println("\"" + parent.getIdentity() + "\" -> \"" + doc.getIdentity() + "\" [" +
                  applyStyle(parent, doc, property.getName()) + "];");
          write(doc);
        }
        else
        {
          Literal<QName> value = (Literal<QName>) property.getValue();
          writer.println("\"" + parent.getIdentity() + "\" -> \"" + value.toString() + "\" [" +
                  applyStyle(parent, value, property.getName()) + "];");
          write(value);
        }
      }

      private void write(Literal<QName> literal)
      {
        writer.println("\"" + literal.toString() + "\" [" +
                applyStyle(literal) + "];");

        if(literal instanceof Literal.UriLiteral) {
          Literal.UriLiteral<QName> uriL = (Literal.UriLiteral<QName>) literal;

          writer.println("\"" + uriL.toString() + "\" -> \""  + uriL.getValue().toString() + "\" [" +
                  applyStyle(uriL) + "];");
        }
      }
    };

  }
}
