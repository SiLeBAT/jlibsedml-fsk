package org.jlibsedml;

import static org.junit.Assert.assertNotNull;

import org.jmathml.ASTRootNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ComputeChangeTest {

	ComputeChange change;
	@Before
	public void setUp() throws Exception {
		change= new ComputeChange(new XPathTarget("target"), new ASTRootNode());
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetListOfVariables() {
		assertNotNull(change.getListOfVariables());
	}

	@Test
	public final void testGetListOfParameters() {
		assertNotNull(change.getListOfParameters());
	}
}
