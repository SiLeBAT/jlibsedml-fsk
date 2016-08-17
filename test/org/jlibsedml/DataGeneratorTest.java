package org.jlibsedml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jmathml.ASTNumber;
import org.jmathml.ASTRootNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DataGeneratorTest {

	DataGenerator dGen;
	@Before
	public void setUp() throws Exception {
		dGen = createOKDG();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	(expected=IllegalArgumentException.class)
	public final void testDataGeneratorThrowsIAEIfIDnull() {
		dGen = new DataGenerator(null, "name", ASTNumber.createNumber(2));
	}
	
	@Test
	(expected=IllegalArgumentException.class)
	public final void testDataGeneratorThrowsIAEIfMathnull() {
		dGen = new DataGenerator("id", "name", null);
	}

	@Test
	public final void testToString() {
	
		assertTrue(dGen.toString().contains(dGen.getName()));
	}

	private DataGenerator createOKDG() {
		return new DataGenerator("id", "name", new ASTRootNode());
	}

	@Test
	public final void testGetListOfVariables() {
		assertNotNull(dGen.getListOfVariables());
		assertTrue(dGen.getListOfVariables().size()==0);
	}

	@Test
	public final void testAddVariable() {
		Variable v = TestUtils.createAnyVariableWithID("id", "ref");
		dGen.addVariable(v);
		// same ID not added 
		Variable v2 = TestUtils.createAnyVariableWithID("id", "ref");
		dGen.addVariable(v2);
		assertEquals(1,dGen.getListOfVariables().size());
	}

	@Test
	public final void testRemoveVariable() {
		Variable v = TestUtils.createAnyVariableWithID("id", "ref");
		dGen.addVariable(v);
		dGen.removeVariable(v);
		assertTrue(dGen.getListOfVariables().size()==0);
	}

	@Test
	public final void testGetListOfParameters() {
		assertNotNull(dGen.getListOfParameters());
		assertTrue(dGen.getListOfParameters().size()==0);
	}

	@Test
	public final void testAddRemoveParameter() {
		Parameter p = new Parameter("id", "name", 0.4d);
		dGen.addParameter(p);
		assertTrue(dGen.getListOfParameters().size()==1);
		dGen.removeParameter(p);
		assertTrue(dGen.getListOfParameters().size()==0);
	}

	

	@Test
	public final void testGetMath() {
		assertNotNull(dGen.getMath());
	}

}
