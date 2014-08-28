package uk.ac.ncl.intbio.core.datatree;

import javax.xml.namespace.QName;
import java.net.URI;

/**
 * A binding of a namespace URI to a prefix.
 *
 * <p>
 *   Create instances using {@link Datatree}.NamespaceBinding().
 * </p>
 *
 * @author Matthew Pocock
 */
public final class NamespaceBinding {
  private final String namespaceURI;
  private final String prefix;

  // package-private
  NamespaceBinding(String namespaceURI, String prefix) {
    this.namespaceURI = namespaceURI;
    this.prefix = prefix;
  }

  /**
   * Get the namespace URI
   *
   * @return the namespace URI string
   */
  public String getNamespaceURI() {
    return namespaceURI;
  }

  /**
   * Get the prefix.
   *
   * @return the prefix
   */
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

  /**
   * Create a QName for this namespace binding with a local part.
   *
   * @param localPart  the qname's local part
   * @return  a new QName with this namespace binding and the supplied local part
   */
  public QName withLocalPart(String localPart) {
    return new QName(namespaceURI, localPart, prefix);
  }

  /**
   * Create a URI from the namespace's prefix and a local part.
   *
   * @param localPart the URI's local part
   * @return  a new URI with this namespace URI and the supplied local part
   */
  public URI namespacedUri(String localPart) {
    return URI.create(namespaceURI + localPart);
  }
}
