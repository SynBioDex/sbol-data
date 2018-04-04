package org.sbolstandard.core.schema;

import java.net.URI;

public interface PropertyValueSchema {

	public static final class XsdValue implements PropertyValueSchema
	{
		private final String xsdType;

		public XsdValue(String xsdType) {
			super();
			this.xsdType = xsdType;
		}

		public String getXsdType() {
			return xsdType;
		}
		
	}

public static final class DocumentValue implements PropertyValueSchema
{
	private final IdentifiableDocumentSchema documentType;

	public IdentifiableDocumentSchema getDocumentType() {
		return documentType;
	}

	public DocumentValue(IdentifiableDocumentSchema documentType) {
		super();
		this.documentType = documentType;
	}
	
}

public static final class ReferenceValue implements PropertyValueSchema
{
	private final  URI referenceType;
	public URI getDocumentType() {
		return referenceType;
	}

	public ReferenceValue(URI documentType) {
		super();
		this.referenceType = documentType;
	}
}

}
