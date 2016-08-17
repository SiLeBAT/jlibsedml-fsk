package org.jlibsedml.extensions;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class XSLTSchematronGenerator {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void test() throws TransformerException, IOException {
        StreamSource xslSource = new StreamSource("resources/iso_svrl_for_xslt1.xsl");
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer(xslSource);
        StreamResult target = new StreamResult("resources/validatorl1v1.xsl");
        t.transform(new StreamSource("resources/schematronL1V1.xml"),target);
        
        StreamResult target2 = new StreamResult("resources/validatorl1v2.xsl");
        t.transform(new StreamSource("resources/schematronL1V2.xml"),target2);
       
        
        StreamSource xslSource2 = new StreamSource("resources/validatorl1v1.xsl");
        TransformerFactory tf2 = TransformerFactory.newInstance();
        Transformer t2 = tf2.newTransformer(xslSource2);
        File out = File.createTempFile("xslt", ".xml");
        StreamResult target3 = new StreamResult(out.toURI().toString());
        t2.transform(new StreamSource("TestData/predatorPreySedml.xml"),target3);
        assertTrue(out.length() > 100);
    }

}
