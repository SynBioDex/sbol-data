package uk.ac.ncl.intbio.core.datatree;

import java.util.List;

public interface DocumentRoot<N> extends Document<N, Literal> {
	
	public List<TopLevelDocument<N>> getTopLevelDocuments();
	
	
}
