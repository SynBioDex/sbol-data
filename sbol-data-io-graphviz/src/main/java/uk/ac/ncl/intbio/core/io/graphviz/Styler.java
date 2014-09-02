package uk.ac.ncl.intbio.core.io.graphviz;

import uk.ac.ncl.intbio.core.datatree.Func;
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
        public static DocumentStyler all(final DocumentStyler ... stylers) {
            return new DocumentStyler() {
                @Override
                public void applyStyle(Map<String, String> styleMap, IdentifiableDocument<QName> document) {
                    for(DocumentStyler s : stylers) {
                        s.applyStyle(styleMap, document);
                    }
                }
            };
        }

        public static DocumentStyler apply(final MapMod mm) {
            return new DocumentStyler() {
                @Override
                public void applyStyle(Map<String, String> styleMap, IdentifiableDocument<QName> document) {
                    mm.apply(styleMap);
                }
            };
        }

        public static DocumentStyler identityAsLabel = new DocumentStyler() {
            @Override
            public void applyStyle(Map<String, String> styleMap, IdentifiableDocument<QName> document) {
              styleMap.put("label", document.getIdentity().toString());
            }
        };
    }

    public static class literal {
        public static LiteralStyler all(final LiteralStyler ... stylers) {
            return new LiteralStyler() {
                @Override
                public void applyStyle(Map<String, String> styleMap, Literal<QName> value) {
                    for(LiteralStyler s : stylers) {
                        s.applyStyle(styleMap, value);
                    }
                }
            };
        }

        public static LiteralStyler apply(final MapMod mm) {
            return new LiteralStyler() {
                @Override
                public void applyStyle(Map<String, String> styleMap, Literal<QName> value) {
                    mm.apply(styleMap);
                }
            };
        }

        public static LiteralStyler valueAslabel = new LiteralStyler() {
            @Override
            public void applyStyle(Map<String, String> styleMap, Literal<QName> value) {
                if(value instanceof Literal.StringLiteral) {
                    styleMap.put("label", prettyClip(30, value.getValue().toString()));
                } else {
                    styleMap.put("label", value.getValue().toString());
                }
            }
        };
    }

    public static class edge {
        static EdgeStyler apply(final MapMod mm) {
            return new EdgeStyler() {
                @Override
                public void applyStyle(Map<String, String> styleMap,
                                       IdentifiableDocument<QName> from,
                                       PropertyValue<QName> to,
                                       QName edgeType)
                {
                    mm.apply(styleMap);
                }
            };
        }

        static EdgeStyler nameAsLabel = new EdgeStyler() {
            @Override
            public void applyStyle(Map<String, String> styleMap, IdentifiableDocument<QName> from,
                                   PropertyValue<QName> to,
                                   QName edgeType)
            {
                styleMap.put("label", edgeType.getLocalPart());
            }
        };
    }

    public static class linker {
        static LinkerStyler all(final LinkerStyler ... styles) {
            return new LinkerStyler() {
                @Override
                public void applyStyle(Map<String, String> styleMap,
                                       Literal.UriLiteral<QName> link) {
                    for(LinkerStyler s : styles) {
                        s.applyStyle(styleMap, link);
                    }
                }
            };
        }

        static LinkerStyler apply(final MapMod mm) {
            return new LinkerStyler() {
                @Override
                public void applyStyle(Map<String, String> styleMap,
                                       Literal.UriLiteral<QName> link) {
                    mm.apply(styleMap);
                }
            };
        }

        static LinkerStyler nonConstraint = apply(mapMod.set("constraint", "false"));
        static LinkerStyler dashed = apply(mapMod.set("style", "dashed"));
    }

    private static String prettyClip(int maxLen, String toClip)
   	{
   		if (toClip.length() <= maxLen)
   			return toClip;
   		else
   			return toClip.substring(0, maxLen - 3) + "...";

   	}

    public static class mapMod {
        public static MapMod set(final String key, final String value) {
            return new MapMod() {
                @Override
                public void apply(Map<String, String> map) {
                    map.put(key, value);
                }
            };
        }

        public static <V> Func<V, MapMod> withValue(final String key, final Func<V, String> vf) {
            return new Func<V, MapMod>() {
                @Override
                public MapMod apply(V v) {
                    return set(key, vf.apply(v));
                }
            };
        }
    }


}
