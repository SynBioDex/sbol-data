package uk.ac.ncl.intbio.core.io.json;

import uk.ac.ncl.intbio.core.datatree.NameTransformer;

import javax.xml.namespace.QName;

/**
 * @author Matthew Pocock
 */
public class StringifyQName extends NameTransformer<QName, String> {
  @Override
  public String transformName(QName qName) {
		return qName.getNamespaceURI() + qName.getLocalPart();
  }

  public static StringifyQName INSTANCE = new StringifyQName();
}
