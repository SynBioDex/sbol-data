package org.sbolstandard.core.io;

import org.sbolstandard.core.datatree.DocumentRoot;

public interface IoWriter<N> {
	public void write (DocumentRoot<N> document) throws CoreIoException;
}
