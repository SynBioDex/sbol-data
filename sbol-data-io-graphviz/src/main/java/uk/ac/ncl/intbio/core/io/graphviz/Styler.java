package uk.ac.ncl.intbio.core.io.graphviz;

import uk.ac.ncl.intbio.core.datatree.IdentifiableDocument;
import uk.ac.ncl.intbio.core.datatree.Literal;
import uk.ac.ncl.intbio.core.datatree.PropertyValue;

import javax.xml.namespace.QName;
import java.util.Map;

/**
 * Created by caroline on 19/08/2014.
 */
public class Styler {
    public static class document {
        static DocumentStyler identityAsLabel = new DocumentStyler() {
            @Override
            public void applyStyle(Map<String, String> styleMap, IdentifiableDocument<QName, ? extends PropertyValue> document) {
              styleMap.put("label", document.getIdentity().toString());
            }
        };
    }

    public static class literal {
        static LiteralStyler valueAslabel = new LiteralStyler() {
            @Override
            public void applyStyle(Map<String, String> styleMap, Literal value) {
                if(value instanceof Literal.StringLiteral) {
                    styleMap.put("label", prettyClip(30, value.getValue().toString()));
                } else {
                    styleMap.put("label", value.getValue().toString());
                }
            }
        };
    }

    public static class edge {
        static EdgeStyler nameAsLabel = new EdgeStyler() {
            @Override
            public void applyStyle(Map<String, String> styleMap, IdentifiableDocument<QName, ? extends PropertyValue> from, PropertyValue to, QName edgeType) {
                styleMap.put("label", edgeType.getLocalPart());
            }
        };
    }

    public static class linker {
        static LinkerStyler nonConstraint = new LinkerStyler() {
            @Override
            public void applyStyle(Map<String, String> styleMap, Literal.UriLiteral link) {
                styleMap.put("constraint", "false");
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
