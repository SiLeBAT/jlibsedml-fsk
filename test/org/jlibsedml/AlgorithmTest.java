package org.jlibsedml;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AlgorithmTest {
	Algorithm a1 = new Algorithm("001");
	Algorithm same = new Algorithm("001");
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	(expected=IllegalArgumentException.class)
	public final void testAlgorithmThrowsIAEIfKisaoIDNull() {
		new Algorithm(null);
	}
	
	@Test
	(expected=IllegalArgumentException.class)
	public final void testAlgorithmThrowsIAEIfKisaoIDEmpty() {
		new Algorithm("");
	}

	@Test
	public final void testHashCodeBasedOnKisaoID() {	
		assertEquals(a1.hashCode(), same.hashCode());
	}

	@Test
	public final void testEqualsBasedOnKisaoID() {
		assertEquals(a1, same);
	}
}
