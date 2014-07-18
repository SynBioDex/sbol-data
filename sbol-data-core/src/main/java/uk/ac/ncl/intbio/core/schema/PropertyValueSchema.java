package uk.ac.ncl.intbio.core.schema;

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
	private final  IdentifiableDocumentSchema documentType;
	public IdentifiableDocumentSchema getDocumentType() {
		return documentType;
	}

	public ReferenceValue(IdentifiableDocumentSchema documentType) {
		super();
		this.documentType = documentType;
	}
}

}
