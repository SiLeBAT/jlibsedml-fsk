package org.jlibsedml.modelsupport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jlibsedml.XMLException;
import org.jlibsedml.modelsupport.SBMLSupport.CompartmentAttribute;
import org.jlibsedml.modelsupport.SBMLSupport.ParameterAttribute;
import org.jlibsedml.modelsupport.SBMLSupport.SpeciesAttribute;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SBMLSupportTest {
	final File TEST_FILE = new File("TestData/BIOMD0000000012.xml");
	SBMLSupport support;

	@Before
	public void setUp() throws Exception {
		support = new SBMLSupport();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetLanguageFor() {
		assertEquals(SUPPORTED_LANGUAGE.SBML_L1V1, support.getLanguageFor(1, 1));
		assertEquals(SUPPORTED_LANGUAGE.SBML_L2V3Rel1, support.getLanguageFor(
				2, 3));

		assertEquals(SUPPORTED_LANGUAGE.SBML_GENERIC, support.getLanguageFor(
				-200, 300));
	}

	@Test
	public final void resolveIdenitifer() {
		String xpath = "/sbml:sbml/sbml:model/sbml:listOfSpecies/sbml:species[@id='X']";
		assertEquals("X", support.getIdFromXPathIdentifer(xpath));

		String xpath2 = "/sbml:sbml/sbml:model/sbml:listOfParameters/sbml:parameters[@id='p1']";
		assertEquals("p1", support.getIdFromXPathIdentifer(xpath2));

		String xpath3 = "/sbml:sbml/sbml:model/sbml:listOfParameters/sbml:parameters[@ANY_ATRTIBUTE='p1']";
		assertEquals("p1", support.getIdFromXPathIdentifer(xpath3));

	}

	@Test
	public void testApplyXPath() throws FileNotFoundException, XPathExpressionException, ParserConfigurationException, SAXException, IOException, XMLException {
		String xpath =support.getXPathForGlobalParameter("eff");
		NodeList nl = setUpXPath(TEST_FILE, xpath);
		assertEquals(1,nl.getLength());
		assertEquals("parameter",nl.item(0).getNodeName());
		assertEquals("eff",nl.item(0).getAttributes().getNamedItem("id").getTextContent());
	}
	
	@Test
	public void testApplyXPathForSpecies() throws FileNotFoundException, XPathExpressionException, ParserConfigurationException, SAXException, IOException, XMLException {
		String xpath =support.getXPathForSpecies("PY");
		NodeList nl = setUpXPath(TEST_FILE, xpath);
		assertEquals(1,nl.getLength());
		assertEquals("species",nl.item(0).getNodeName());
		assertEquals("PY",nl.item(0).getAttributes().getNamedItem("id").getTextContent());
	}
	
	
	public void testApplyXPathForCompartment() throws FileNotFoundException, XPathExpressionException, ParserConfigurationException, SAXException, IOException, XMLException {
		String xpath =support.getXPathForCompartment("cell");
		NodeList nl = setUpXPath(TEST_FILE, xpath);
		assertEquals(1,nl.getLength());
		assertEquals("compartment",nl.item(0).getNodeName());
		assertEquals("cell",nl.item(0).getAttributes().getNamedItem("id").getTextContent());
	}
	@Test
	public  void getXPathForParameterAttributeTest () throws FileNotFoundException, XPathExpressionException, ParserConfigurationException, SAXException, IOException, XMLException {
		String xpath =support.getXPathForGlobalParameter("eff", ParameterAttribute.value);
		NodeList nl = setUpXPath(TEST_FILE, xpath);
		assertEquals(1,nl.getLength());
		assertEquals("20",nl.item(0).getTextContent());
	}
	
	@Test
	public  void getXPathForSpeciesAttributeTest () throws FileNotFoundException, XPathExpressionException, ParserConfigurationException, SAXException, IOException, XMLException {
		String xpath =support.getXPathForSpecies("PY", SpeciesAttribute.initialAmount);
		NodeList nl = setUpXPath(TEST_FILE, xpath);
		assertEquals(1,nl.getLength());
		assertEquals("0",nl.item(0).getTextContent());
	}
	
	@Test
	public  void getXPathForCompartmentAttributeTest () throws FileNotFoundException, XPathExpressionException, ParserConfigurationException, SAXException, IOException, XMLException {
		String xpath =support.getXPathForCompartment("cell", CompartmentAttribute.size);
		NodeList nl = setUpXPath(TEST_FILE, xpath);
		assertEquals(1,nl.getLength());
		assertEquals("1",nl.item(0).getTextContent());
	}

	@Test
	public final void resolveIdenitiferReturnsNullIfNotFound() {
		String xpath = "/sbml:sbml/sbml:model/sbml:listOfSpecies/sbml:species";
		assertNull(support.getIdFromXPathIdentifer(xpath));

	}

	public NodeList setUpXPath(File file, String xPath) throws ParserConfigurationException,
			FileNotFoundException, SAXException, IOException, XMLException, XPathExpressionException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true); // never forget this!
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new FileInputStream(TEST_FILE));

		XPathFactory xpf = XPathFactory.newInstance();
		XPath xpath = xpf.newXPath();
		org.jdom.Document docj = createDocument(TEST_FILE);
		NamespaceContext nc = new NamespaceContext(){
			public String getNamespaceURI(String prefix) {
				return "http://www.sbml.org/sbml/level2";
			}

			public String getPrefix(String namespaceURI) {	return null;}
			public Iterator getPrefixes(String namespaceURI) {	return null;}
			
		};
		xpath.setNamespaceContext(nc);
		XPathExpression expr = xpath.compile(xPath);

		NodeList res = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);
		return res;
	}
	

	private org.jdom.Document createDocument(File f) throws XMLException {
		SAXBuilder builder = new SAXBuilder();

		org.jdom.Document doc;
		try {
			doc = builder.build(new FileReader(f));
		} catch (JDOMException e) {
			throw new XMLException("Error reading file", e);
		} catch (IOException e) {
			throw new XMLException("Error reading file", e);
		}
		return doc;
	}

}
