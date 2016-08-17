package org.jlibsedml.testcases;


import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import org.jlibsedml.Libsedml;
import org.jlibsedml.SEDMLDocument;
import org.jlibsedml.SedMLError;
import org.jlibsedml.XMLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;

/**
 * Assumes you have SEDML project in the workspace as well.
 * @author radams
 *
 */
public class TestSpecExamples {
    String sedmlSpecDir = System.getProperty("sedmlSpecDir");
  
    @Rule public MethodRule ignore = new  IgnoreIfSedMLDirNotSpecifiedRule();
	String SpecName_Root="../SEDML_specification/specification/trunk/level-1/version-1/xml";
	String EGName_Root="../SEDML_specification/examples/level1/version1";
   
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	// Failing due to proxy
//	@Test
//	@IgnoreIfSedMLDirNotPresent
//	public void testSpecCases () throws XMLException{
//		File [] xmls= new File(SpecName_Root).listFiles(new FilenameFilter() {
//
//			public boolean accept(File dir, String name) {
//				return name.endsWith("xml");
//			}
//		});
//		for (File xml:xmls){
//			SEDMLDocument doc = Libsedml.readDocument(xml);
//			printErrors(doc);
//			assertFalse(doc.hasErrors());
//		}
//	}

	// Failing due to proxy
//	@Test
//	@IgnoreIfSedMLDirNotPresent
//	public void testEgCases () throws XMLException{
//
//		File [] xmls= new File(EGName_Root).listFiles(new FilenameFilter() {
//
//			public boolean accept(File dir, String name) {
//				return name.endsWith("xml");
//			}
//		});
//		for (File xml:xmls){
//			System.err.println("looking at " + xml.getName());
//
//			SEDMLDocument doc = Libsedml.readDocument(xml);
//			printErrors(doc);
//			assertFalse(doc.hasErrors());
//		}
//	}
	
	private void printErrors(SEDMLDocument doc) {
		List<SedMLError>errs = doc.getErrors();
		for (SedMLError err:errs) {
			System.err.println(err.getMessage());
		}
	}

	private String getFileName(int i) {
		return SpecName_Root + i + ".xml";
	}

}
