package uk.ac.ncl.intbio.core.datatree;

public class StringTerminal implements TreeTerminal {

	final private String value;

	public StringTerminal(String value)
	{
		this.value=value;		
	}
	
	@Override
	public String getValue()
	{		
		return this.value;
	}
}
