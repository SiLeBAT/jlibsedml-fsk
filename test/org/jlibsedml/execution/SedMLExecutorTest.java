package org.jlibsedml.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jlibsedml.AbstractTask;
import org.jlibsedml.Algorithm;
import org.jlibsedml.ChangeAttribute;
import org.jlibsedml.Libsedml;
import org.jlibsedml.Model;
import org.jlibsedml.Output;
import org.jlibsedml.SEDMLDocument;
import org.jlibsedml.SedML;
import org.jlibsedml.Simulation;
import org.jlibsedml.Task;
import org.jlibsedml.TestUtils;
import org.jlibsedml.UniformTimeCourse;
import org.jlibsedml.XMLException;
import org.jlibsedml.XPathTarget;
import org.jlibsedml.mathsymbols.SedMLSymbolFactory;
import org.jlibsedml.modelsupport.SUPPORTED_LANGUAGE;
import org.jmathml.ASTCi;
import org.jmathml.ASTNode;
import org.jmathml.ASTNumber;
import org.jmathml.EvaluationContext;
import org.jmathml.SymbolRegistry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SedMLExecutorTest {

     static final String ANY_Runge_Kutta = "KISAO:0000064";

    String KISAO_TERM_CVODE = "KISAO:0000019";

    String NON_ODE = "KISAO:0000051";

    String GENERAL_ODE = "KISAO:0000433";

    final Simulation sim1 = new UniformTimeCourse("sim1", "name", 0, 0, 0, 0,
            TestUtils.createDefaultAlgorithm());
    final Simulation sim2 = new UniformTimeCourse("sim2", "name", 0, 0, 0, 0,
            TestUtils.createDefaultAlgorithm());
    final Simulation sim3 = new UniformTimeCourse("sim3", "name", 0, 0, 0, 0,
            TestUtils.createDefaultAlgorithm());

    final AbstractTask task1 = new Task("Tid1", "name", "any", "sim1");
    final AbstractTask task2 = new Task("Tid1", "name", "any", "sim2");
    final AbstractTask task3 = new Task("Tid1", "name", "any", "sim3");

    NoDependencyTestExecutor exe;
    Model dummybase = new Model("id1", "name",
            SUPPORTED_LANGUAGE.SBML_GENERIC.getURN(), "anyfile.xml");
    Model dummy1 = new Model("id2", "name",
            SUPPORTED_LANGUAGE.SBML_GENERIC.getURN(), "id1");
    Model dummy2 = new Model("id3", "name",
            SUPPORTED_LANGUAGE.SBML_GENERIC.getURN(), "id2");
    ModelResolver resolver;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorDoesNotTakeNull() {
        exe = new NoDependencyTestExecutor(null, TestUtils.createAnyOutput());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorDoesNotTakeNull2() {
        SedML model = new SEDMLDocument().getSedMLModel();
        Output o = null;
        exe = new NoDependencyTestExecutor(model, o);
    }

    @Test
    public final void testResolveModelsForTasks() {
        SedML model = new SEDMLDocument().getSedMLModel();
        model.setModels(Arrays
                .asList(new Model[] { dummy2, dummy1, dummybase }));

        resolver = new ModelResolver(model);
        List<String> rc = new ArrayList<String>();
        resolver.getModelModificationTree(dummy2, rc);
        assertEquals(3, rc.size());
        assertEquals(dummybase.getId(), rc.get(0));
        assertEquals(dummy2.getId(), rc.get(2));
    }

    @Test
    public void applyChanges() {
        SedML model = new SEDMLDocument().getSedMLModel();
        model.setModels(Arrays
                .asList(new Model[] { dummy2, dummy1, dummybase }));
        // sequential changes applied in a cascade tnhrough the models
        dummybase.addChange(new ChangeAttribute(new XPathTarget(
                "/a/element[@id='a']/@val"), "1"));
        dummy1.addChange(new ChangeAttribute(new XPathTarget(
                "/a/element[@id='b']/@val"), "2"));
        dummy2.addChange(new ChangeAttribute(new XPathTarget(
                "/a/element[@id='c']/@val"), "3"));
        final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<a><element id=\"a\" val=\"x\"/><element id=\"b\" val=\"y\"/><element id=\"c\" val=\"z\"/></a>";

        resolver = new ModelResolver(model);

        List<String> rc = new ArrayList<String>();
        resolver.getModelModificationTree(dummy2, rc);
        // check that all changes have been applied.
        String newMdl = resolver.applyModelChanges(rc, xml);
        assertTrue(newMdl.contains("element id=\"a\" val=\"1\""));
        assertTrue(newMdl.contains("element id=\"b\" val=\"2\""));
        assertTrue(newMdl.contains("element id=\"c\" val=\"3\""));
    }

    @Test
    public void applyChanges2() throws XMLException {
        SEDMLDocument doc = Libsedml.readDocument(new File(
                "TestData/goldsed.xml"));
        SedML sedml = doc.getSedMLModel();
        ModelResolver rs = new ModelResolver(doc.getSedMLModel());
        rs.add(new FileModelResolver());
        for (Model m : sedml.getModels()) {
            String s = rs.getModelString(m);
         //   System.err.println(s);
        }
    }

    @Test
    public void applyChangesToSingleModelWithChanges() {
        SedML model = new SEDMLDocument().getSedMLModel();
        model.setModels(Arrays.asList(new Model[] { dummybase }));
        dummybase.addChange(new ChangeAttribute(new XPathTarget(
                "/a/element[@id='a']/@val"), "1"));

        final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<a><element id=\"a\" val=\"x\"/><element id=\"b\" val=\"y\"/><element id=\"c\" val=\"z\"/></a>";

        resolver = new ModelResolver(model);
        List<String> rc = new ArrayList<String>();
        resolver.getModelModificationTree(dummybase, rc);
        String newMdl = resolver.applyModelChanges(rc, xml);
        assertTrue(newMdl.contains("element id=\"a\" val=\"1\""));
        assertTrue(newMdl.contains("element id=\"b\" val=\"y\""));
        assertTrue(newMdl.contains("element id=\"c\" val=\"z\""));
    }

    @Test
    public final void testCVODECannotExecuteSimulationOfStochasticSedml() {
        SedML model = new SEDMLDocument().getSedMLModel();
        exe = new NoDependencyTestExecutor(model, TestUtils.createAnyOutput());
        sim1.setAlgorithm(new Algorithm(NON_ODE));
        assertFalse(exe.canExecuteSimulation(sim1));
    }

    @Test
    public final void testHandlesInvalidOrNullKISAOID() {
        SedML model = new SEDMLDocument().getSedMLModel();
        exe = new NoDependencyTestExecutor(model, TestUtils.createAnyOutput());
        assertFalse(exe.canExecuteSimulation(sim1));
        sim1.setAlgorithm(new Algorithm("RUBBISH"));
        assertFalse(exe.canExecuteSimulation(sim1));
    }

    @Test
    public final void testHandlesSingleTask() {
        SedML model = new SEDMLDocument().getSedMLModel();
        sim1.setAlgorithm(new Algorithm(ANY_Runge_Kutta));
        exe = new NoDependencyTestExecutor(model, task1);
        assertTrue(exe.canExecuteSimulation(sim1));
    }

    @Test
    public final void testGetSimulatableTasks2() {
        SedML model = createModel();
        sim1.setAlgorithm(new Algorithm(ANY_Runge_Kutta));
        assertEquals(1, exe.getSimulatableTasks().size());
    }

    private SedML createModel() {
        SedML model = new SEDMLDocument().getSedMLModel();
        model.setTasks(Arrays
                .asList(new AbstractTask[] { task1, task2, task3 }));
        model.setSimulations(Arrays
                .asList(new Simulation[] { sim1, sim2, sim3 }));
        exe = new NoDependencyTestExecutor(model, TestUtils.createAnyOutput());
        return model;
    }

    @Test
    public void testEvaluationOfVectorFields() {
        SymbolRegistry.getInstance().addSymbolFactory(new SedMLSymbolFactory());
        Double[] aData = new Double[] { 4.0, 8.0, 12.0 };
        Double[] EXPECTED = new Double[] { 0d, 0.5, 1d };
        ASTNode toEval = Libsedml
                .parseFormulaString("(a - min(a)) / (max (a) - min(a))");
        assertEquals(4, toEval.getIdentifiers().size());
        for (ASTCi var : toEval.getIdentifiers()) {
            if (var.isVector()) {
                Map<String, Iterable<Double>> ec = new HashMap<String, Iterable<Double>>();
                ec.put(var.getName(), Arrays.asList(aData));
                ASTNumber num = var.getParentNode().evaluate(
                        new EvaluationContext(ec));
                var.getParentNode().getParentNode()
                        .replaceChild(var.getParentNode(), num);
            }
        }
        assertEquals(1, toEval.getIdentifiers().size());

        int i = 0;
        for (Double d : aData) {
            for (ASTCi var : toEval.getIdentifiers()) {
                EvaluationContext ec = new EvaluationContext();
                ec.setValueFor(var.getName(), d);
                assertEquals("Failed for " + i, EXPECTED[i].doubleValue(),
                        toEval.evaluate(ec).getValue(), 0.01);
            }
            i++;
        }
    }

    @Test
    public void testLongPrefix() throws XMLException {
        SedML sed = Libsedml.readDocument(
                new File("TestData/longPrefixdXpath.xml")).getSedMLModel();
        exe = new NoDependencyTestExecutor(sed, sed.getOutputs().get(0));
        try {
            exe.runSimulations();
        } catch (Exception e) {
            fail("Should have run without exception...");
        }
    }
}
