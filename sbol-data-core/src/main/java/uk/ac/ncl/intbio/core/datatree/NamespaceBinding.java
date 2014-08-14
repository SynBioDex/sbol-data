package uk.ac.ncl.intbio.core.datatree;

import javax.xml.namespace.QName;
import java.net.URI;

/**
 * @author Matthew Pocock
 */
public final class NamespaceBinding {
  private final String namespaceURI;
  private final String prefix;

  public NamespaceBinding(String namespaceURI, String prefix) {
    this.namespaceURI = namespaceURI;
    this.prefix = prefix;
  }

  public String getNamespaceURI() {
    return namespaceURI;
  }

  public String getPrefix() {
    return prefix;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    NamespaceBinding that = (NamespaceBinding) o;

    if (!namespaceURI.equals(that.namespaceURI)) return false;
    if (!prefix.equals(that.prefix)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = namespaceURI.hashCode();
    result = 31 * result + prefix.hashCode();
    return result;
  }

  public QName withLocalPart(String localPart) {
    return new QName(namespaceURI, localPart, prefix);
  }

  public URI namespacedUri(String localPart) {
    return URI.create(namespaceURI + localPart);
  }
}
