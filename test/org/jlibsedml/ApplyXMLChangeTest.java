package org.jlibsedml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class ApplyXMLChangeTest {

    File SBMLModel = new File("TestData/BIOMD0000000012.xml");
    String sbml1 = "/sbml:sbml/sbml:model/sbml:listOfParameters/sbml:parameter[@id='beta']";

    @Test
    public void testNSIteration() throws XMLException {
        org.jdom.Document doc = createDocument(SBMLModel);
        NamespaceContextHelper nc = new NamespaceContextHelper(doc);
        nc.process(new XPathTarget(sbml1));
    }

    private static org.jdom.Document createDocument(File file)
            throws XMLException {
        SAXBuilder builder = new SAXBuilder();

        org.jdom.Document doc;
        try {
            doc = builder.build(file);
        } catch (JDOMException e) {
            throw new XMLException("Error reading file", e);
        } catch (IOException e) {
            throw new XMLException("Error reading file", e);
        }
        return doc;
    }

    private static org.jdom.Document createDocument(String str)
            throws XMLException {
        SAXBuilder builder = new SAXBuilder();

        org.jdom.Document doc;
        try {
            doc = builder.build(new StringReader(str));
        } catch (JDOMException e) {
            throw new XMLException("Error reading file", e);
        } catch (IOException e) {
            throw new XMLException("Error reading file", e);
        }
        return doc;
    }

    @Test
    public void testDeleteNode() throws XMLException, XPathEvaluationException,
            IOException, ParserConfigurationException, SAXException,
            TransformerConfigurationException,
            TransformerFactoryConfigurationError, TransformerException {
        String model = readOriginalModel(SBMLModel);

        assertTrue(model.contains("id=\"beta\""));
        String xPathToRemove = "/sbml:sbml/sbml:model/sbml:listOfParameters/sbml:parameter[@id='beta']";
        Document doc = ModelTransformationUtils
                .getXMLDocumentFromModelString(model);
        org.jdom.Document docj = createDocument(SBMLModel);

        NamespaceContextHelper nc = new NamespaceContextHelper(docj);
        nc.process(new XPathTarget(xPathToRemove));
        XPath path = createXPath(model, nc);

        ModelTransformationUtils.deleteXMLElement(doc, xPathToRemove, path);
        String altered = ModelTransformationUtils.exportChangedXMLAsString(doc);

        assertFalse(altered.contains("id=\"beta\""));
    }

    private XPath createXPath(String model, NamespaceContext con)
            throws UnsupportedEncodingException, ParserConfigurationException,
            IOException, SAXException {
        org.w3c.dom.Document doc = ModelTransformationUtils
                .getXMLDocumentFromModelString(model);
        NamedNodeMap map = doc.getFirstChild().getAttributes();
        Node n = map.getNamedItem("xmlns");
        String xmlns = "";
        if (n != null) {
            xmlns = n.getTextContent();
        }

        XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();
        XPathExpression xer;

        xpath.setNamespaceContext(con);
        return xpath;
    }

    @Test
    public void testAddNode() throws XMLException, XPathEvaluationException,
            IOException, ParserConfigurationException, SAXException,
            TransformerConfigurationException,
            TransformerFactoryConfigurationError, TransformerException {
        String NEW_XML_TO_ADD = "<parameter metaid=\"metaid_0000022\" id=\"beta22\" name=\"Ratio of protein to mRNA decay rates\" value=\"0.2\"/>";
        String model = readOriginalModel(SBMLModel);
        assertFalse(model.contains("id=\"beta22\""));
        Document doc = ModelTransformationUtils
                .getXMLDocumentFromModelString(model);
        org.jdom.Document docj = createDocument(SBMLModel);
        NamespaceContextHelper nc = new NamespaceContextHelper(docj);
        nc.process(new XPathTarget(
                "//sbml:sbml/sbml:model/sbml:listOfParameters"));
        XPath path = createXPath(model, nc);
        ModelTransformationUtils.addXMLelement(doc, NEW_XML_TO_ADD,
                "//sbml:sbml/sbml:model/sbml:listOfParameters", path);
        String altered = ModelTransformationUtils.exportChangedXMLAsString(doc);
        assertTrue(altered.contains("id=\"beta22\""));

    }

    @Test
    public void testDeleteMath() throws XMLException, XPathEvaluationException,
            IOException, ParserConfigurationException, SAXException,
            TransformerConfigurationException,
            TransformerFactoryConfigurationError, TransformerException {
        String model = readOriginalModel(SBMLModel);

        assertTrue(model.contains("id=\"beta\""));
        String xPathToRemove = "//sbml:reaction[@id='Reaction6']/sbml:kineticLaw/math:math";
        Document doc = ModelTransformationUtils
                .getXMLDocumentFromModelString(model);
        org.jdom.Document docj = createDocument(SBMLModel);
        NamespaceContextHelper nc = new NamespaceContextHelper(docj);
        nc.process(new XPathTarget(xPathToRemove));
        XPath path = createXPath(model, nc);

        ModelTransformationUtils.deleteXMLElement(doc, xPathToRemove, path);
        String altered = ModelTransformationUtils.exportChangedXMLAsString(doc);
        docj = createDocument(altered);

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
