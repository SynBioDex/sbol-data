package uk.ac.ncl.intbio.core.io;

import uk.ac.ncl.intbio.core.datatree.DocumentRoot;

public interface IoWriter {
	public void write (DocumentRoot document) throws CoreIoException;
}
