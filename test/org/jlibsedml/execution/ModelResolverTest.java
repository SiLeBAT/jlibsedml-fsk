package org.jlibsedml.execution;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.jlibsedml.Libsedml;
import org.jlibsedml.SedML;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ModelResolverTest {

    ModelResolver mr;
    SedML sedml;

    @Before
    public void setUp() throws Exception {
        sedml = Libsedml.readDocument(
                new File("TestData/predatorPreySedml.xml")).getSedMLModel();
        mr = new ModelResolver(sedml);
        mr.add(new FileModelResolver());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void testGetReferenceModelString() throws JDOMException,
            IOException {
        String model = mr.getReferenceModelString(sedml
                .getModelWithId("lessPrey"));
        String xpathStr = getXPathForChange();
        SAXBuilder builder = new SAXBuilder();
        Document d = builder.build(new StringReader(model));
        XPath xpath = createXPath(xpathStr);
        List selected = xpath.selectNodes(d);
        for (int i = 0; i < selected.size(); i++) {
            if (selected.get(i) instanceof Attribute) {
                Attribute att = (Attribute) selected.get(i);
                assertEquals(105, att.getDoubleValue(), 0.001);
            }
        }
    }

    XPath createXPath(String xpathStr) throws JDOMException {
        XPath xpath = XPath.newInstance(xpathStr);
        xpath.addNamespace("sbml", "http://www.sbml.org/sbml/level2");

        return xpath;
    }

    @Test
    public final void testGetModelString() throws JDOMException, IOException {
        String model = mr.getModelString(sedml.getModelWithId("lessPrey"));
        String xpathStr = getXPathForChange();
        SAXBuilder builder = new SAXBuilder();
        Document d = builder.build(new StringReader(model));
        XPath xpath = createXPath(xpathStr);
        List selected = xpath.selectNodes(d);
        for (int i = 0; i < selected.size(); i++) {
            if (selected.get(i) instanceof Attribute) {
                Attribute att = (Attribute) selected.get(i);
                assertEquals(50, att.getDoubleValue(), 0.001);
            }
        }
    }

    String getXPathForChange() {
        return sedml.getModelWithId("lessPrey").getListOfChanges().get(0)
                .getTargetXPath().getTargetAsString();
    }
}
