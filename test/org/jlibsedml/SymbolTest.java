package org.jlibsedml;


import static org.junit.Assert.*;

import java.util.List;

import org.jmathml.ASTCi;
import org.jmathml.ASTSymbol;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SymbolTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	@Test
public void testSymbolReadWrite() throws XMLException{
		SedML sedml = TestUtils.createValidOutputConfig();
		ASTSymbol symbol = new ASTSymbol("max");
		symbol.setDefinitionURL("http://www.biomodels.net/sed-ml/#max");
		symbol.setEncoding("text");
		symbol.addChildNode(new ASTCi("x"));
		DataGenerator dg = new DataGenerator("ddg", null,symbol);
		dg.addVariable(new Variable("x", null, "taskid", "xpath"));
		sedml.addDataGenerator(dg);
		List<SedMLError>errs = new SEDMLDocument(sedml).validate();
		assertEquals(1, errs.size());
	}
}
