package org.jlibsedml.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jdom.Document;
import org.jlibsedml.Libsedml;
import org.jlibsedml.SEDMLDocument;
import org.jlibsedml.SedMLError;
import org.jlibsedml.XMLException;
import org.jlibsedml.extensions.XMLUtils;
import org.jlibsedml.validation.ModelCyclesDetector;
import org.jlibsedml.validation.RawContentsSchemaValidator;
import org.jlibsedml.validation.SchematronValidator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SedMLValidateL1V2Test {
    File l1v2exampleDir = new File("TestData/l1v2");
    Logger log = LoggerFactory.getLogger(getClass());

    @Test
    public void basicValidationAgainstSchema() throws XMLException, IOException {
        log.info("xmlSchema");
        Collection<File> l1v2examples = getExampleFiles();
        for (File example : l1v2examples) {
            log.info("Looking at {}", example.getName());
            assertTrue(Libsedml.isSEDML(example));
            String sedmlString = FileUtils.readFileToString(example);
            List<SedMLError> errors = new RawContentsSchemaValidator(
                    sedmlString).validate();
            printErrors(errors);
            assertEquals(0, errors.size());
        }
    }
    @Test
    public void fullValidation() throws XMLException, IOException {
        log.info("full");
        Collection<File> l1v2examples = getExampleFiles();
        for (File example : l1v2examples) {
            log.info("Looking at {}", example.getName());
            SEDMLDocument doc = Libsedml.readDocument(example);
            printErrors(doc.getErrors());
            assertEquals(0, doc.getErrors().size());
        }
    }
    
    @Test
    public void noModelCycles () throws XMLException, IOException {
        log.info("model Cycles");
        Collection<File> l1v2examples = getExampleFiles();
        for (File example : l1v2examples) {
            log.info("Looking at {}", example.getName());
            SEDMLDocument doc = Libsedml.readDocument(example);
            String sedmlString = FileUtils.readFileToString(example);
            Document jdomdoc = new XMLUtils().readDoc(sedmlString);
            List<SedMLError> errors = new ModelCyclesDetector(doc.getSedMLModel(), jdomdoc).validate();
            printErrors(errors);
            assertEquals(0, errors.size());
        }
    }

    @Test
    public void schematron () throws XMLException, IOException {
        log.info("Schematron");
        Collection<File> l1v2examples = getExampleFiles();
        for (File example : l1v2examples) {
            log.info("Looking at {}", example.getName());
            SEDMLDocument doc = Libsedml.readDocument(example);
            String sedmlString = FileUtils.readFileToString(example);
            Document jdomdoc = new XMLUtils().readDoc(sedmlString);
            List<SedMLError> errors = new SchematronValidator(jdomdoc, doc.getSedMLModel()).validate();
            printErrors(errors);
            assertEquals(0, errors.size());
        }
    }
    private Collection<File> getExampleFiles() {
        Collection<File> l1v2examples = FileUtils.listFiles(l1v2exampleDir,
                new String[] { "xml" }, false);
        return l1v2examples;
    }

    private void printErrors(List<SedMLError> errors) {
        System.err.println("There are " + errors.size() + " errors. ");
        for (SedMLError e : errors) {
            System.err.println(e);
        }
    }
}
