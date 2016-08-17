package org.jlibsedml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class VersionTest {
    Version v1, v2, v3, v4;
	@Before
	public void before (){
		
	}
	@Test
	public void testIsEarlierThan() {
		v1 = new Version (1,1);
		v2 = new Version (2,1);
		v3 = new Version (1,2);
		v4 = new Version (2,2);
		assertTrue(v1.isEarlierThan(v2));
		assertTrue(v1.isEarlierThan(v3));
		assertTrue(v2.isEarlierThan(v4));
	}

	@Test
	public void testEqualsObject() {
		v1 = new Version (1,1);
		v2 = new Version (1,1);
		assertEquals(v1,v2);
	}

}
