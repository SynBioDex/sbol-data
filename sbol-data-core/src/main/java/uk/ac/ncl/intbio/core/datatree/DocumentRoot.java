package uk.ac.ncl.intbio.core.datatree;

import java.util.List;

public interface DocumentRoot extends Document<TreeTerminal> {
	
	public List<TopLevelDocument> getTopLevelDocuments();
	
	
}
