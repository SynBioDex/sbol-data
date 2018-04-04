package org.sbolstandard.core.schema;

import javax.xml.namespace.QName;

public interface TypeSchema {

	public static final class ExactType implements TypeSchema {
		private final QName type;

		public ExactType(QName type) {
			super();
			this.type = type;
		}

		public QName getType() {
			return type;
		}
	}
	
	public static final class HasPrefix implements TypeSchema {
		private final String prefix;

		public HasPrefix(String prefix) {
			super();
			this.prefix = prefix;
		}

		public String getPrefix() {
			return prefix;
		}
	}
	
	public static final class HasLocalName implements TypeSchema {
		private final String localName;

		public HasLocalName(String localName) {
			super();
			this.localName = localName;
		}

		public String getLocalName() {
			return localName;
		}
	}
	
	
}
