package org.jlibsedml.testcases;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.jlibsedml.Libsedml;
import org.jlibsedml.SEDMLDocument;
import org.jlibsedml.SedMLError;
import org.jlibsedml.XMLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestTestCases {
    private static final int TOTAL = 11;
	String Name_Root="TestData/TestCases/Test";
    final int NUMVALIDEXAMPLES=7;
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testCases () throws XMLException{
		
		for (int i=1;i<=NUMVALIDEXAMPLES;i++){
			File f = new File(getFileName(i));
			if(!f.exists()){
				System.err.println("File does not exist-skipping");
				continue;
			}
			
			SEDMLDocument doc = Libsedml.readDocument(f);
			assertFalse(doc.hasErrors());
			
			System.err.println(getFileName(i) + " OK ");
		}
		
	}
	
	@Test
	public void testInvalidCases () throws XMLException{
		
		for (int i=NUMVALIDEXAMPLES+1;i<=TOTAL;i++){
			File f = new File(getFileName(i));
			if(!f.exists()){
				System.err.println("File does not exist-skipping");
				continue;
			}
			
			SEDMLDocument doc = Libsedml.readDocument(f);
			
			assertTrue(doc.hasErrors());
			printErrors(doc);
			System.err.println(getFileName(i) + " OK ");
		}
		
	}

	private void printErrors(SEDMLDocument doc) {
		List<SedMLError>errs = doc.getErrors();
		for (SedMLError err:errs) {
			System.err.println(err.getMessage());
		}
	}

	private String getFileName(int i) {
		return Name_Root + i + ".xml";
	}

}
