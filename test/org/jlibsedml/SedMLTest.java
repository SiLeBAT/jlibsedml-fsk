package org.jlibsedml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jlibsedml.modelsupport.SBMLSupport;
import org.jlibsedml.validation.SEDMLSchemaValidator;
import org.jmathml.ASTNumber;
import org.jmathml.ASTRootNode;
import org.jmathml.FormulaFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SedMLTest {

    SedML sedml;
    final String EXPECTED_ID = "id";
    final String NO_ID = "noid";
    Logger log = LoggerFactory.getLogger(SedMLTest.class);

    @Before
    public void setUp() throws Exception {
        sedml = TestUtils.createEmptySedml();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void testGetModel() {
        Model model = createModel();
        sedml.setModels(Arrays.asList(new Model[]{model}));
        assertNotNull(sedml.getModelWithId(EXPECTED_ID));
        assertNull(sedml.getModelWithId(NO_ID));

    }

    private Model createModel() {
        Model model = new Model(EXPECTED_ID, "name", "sbml", "urn:aaa");
        return model;
    }

    @Test
    public final void testIsModel() {
        Model model = createModel();
        sedml.setModels(Arrays.asList(new Model[]{model}));
        assertTrue(sedml.isModel(EXPECTED_ID));
        assertFalse(sedml.isModel(NO_ID));
    }

    @Test
    public final void testGetSimulation() {
        Simulation sim = TestUtils.createAnySimulation(EXPECTED_ID);
        sedml.setSimulations(Arrays.asList(new Simulation[]{sim}));
        assertNotNull(sedml.getSimulation(EXPECTED_ID));
        assertNull(sedml.getSimulation(NO_ID));
    }


    @Test
    public final void testGetTask() {
        AbstractTask task = createTask();
        sedml.setTasks(Arrays.asList(new AbstractTask[]{task}));
        assertNotNull(sedml.getTaskWithId(EXPECTED_ID));
        assertNull(sedml.getTaskWithId(NO_ID));
    }

    private Task createTask() {
        Task task = new Task(EXPECTED_ID, "name", "modelref", "simref");
        return task;
    }

    @Test
    public final void testGetDataGenerator() {
        DataGenerator dg = new DataGenerator(EXPECTED_ID, "name", new ASTRootNode());
        sedml.setDataGenerators(Arrays.asList(new DataGenerator[]{dg}));
        assertNotNull(sedml.getDataGeneratorWithId(EXPECTED_ID));
        assertNull(sedml.getDataGeneratorWithId(NO_ID));
    }

    @Test
    public final void testAddRemoveModel() {
        sedml.addModel(createModel());
        assertEquals(1, sedml.getModels().size());
        sedml.addModel(createModel());
        assertEquals(1, sedml.getModels().size());// duplicate not added.
        assertTrue(sedml.removeModel(createModel()));
        assertEquals(0, sedml.getModels().size());
    }

    @Test
    public final void testAddRemoveSimulation() {
        assertTrue(sedml.addSimulation(TestUtils.createAnySimulation(EXPECTED_ID)));
        assertEquals(1, sedml.getSimulations().size());
        assertFalse(sedml.addSimulation(TestUtils.createAnySimulation(EXPECTED_ID)));
        assertEquals(1, sedml.getSimulations().size());// duplicate not added.
        assertTrue(sedml.removeSimulation(TestUtils.createAnySimulation(EXPECTED_ID)));
        assertEquals(0, sedml.getSimulations().size());// duplicate not added.
    }

    @Test
    public final void testAddRemoveTask() {
        assertTrue(sedml.addTask(createTask()));
        assertEquals(1, sedml.getTasks().size());
        assertFalse(sedml.addTask(createTask()));
        assertEquals(1, sedml.getTasks().size());// duplicate not added.
        assertTrue(sedml.removeTask(createTask()));
        assertEquals(0, sedml.getTasks().size());// duplicate not added.
    }

    @Test
    public final void testAddOutput() {
        Output o1 = createOutput();
        assertTrue(sedml.addOutput(o1));
        assertEquals(1, sedml.getOutputs().size());
        assertFalse(sedml.addOutput(createOutput()));
        assertEquals(1, sedml.getOutputs().size());// duplicate not added.
        assertEquals(o1, sedml.getOutputWithId(o1.getId()));// duplicate not added.
        assertTrue(sedml.removeOutput(o1));
        assertEquals(0, sedml.getOutputs().size());// duplicate not added.
    }

    @Test
    public final void testAddRemoveDataGenerator() {
        DataGenerator dg = new DataGenerator("id", "name", ASTNumber.E());
        assertTrue(sedml.addDataGenerator(dg));
        assertEquals(1, sedml.getDataGenerators().size());
        assertFalse(sedml.addDataGenerator(dg));
        assertEquals(1, sedml.getDataGenerators().size());
        assertTrue(sedml.removeDataGenerator(dg));
        assertEquals(0, sedml.getDataGenerators().size());
    }

    private Output createOutput() {
        return new Plot2D("output", "name");
    }

    @Test
    public void testGetChangesReturnsInSchemaOrder() throws XMLException {
        SedML sed = new SEDMLDocument().getSedMLModel();
        Output o1 = TestUtils.createEmptyReport();
        Output o2 = TestUtils.createAnyPlot2d();
        Output o3 = TestUtils.createAnyPlot3d();
        // add in non-schema order
        List<Output> allOutput = Arrays.asList(new Output[]{o1, o2, o3});
        for (Output x : allOutput) {
            sed.addOutput(x);
        }

        List<SedMLError> errs = new SEDMLSchemaValidator(sed).validate();

        assertTrue(errs.isEmpty());
    }

    @Test
    public void testaddSimpleSpeciesAsOutput() {
        SedML sed = new SEDMLDocument().getSedMLModel();
        Task t1 = new Task("id", null, "sim1", " mode1");
        XPathTarget xpath = new XPathTarget(new SBMLSupport().getXPathForGlobalParameter("k1"));
        DataGenerator dg = sed.addSimpleSpeciesAsOutput(xpath, "k1", "A param name", t1, true);
        assertNotNull(dg);
        assertEquals("k1", dg.getListOfVariables().get(0).getId());
        assertEquals("(k1)", new FormulaFormatter().formulaToString(dg.getMath()));
        assertEquals(1, dg.getListOfVariables().size());

        // now trying to aadd duplicate
        DataGenerator dg2 = sed.addSimpleSpeciesAsOutput(xpath, "k1", "A param name", t1, true);
        assertEquals("k1__1", dg2.getListOfVariables().get(0).getId());
        assertEquals(2, sed.getDataGenerators().size());

        // now try adding duplicate with force == false -> not added
        DataGenerator dg3 = sed.addSimpleSpeciesAsOutput(xpath, "k1", "A param name", t1, false);
        assertNull(dg3);
        assertEquals(2, sed.getDataGenerators().size());
    }

    // Failing due to proxy
//	@Test
//	@Ignore // needs assertions and abstraction of folder location
//	  // =============== quick functionality test
//    public  void testReadModels() throws JDOMException, IOException, XMLException {
//
//        String prefix = "C:/WinPython-32bit-2.7.5.3/python-2.7.5/Lib/site-packages/roadrunner/testing/SedMLModels/";
//      String file = "lorenz";
//    //  String file = "00001-sbml-l3v1-sedml";
//    //  String file = "BIOMD0000000003";
//    //    String file = "BIOMD0000000012";          // multiple tasks
//    //  String file = "BM12";                   // multiple tasks
//    //  String file = "BIOMD0000000021";
//    //  String file = "BIOMD0000000139";   // bug
//    //  String file = "BIOMD0000000140";
//    //  String file = "BorisEJB-os";
//    //String file = "BorisEJB-steady";
//    //String file = "BorisEJB-test";
//    //String file = "BR-INa-variants";
//        String postfix = ".xml";
//        String fileName = prefix + file + postfix;
//      //String fileName = "c:\\TEMP\\repeatedTaskDemo.xml";
//        SedML sedmlModel = SEDMLReader.readFile(new File(fileName));
//        Namespace namespace = sedmlModel.getNamespace();
//        List<Model> mmm = sedmlModel.getBaseModels();
//        for(Model mm : mmm) {
//            log.info(mm.toString());
//        }
//        List<Simulation> sss = sedmlModel.getSimulations();
//        for(Simulation ss : sss) {
//            log.info("Simulation :{}", ss.toString());
//        }
//        List<AbstractTask> ttt = sedmlModel.getTasks();
//        for(AbstractTask tt : ttt) {
//            log.info("Task :{}", tt.toString());
//        }
//        List<DataGenerator> ddd = sedmlModel.getDataGenerators();
//        for(DataGenerator dd : ddd) {
//            log.info("DataGenerator :{}", dd.toString());
//        }
//        List<Output> ooo = sedmlModel.getOutputs();
//        for(Output oo : ooo) {
//            log.info("output :{}", oo.toString());
//
//        }
//       log.info("");
//       log.info(" ------------------------------------------------------------------");
//       log.info("");
//
//        SEDMLDocument sedmlDocument = new SEDMLDocument(sedmlModel);
//        List <SedMLError> eee = sedmlDocument.validate();
//        for(SedMLError ee : eee) {
//            log.info("validation errors: {}", ee.getMessage());
//        }
//
//        sedmlDocument.writeDocument(File.createTempFile("repeatedTaskDemo", ".xml"));
//    }
}
