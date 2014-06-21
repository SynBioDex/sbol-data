package uk.ac.ncl.intbio.core.io;

import uk.ac.ncl.intbio.core.datatree.DocumentRoot;

public interface IoWriter<N> {
	public void write (DocumentRoot<N> document) throws CoreIoException;
}
