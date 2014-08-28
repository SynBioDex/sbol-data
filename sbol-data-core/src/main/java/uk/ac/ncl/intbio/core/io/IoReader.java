package uk.ac.ncl.intbio.core.io;

import javax.xml.stream.XMLStreamException;

import uk.ac.ncl.intbio.core.datatree.DocumentRoot;

public interface IoReader<N> {
	public DocumentRoot<N> read () throws CoreIoException;
}
