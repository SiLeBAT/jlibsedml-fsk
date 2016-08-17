package org.jlibsedml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jlibsedml.SedMLError.ERROR_SEVERITY;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SedMLErrorTest {

	SedMLError err1, err2;
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHashCode() {
		err1 = createError1();
		err2 = createError2();
		SedMLError same = createError1();
		assertEquals(err1.hashCode(), same.hashCode());
		assertFalse(err1.hashCode()== err2.hashCode());
	}

	private SedMLError createError1() {
		return new SedMLError(1, "message1", ERROR_SEVERITY.ERROR);
	}
	
	private SedMLError createError2() {
		return new SedMLError(2, "message2", ERROR_SEVERITY.ERROR);
	}

	

	@Test
	public void testGetSeverity() {
		err1= createError1();
		assertEquals(ERROR_SEVERITY.ERROR, err1.getSeverity());
	}

	@Test
	public void testGetLineNo() {
		err1= createError1();
		assertEquals(1, err1.getLineNo());
	}

	@Test
	public void testGetMessage() {
		err1= createError1();
		assertEquals("message1", err1.getMessage());
	}

	@Test
	public void testToString() {
		err1= createError1();
		assertTrue(err1.toString().contains(err1.getMessage()) && 
				err1.toString().contains(Integer.toString(err1.getLineNo()))&&
						err1.toString().contains(err1.getSeverity().toString()));
	}

	@Test
	public void testEqualsObject() {
		err1 = createError1();
		err2 = createError2();
		SedMLError same = createError1();
		assertEquals(err1, same);
		assertFalse(err1.equals(err2));
	}

}
