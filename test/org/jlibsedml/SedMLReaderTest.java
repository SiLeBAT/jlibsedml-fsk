package org.jlibsedml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.jdom.JDOMException;
import org.junit.Test;


public class SedMLReaderTest {
    
	public static final String  SEDML3= "TestData/sedml_3.xml";
	
	
	@Test
	public void testFileRead3() throws JDOMException, IOException, XMLException{		
			SedML sedML =TestUtils.readSedml3();
			assertEquals(1,sedML.getModels().size());
			assertEquals(4, ((Plot2D)sedML.getOutputs().get(0)).getListOfCurves().size());
			assertEquals(5, sedML.getDataGenerators().size());
			assertEquals(1, sedML.getSimulations().size());
			assertEquals(2, sedML.getModels().get(0).getListOfChanges().size());
			assertEquals(1, sedML.getTasks().size());
			assertNotNull(sedML.getSimulations().get(0).getAlgorithm());
			assertEquals(0,new SEDMLDocument(sedML).validate().size());
	}
	
	@Test
	public void testFileRead12() throws JDOMException, IOException, XMLException{		
			SedML sedML =TestUtils.readSedml12();
			assertEquals(2,sedML.getModels().size());
			assertEquals(3, ((Plot2D)sedML.getOutputs().get(0)).getListOfCurves().size());
			assertEquals(11, sedML.getDataGenerators().size());
			assertEquals(2, sedML.getSimulations().size());
			assertEquals(2, sedML.getModels().get(1).getListOfChanges().size());
			assertEquals(2, sedML.getTasks().size());
			assertNotNull(sedML.getSimulations().get(0).getAlgorithm());
			assertEquals(0,new SEDMLDocument(sedML).validate().size());
		
	}
	
	@Test
	public void testModel() throws JDOMException, IOException, XMLException {
		SedML sedML =TestUtils.readSedml3();
		Model model= sedML.getModels().get(0);
		assertEquals("urn:sedml:language:sbml", model.getLanguage());
		assertEquals("model1", model.getId());
		assertEquals("Biomodels3", model.getName());
		
	}
}
