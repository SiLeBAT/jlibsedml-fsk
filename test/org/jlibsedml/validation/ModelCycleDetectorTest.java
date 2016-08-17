package org.jlibsedml.validation;

import static org.junit.Assert.assertTrue;

import org.jdom.Document;
import org.jlibsedml.IIdentifiable;
import org.jlibsedml.Model;
import org.jlibsedml.SEDMLDocument;
import org.jlibsedml.SedML;
import org.jlibsedml.XMLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ModelCycleDetectorTest {

    class ModelCycleDetectorTSS extends ModelCyclesDetector {
        ModelCycleDetectorTSS(SedML tc, Document doc) {
            super(tc, doc);
        }

        int getLineNumberOfError(String elementKind, IIdentifiable identifiable) {
            return 0;
        }
    }

    ModelCycleDetectorTSS tss;
    ModelCyclesDetector api;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void testValidate() throws XMLException {
        tss = new ModelCycleDetectorTSS(createSedMLWithModelCycles(), null);
        api = tss;
        assertTrue(api.validate().size() > 0);
    }

    @Test
    public final void testValidate2() throws XMLException {
        tss = new ModelCycleDetectorTSS(createSedMLWithModelCycles2(), null);
        api = tss;
        assertTrue(api.validate().size() > 0);
    }

    private SedML createSedMLWithModelCycles() {
        SedML sedml = new SEDMLDocument().getSedMLModel();
        Model m1 = new Model("id1", null, "sbml", "id2");
        Model m2 = new Model("id2", null, "sbml", "id1");
        sedml.addModel(m1);
        sedml.addModel(m2);
        return sedml;
    }

    private SedML createSedMLWithModelCycles2() {
        SedML sedml = new SEDMLDocument().getSedMLModel();
        Model m1 = new Model("id1", null, "sbml", "id3");
        Model m2 = new Model("id2", null, "sbml", "id1");
        Model m3 = new Model("id3", null, "sbml", "id2");
        sedml.addModel(m1);
        sedml.addModel(m3);
        sedml.addModel(m2);

        return sedml;
    }
}
