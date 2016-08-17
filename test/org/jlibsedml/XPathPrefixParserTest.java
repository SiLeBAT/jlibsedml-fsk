package org.jlibsedml;


import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class XPathPrefixParserTest {

	static Pattern test=Pattern.compile("(/|@)(\\w*):");
	String sbml1="/sbml:sbml/sbml:model/sbml:listOfSpecies/sbml:species[@id='PY']";
	String noPrefix="/:sbml/sbml:model/sbml:listOfSpecies/sbml:species[@myprefix:id='PY']";
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	@Test
	public void testGetPRefixes(){
		getPrefixes(sbml1);
	}
	
	@Test
	public void testGetNoPrefixes(){
		getPrefixes(noPrefix);
	}
	
	
	
	
	public static  Set<String> getPrefixes(String target) {
		Matcher m = test.matcher(target);
		Set<String> prefixes = new HashSet<String>();
		while(m.find()){
			prefixes.add(m.group(2));
		}
		return prefixes;
		
		
	}
	
	

}
