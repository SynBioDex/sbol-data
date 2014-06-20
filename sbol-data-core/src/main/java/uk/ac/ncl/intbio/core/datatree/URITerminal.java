package uk.ac.ncl.intbio.core.datatree;

import java.net.URI;

public class URITerminal implements TreeTerminal{

	final private URI value;

	public URITerminal(URI value)
	{
		this.value=value;		
	}
	
	@Override
	public URI getValue()
	{		
		return this.value;
	}
	
}
