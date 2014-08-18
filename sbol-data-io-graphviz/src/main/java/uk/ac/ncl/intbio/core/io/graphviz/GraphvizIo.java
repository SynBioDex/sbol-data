package uk.ac.ncl.intbio.core.io.graphviz;

import java.io.PrintWriter;
import java.io.Writer;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import uk.ac.ncl.intbio.core.datatree.Datatree;
import uk.ac.ncl.intbio.core.datatree.Document;
import uk.ac.ncl.intbio.core.datatree.DocumentRoot;
import uk.ac.ncl.intbio.core.datatree.IdentifiableDocument;
import uk.ac.ncl.intbio.core.datatree.Literal;
import uk.ac.ncl.intbio.core.datatree.NamedProperty;
import uk.ac.ncl.intbio.core.datatree.NamespaceBinding;
import uk.ac.ncl.intbio.core.datatree.NestedDocument;
import uk.ac.ncl.intbio.core.datatree.PropertyValue;
import uk.ac.ncl.intbio.core.datatree.TopLevelDocument;
import uk.ac.ncl.intbio.core.io.CoreIoException;
import uk.ac.ncl.intbio.core.io.IoWriter;

public class GraphvizIo
{

	public IoWriter<QName> createIoWriter(final PrintWriter writer)
	{
		return new IoWriter<QName>()
		{

			@Override
			public void write(DocumentRoot<QName> document) throws CoreIoException
			{
				writer.println("digraph {");
				for (TopLevelDocument<QName> child : document.getTopLevelDocuments())
				{
					write(child);
				}
				writer.println("}");
			}

			private void write(IdentifiableDocument<QName, PropertyValue> doc)
			{
				writer.println("\"" + doc.getIdentity() + "\" [];");

				for (NamedProperty<QName, PropertyValue> property : doc.getProperties())
				{
					write(doc.getIdentity(), property);
				}

			}

			private void write(QName parent, NamedProperty<QName, PropertyValue> property)
			{
				if (property.getValue() instanceof Datatree.NestedDocuments)
				{
					Datatree.NestedDocuments<QName> docs = (Datatree.NestedDocuments<QName>) property.getValue();
					for (NestedDocument<QName> doc : docs.getDocuments())
					{
						writer.println("\"" + parent + "\" -> \"" + doc.getIdentity() + "\" [label=\"" + property.getName() + "\"];");
						write(doc);
					}
				}
				else
				{
					Literal value = (Literal) property.getValue();
					writer.println("\"" + parent + "\" -> \"" + value.toString() + "\" [label=\"" + property.getName() + "\"];");
					write(value);
				}
			}

			private void write(Literal literal)
			{
				String val;
				if (literal instanceof Literal.StringLiteral)
				{
					val = ((Literal.StringLiteral) literal).getValue();
				}
				else if (literal instanceof Literal.IntegerLiteral)
				{
					val = ((Literal.IntegerLiteral) literal).getValue().toString();
				}
				else if (literal instanceof Literal.QNameLiteral)
				{
					Literal.QNameLiteral ql = (Literal.QNameLiteral) literal;
					val = ql.getValue().getLocalPart();
				}
				else if (literal instanceof Literal.UriLiteral)
				{
					Literal.UriLiteral ul = (Literal.UriLiteral) literal;
					val = ul.getValue().toString();
				}
				else
				{
					throw new IllegalStateException("Unknown type of literal: " + literal.getClass().getName() + " extends "
							+ literal.getClass().getInterfaces()[0].getName());
				}
				writer.println("\"" + literal.toString() + "\" [label=\"" + prettyClip(15, val) + "\"];");
			}
		};

	}

	private static String prettyClip(int maxLen, String toClip)
	{
		if (toClip.length() <= maxLen)
			return toClip;
		else
			return toClip.substring(0, maxLen - 3) + "...";

	}
}
