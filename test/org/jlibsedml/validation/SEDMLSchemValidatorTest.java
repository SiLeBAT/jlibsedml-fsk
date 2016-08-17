package org.jlibsedml.validation;

import static org.junit.Assert.assertTrue;

import org.jlibsedml.Model;
import org.jlibsedml.SEDMLDocument;
import org.jlibsedml.SedML;
import org.jlibsedml.SedMLError;
import org.jlibsedml.Simulation;
import org.jlibsedml.TestUtils;
import org.jlibsedml.XMLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SEDMLSchemValidatorTest {

    private static final String ID1 = "A1234";
    Logger log = LoggerFactory.getLogger(SEDMLSchemValidatorTest.class);

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    // Failing due to proxy
//    @Test
//    public final void testSEDMLSchemaValidatorDetectsDuplicateMetaids()
//            throws XMLException {
//        SEDMLDocument dc = new SEDMLDocument();
//        SedML sedml = dc.getSedMLModel();
//        Model m = TestUtils.createAModel();
//        m.setMetaId(ID1);
//        Simulation sim1 = TestUtils.createAnySimulation("anyid");
//        sim1.setMetaId(ID1);
//        sedml.addSimulation(sim1);
//        sedml.addModel(m);
//        printErrors(dc);
//        assertTrue(dc.hasErrors());
//        assertTrue(dc.getErrors().get(0).getMessage()
//                .contains("There are multiple occurrences of ID value 'A1234'"));
//    }

    private void printErrors(SEDMLDocument doc) throws XMLException {
        for (SedMLError err : doc.validate()) {
            log.info("Validation error:{}", err.getMessage());
        }
    }
}
