package org.jlibsedml;

import static org.junit.Assert.assertEquals;

import org.jlibsedml.modelsupport.SBMLSupport;
import org.jlibsedml.modelsupport.SBMLSupport.ParameterAttribute;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class XPathSBMLSupportTest {
	
	private SBMLSupport support=new SBMLSupport();
	
	final String EXPECTED_PARAM_VALUE="/sbml:sbml/sbml:model/sbml:listOfParameters/sbml:parameter[@id='V_mT']/@value";
	final String EXPECTED_PARAM="/sbml:sbml/sbml:model/sbml:listOfParameters/sbml:parameter[@id='V_mT']";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetXPathForGlobalParameterString() {
		assertEquals(EXPECTED_PARAM,support.getXPathForGlobalParameter("V_mT"));
	}

	@Test
	public final void testGetXPathForGlobalParameterStringParameterAttribute() {
		assertEquals(EXPECTED_PARAM_VALUE,support.getXPathForGlobalParameter("V_mT", ParameterAttribute.value));
	}



}
