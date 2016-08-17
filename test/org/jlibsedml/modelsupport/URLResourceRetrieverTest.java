package org.jlibsedml.modelsupport;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jlibsedml.execution.IModelResolver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class URLResourceRetrieverTest {
    IModelResolver mrAPI;
    final static String BIOMODELS12_AS_URL = "http://www.ebi.ac.uk/biomodels/models-main/publ/BIOMD0000000012/BIOMD0000000012.xml";

    @Before
    public void setUp() throws Exception {
        mrAPI = new URLResourceRetriever();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testGetModelXMLForThrowsIAEIfArgIsNull() {
        mrAPI.getModelXMLFor(null);
    }

    // Failing due to proxy
//    @Test
//    public final void testGetModelXMLReturnsNotNullForAnyValidURL()
//            throws URISyntaxException {
//        assertNotNull(mrAPI.getModelXMLFor(new URI("http://www.google.com")));
//    }

    // Test failing due to proxy
//    @Test
//    public final void testGetModelXMLReturnSBML()
//            throws URISyntaxException, JDOMException, IOException {
//        String sbml = mrAPI.getModelXMLFor(new URI(BIOMODELS12_AS_URL));
//        System.err.println(sbml);
//        assertTrue(sbml.trim().contains("<sbml"));
//
//        Document rc = null;
//        SAXBuilder builder = new SAXBuilder();
//
//        InputStreamReader isr = new InputStreamReader(
//                new ByteArrayInputStream(sbml.getBytes()));
//        rc = builder.build(isr);
//
//    }

}
