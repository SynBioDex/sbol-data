package org.sbolstandard.core.io;

import javax.xml.stream.XMLStreamException;

import org.sbolstandard.core.datatree.DocumentRoot;

public interface IoReader<N> {
	public DocumentRoot<N> read () throws CoreIoException;
}
