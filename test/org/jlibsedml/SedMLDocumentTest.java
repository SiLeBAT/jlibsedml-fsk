package org.jlibsedml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;
import org.jlibsedml.mathsymbols.SedMLSymbolFactory;
import org.jmathml.ASTNode;
import org.jmathml.EvaluationContext;
import org.jmathml.SymbolRegistry;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SedMLDocumentTest {
	SEDMLDocument doc;
	
	@BeforeClass
	public static void beforeClass(){
	  SymbolRegistry.getInstance().addSymbolFactory(new SedMLSymbolFactory());  
	}
	
	@AfterClass
    public static void AfterClass(){
      SymbolRegistry.getInstance().removeSymbolFactory(new SedMLSymbolFactory());  
    }

	@Before
	public void before() {
		doc = new SEDMLDocument();
	}

	@Test
	public void testGetErrors() {
		assertNotNull(doc.getErrors());
		assertTrue(doc.getErrors().isEmpty());
		assertEquals(new Version(1, 1), doc.getVersion());
	}

	@Test
	public void testGetSedMLModel() {
		assertNotNull(doc.getSedMLModel());
	}
	
	@Test
    public void testMixupVErsions() throws XMLException {
        doc.getSedMLModel().addTask(new RepeatedTask("x1", "x1", false, "22"));
        doc.validate();
        assertEquals(1, doc.getErrors().size());
    }
	
	@Test
	public void testReadWriteDocument() throws JDOMException, IOException,
			XMLException {
		// XPATH_PREFIX write /read round trip
		SedML sedml = TestUtils.readSedml3();
		SEDMLDocument doc = new SEDMLDocument(sedml);
		assertFalse(doc.hasErrors());
		File tmp = new File("tmp");
		doc.writeDocument(tmp);

		SEDMLDocument doc2 = new SEDMLDocument(TestUtils.readFile(tmp));
		// tmp.delete();
		assertFalse(doc2.hasErrors());

		// now testMathML
		DataGenerator dg = sedml.getDataGeneratorWithId("Total");
		ASTNode math = dg.getMath();
		EvaluationContext con = new EvaluationContext();
		con.setValueFor("M2", 23d);
		con.setValueFor("X2", 23d);
		assertEquals(47, math.evaluate(con).getValue(), 0.0001);

		tmp.delete();
	}

	@Test
	public void testWriteDocumentToString() throws JDOMException, IOException,
			XMLException {
		String st = "<sedML xmlns=\"http://sed-ml.org/\" level=\"1\"";
		SedML sedml = TestUtils.readSedml3();
		SEDMLDocument doc = new SEDMLDocument(sedml);
		//System.err.println(doc.writeDocumentToString());
	   assertTrue(doc.writeDocumentToString().contains(st));
	}

	@Test
	public void testGetVersion() {
		Version expected = new Version(1, 1);
		assertEquals(expected, doc.getVersion());
	}

	@Test
	public void testvalidateSchemaForSedml3() throws JDOMException,
			IOException, XMLException {
		SedML sedml = TestUtils.readSedml3();
		SEDMLDocument doc = new SEDMLDocument(sedml);
		doc.validate();
		assertFalse(doc.hasErrors());
	}
	
	@Test
    public void testclearInWrongORderCanInvalidateDocument() throws JDOMException,
            IOException, XMLException {
	    SedML sedml = TestUtils.readSedml3();
	    sedml.clearModels(); // now task refer to non-existent models.
        SEDMLDocument doc = new SEDMLDocument(sedml);
        doc.validate();
        assertTrue(doc.hasErrors());
        
        sedml = TestUtils.readSedml3();
        doc = new SEDMLDocument(sedml);
        sedml.clearOutputs();
        assertFalse(doc.hasErrors());
        sedml.clearDataGenerators();
        assertFalse(doc.hasErrors());
        sedml.clearTasks();
        assertFalse(doc.hasErrors());
        sedml.clearSimulations();
        assertFalse(doc.hasErrors());
        sedml.clearModels();
        assertFalse(doc.hasErrors());
        assertEquals(0, sedml.getDataGenerators().size());
        
        
        
	    
	}

	@Test
	public void testvalidateSedml12() throws JDOMException, IOException,
			XMLException {
		SEDMLDocument doc = Libsedml.readDocument(new File(TestUtils.SEDML12));
		doc.validate();
		assertFalse(doc.hasErrors());
	}

	@Test
	public void testvalidateSedml21() throws JDOMException, IOException,
			XMLException {
	    SEDMLDocument doc = Libsedml.readDocument(new File("TestData/sedMLBIOM21.xml"));

		doc.validate();

		assertFalse(doc.hasErrors());

	}

	@Test
	(expected=XMLException.class)
	public void testvalidateSEDMLWithMissingIDAttributesFails()
			throws JDOMException, IOException, XMLException {
	    Libsedml.readDocument(new File(
				"TestData/sedml_3WithMissingIDValues.xml"));
	}

	@Test
	public void testApplYModel2ChangesToBioModels21() throws Exception {
		String original = readOriginalModel(new File(
				"TestData/BIOMD0000000021.xml"));
		assertFalse(original.contains("value=\"4.8\""));
		SEDMLDocument doc = Libsedml.readDocument(new File(
				"TestData/sedMLBIOM21.xml"));
		//System.err.println("orig is " + doc.writeDocumentToString());
		String changed = doc.getChangedModel("model2", original);
		//System.err.println("changed is " + changed);

		assertTrue(changed.contains("value=\"4.8\""));

	}

	@Test
	public void testApplYModel3ChangesToBioModels21RemovesElement()
			throws Exception {
		String original = readOriginalModel(new File(
				"TestData/BIOMD0000000021.xml"));
		SEDMLDocument doc = Libsedml.readDocument(new File(
				"TestData/sedMLBIOM21.xml"));
		assertTrue(original.contains("id=\"V_mT\""));
		String changed = doc.getChangedModel("model3", original);
		assertFalse(changed.contains("id=\"V_mT\""));

	}

	@Test
	public void testApplYModel4ChangesToBioModels21AddsElement()
			throws Exception {
		String original = readOriginalModel(new File(
				"TestData/BIOMD0000000021.xml"));
		SEDMLDocument doc = Libsedml.readDocument(new File(
				"TestData/sedMLBIOM21.xml"));
		assertFalse(original.contains("id=\"newParam1\""));
		assertFalse(original.contains("id=\"newParam2\""));
		String changed = doc.getChangedModel("model4", original);
		assertTrue(changed.contains("id=\"newParam1\""));
		assertTrue(changed.contains("id=\"newParam2\""));

	}

	@Test
	public void testApplYModel5ChangesToBioModels21RemovesV_MTAndAdds2NewPArameters()
			throws Exception {
		String original = readOriginalModel(new File(
				"TestData/BIOMD0000000021.xml"));
		SEDMLDocument doc = Libsedml.readDocument(new File(
				"TestData/sedMLBIOM21.xml"));
		assertFalse(original.contains("id=\"newParam1\""));
		assertFalse(original.contains("id=\"newParam2\""));
		String changed = doc.getChangedModel("model5", original);
		assertFalse(changed.contains("id=\"V_mT\""));
		assertTrue(changed.contains("id=\"newParam1\""));
		assertTrue(changed.contains("id=\"newParam2\""));
		
		// check inserted XML does not have SEDML namespace.
		Document doc2 = new SAXBuilder().build(new StringReader(changed));
		Iterator it = doc2.getDescendants(new ElementFilter("parameter"));
		while (it.hasNext()){
		    assertFalse(SEDMLTags.SEDML_L1V1_NS.equals(((Element)it.next()).getNamespaceURI()));
		}
	}

	@Test
	public void testApplYModel2ChangesToLVModel() throws Exception {
		String original = readOriginalModel(new File(
				"TestData/Predator_Prey.xml"));
		assertFalse(original.contains("initialConcentration=\"50\""));
		SEDMLDocument doc = Libsedml.readDocument(new File(
				"TestData/predatorPreySedml.xml"));
		String changed = doc.getChangedModel("lessPrey", original);

		assertTrue(changed.contains("initialConcentration=\"50\""));

	}

	@Test
	public void testCanResolveXPathsForModel2ChangesToLVModel()
			throws Exception {
		String original = readOriginalModel(new File(
				"TestData/Predator_Prey.xml"));

		SEDMLDocument doc = Libsedml.readDocument(new File(
				"TestData/predatorPreySedml.xml"));

		assertTrue(doc.canResolveXPathExpressions("lessPrey", original));

	}

	@Test
	public void testgetSedMLValidationReport() throws Exception {
		SedML sedml = TestUtils.readSedml3();
		SEDMLDocument doc = new SEDMLDocument(sedml);
		// null before validation
		assertNull(doc.getValidationReport());
		doc.validate();
		SedMLValidationReport rep = doc.getValidationReport();
		// now not null
		assertNotNull(rep);
		assertNotNull(rep.getErrors());
		int EXPECTED_LINE_COUNT = 79;
		// check string is printed multi-line
		assertEquals(EXPECTED_LINE_COUNT, rep.getPrettyXML().split("\n").length);

	}
	@Test
	public void  testGeneratedFile() throws XMLException{
	    SEDMLDocument doc = Libsedml.readDocument(new File(
        "TestData/sedml2.xml"));
	}

	@Test
	public void testLineNumberFoundForMissingIDXRefErrorInTask() throws Exception {

		SEDMLDocument doc = Libsedml.readDocument(new File(
				"TestData/predatorPreySedmlDefectiveXRefs.xml"));

		 doc.validate();
		 SedMLValidationReport rep = doc.getValidationReport();
		 // check lines match ( error is in 'task' element
		 String line17 = rep.getPrettyXML().split("\n")[17];
		 assertTrue(line17.contains("task"));

	}

	private String readOriginalModel(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		StringBuffer sb = new StringBuffer();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}

}
