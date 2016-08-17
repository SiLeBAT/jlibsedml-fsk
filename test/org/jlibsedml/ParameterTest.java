package org.jlibsedml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ParameterTest {
	Parameter p1, p2,p3;

	@Before
	public void setUp() throws Exception {
		p1 = new Parameter("id", "name", 0.2);
		p2 = new Parameter("id", null, 0.3);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testParameterParameter() {
		p2 = new Parameter(p1);
		assertEquals(p1,p2);
	}

	@Test
	public final void testParameterStringStringStringAllowsNullName() {
		p3 = new Parameter("id", null, 0.4);
		
	}

	@Test
	public final void testIsNameSet() {
		assertTrue(p1.isNameSet());
	}

	@Test
	public final void testGetNameReturnsIdIfNameNull() {
		assertEquals("name", p1.getName());
		assertEquals("id", p2.getId());
	}



}
