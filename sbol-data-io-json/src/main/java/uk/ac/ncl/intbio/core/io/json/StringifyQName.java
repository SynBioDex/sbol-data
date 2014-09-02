package uk.ac.ncl.intbio.core.io.json;

import uk.ac.ncl.intbio.core.datatree.Datatree;
import uk.ac.ncl.intbio.core.datatree.NameTransformer;

import javax.xml.namespace.QName;

/**
 * @author Matthew Pocock
 */
public class StringifyQName {
  public static NameTransformer<QName, String> qname2string = new NameTransformer<QName, String>() {
    @Override
    public String transformName(QName qName) {
      return "{" + qName.getNamespaceURI() + "}" + qName.getLocalPart();
    }
  };

  public static NameTransformer<String, QName> string2qname = new NameTransformer<String, QName>() {
    @Override
    public QName transformName(String f) {
      int hash = f.indexOf("}");
      String prefix = f.substring(1, hash);
      String suffix = f.substring(hash+1, f.length());
      return Datatree.QName(prefix, suffix);
    }
  };
}
