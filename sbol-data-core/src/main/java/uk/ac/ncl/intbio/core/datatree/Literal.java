package uk.ac.ncl.intbio.core.datatree;

import java.net.URI;

import javax.xml.namespace.QName;

public interface Literal extends PropertyValue
{
	public Object getValue();

  public static interface StringLiteral extends Literal {
    @Override
    String getValue();
  }

  public static interface UriLiteral extends Literal {
    @Override
    URI getValue();
  }

  public static interface IntegerLiteral extends Literal {
    @Override
    Integer getValue();
  }
  
  public static interface QNameLiteral extends Literal {
	    @Override
	    QName getValue();
	  }
	  
  
}
