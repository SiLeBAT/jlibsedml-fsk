package org.jlibsedml.validation;

import static org.junit.Assert.*;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jlibsedml.Annotation;
import org.jlibsedml.IIdentifiable;
import org.jlibsedml.Libsedml;
import org.jlibsedml.Model;
import org.jlibsedml.SEDMLDocument;
import org.jlibsedml.SedML;
import org.jlibsedml.TestUtils;
import org.jlibsedml.XMLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AnnotationValidatorTest {

    class AnnotationValidatorTSS extends AnnotationValidator {
        public AnnotationValidatorTSS(SedML sedml, Document doc) {
            super(sedml, doc);
        }

        int getLineForElement(String elName, String elID, Document doc) {
            return 0;
        }

        int getLineNumberOfError(String elementKind,
                IIdentifiable identifiable) {
            return 0;
        }
    }

    AnnotationValidator validator = null;
    SEDMLDocument doc = null;

    @Before
    public void setUp() throws Exception {
        doc = Libsedml.createDocument();
        validator = new AnnotationValidatorTSS(doc.getSedMLModel(), null);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testValidate() throws XMLException {
        Model model = TestUtils.createAModel();

        Namespace n1 = Namespace.getNamespace("x", "http://x.com");
        Element topLevel = new Element("x");
        topLevel.setNamespace(n1);

        Namespace n2 = Namespace.getNamespace("y", "http://y.com");
        Element topLevel2 = new Element("y");
        topLevel2.setNamespace(n2);

        Annotation an = new Annotation(topLevel);
        an.addElement(topLevel2);
        model.setAnnotation(an);
        doc.getSedMLModel().addModel(model);
        // 2 separate annotations
        assertTrue(validator.validate().isEmpty());

        // now add annotation 3 with same namespace
        Element topLevel3 = new Element("z");
        topLevel3.setNamespace(n2);
        an.addElement(topLevel3);
        assertFalse(validator.validate().isEmpty());
        
        an.removeElement(topLevel3);

    }

}
