package org.jlibsedml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class VariableTest {

	Variable symbol;
	Variable var;
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	(expected=IllegalArgumentException.class)
	public final void testVariableStringStringStringString() {
		var = new Variable(null, "name", "ref", "xpath");
	}
	@Test
	(expected=IllegalArgumentException.class)
	public final void testVariableStringStringStringString2() {
		var = new Variable("id", "name",null, "xpath");
	}
	
	@Test
	public final void testVariableWithNullNameOK() {
		var = new Variable("id", null,"ref", "xpath");
		assertTrue(var.isVariable());
		assertFalse(var.isTime());
		assertFalse(var.isSymbol());
		assertNull(var.getSymbol());
	}

	@Test
	public final void testVariableStringStringStringVariableSymbol() {
		symbol=new Variable("time", "time", "ref", VariableSymbol.TIME);
		assertFalse(symbol.isVariable());
		assertTrue(symbol.isTime());
		assertTrue(symbol.isSymbol());
		assertNull(symbol.getTarget());
	}
	
	@Test
    public final void testVariableSetMethosAlternateSymbolAndVariable() {
        symbol=new Variable("time", "time", "ref", VariableSymbol.TIME);
        assertFalse(symbol.isVariable());
        assertTrue(symbol.isTime());
        symbol.setTargetXPathStr("a/v/b");
        assertTrue(symbol.isVariable());
        assertFalse(symbol.isSymbol());
        symbol.setSymbol(VariableSymbol.TIME);
        assertFalse(symbol.isVariable());
        assertTrue(symbol.isTime());
        assertTrue(symbol.isSymbol());
    }

	
}
