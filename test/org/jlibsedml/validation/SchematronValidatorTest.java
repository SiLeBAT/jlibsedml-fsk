package org.jlibsedml.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.jdom.Document;
import org.jlibsedml.AbstractTask;
import org.jlibsedml.DataGenerator;
import org.jlibsedml.Libsedml;
import org.jlibsedml.Model;
import org.jlibsedml.Parameter;
import org.jlibsedml.SEDMLDocument;
import org.jlibsedml.SedML;
import org.jlibsedml.SedMLError;
import org.jlibsedml.Simulation;
import org.jlibsedml.Task;
import org.jlibsedml.TestUtils;
import org.jlibsedml.UniformTimeCourse;
import org.jlibsedml.Variable;
import org.jlibsedml.XMLException;
import org.jlibsedml.mathsymbols.SedMLSymbolFactory;
import org.jmathml.ASTNode;
import org.jmathml.SymbolRegistry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

public class SchematronValidatorTest {
    ISedMLValidator validator = null;
    Logger log = LoggerFactory.getLogger(SchematronValidatorTest.class);

    class SchematronValidatorTSS extends SchematronValidator {

        SchematronValidatorTSS(Document doc, SedML sedml) {
            super(doc, sedml);
            // TODO Auto-generated constructor stub
        }

        int getLineNumber(Node sedmlNode) {
            return 0;
        }
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void testValidateMissingModelRef() throws XMLException {
        SedML sedml = createSedmlWithMissingModel();
        validator = new SchematronValidatorTSS(null, sedml);
        List<SedMLError> errs = validator.validate();
        assertEquals(1, errs.size());
    }

    @Test
    public final void testValidateMissingSimulationRef() throws XMLException {
        SedML sedml = createSedmlWithMissingSimulation();
        validator = new SchematronValidatorTSS(null, sedml);
        List<SedMLError> errs = validator.validate();
        assertEquals(1, errs.size());
        assertTrue(findMesgInErrors(errs, "Simulation reference"));
    }

    private SedML createSedmlWithMissingSimulation() throws XMLException {
        SedML sedml = TestUtils.createEmptySedml();
        AbstractTask task = new Task("id", "name", "modelid", "simRef");
        sedml.setTasks(Arrays.asList(new AbstractTask[] { task }));
        Model model = createModel();
        sedml.setModels(Arrays.asList(new Model[] { model }));
        return sedml;
        // task but no simulation.
    }

    private Model createModel() {
        return new Model("modelid", "modelname", "sbml", "urn:src");
    }

    @Test
    public void testValidateMathsWithIdenitifierinVarAndParamsOK()
            throws XMLException {
        ASTNode OK_MATH = Libsedml.parseFormulaString("v1 * v2 + p1");
        SEDMLDocument doc = setUpWithMath(OK_MATH);
        validator = new SchematronValidatorTSS(null, doc.getSedMLModel());
        assertEquals(2, validator.validate().size()); // due to undefined task
                                                      // ref
    }

    private SEDMLDocument setUpWithMath(final ASTNode math) {
        Variable v1 = TestUtils.createAnyVariableWithID("v1", "");
        Variable v2 = TestUtils.createAnyVariableWithID("v2", "");
        Parameter p1 = TestUtils.createAnyParameterWithID("p1", "");

        DataGenerator toTest = new DataGenerator("any", "any", math);
        toTest.addParameter(p1);
        toTest.addVariable(v1);
        toTest.addVariable(v2);
        SEDMLDocument doc = new SEDMLDocument();
        SedML model = doc.getSedMLModel();
        model.addDataGenerator(toTest);

        return doc;
    }

    @Test
    public void testValidateMathsWithMissinVarNotOK() throws XMLException {
        ASTNode UNKNOWN_VAR_MATH = Libsedml
                .parseFormulaString("v1 * v2 + XXXXX");
        SEDMLDocument doc = setUpWithMath(UNKNOWN_VAR_MATH);
        validator = new SchematronValidatorTSS(null, doc.getSedMLModel());
        List<SedMLError> errs = validator.validate();
        String expected = "referred to unknown task";
        assertTrue(findMesgInErrors(errs, expected));
    }

    boolean findMesgInErrors(List<SedMLError> errs, String expected) {
        boolean found = false;
        for (SedMLError err : errs) {
            if (err.getMessage().contains(expected)) {
                found = true;
            }
        }
        return found;
    }

    @Test
    public void testValidateMathsWithUnknownFunctionNotOK() throws XMLException {
        ASTNode UNKNOWN_VAR_MATH = Libsedml
                .parseFormulaString("v1 * v2 + myfunc(p1)");
        SEDMLDocument doc = setUpWithMath(UNKNOWN_VAR_MATH);
        validator = new SchematronValidatorTSS(null, doc.getSedMLModel());
        List<SedMLError> errs = validator.validate();
        assertEquals(2, errs.size());

        log.info("{} at {}", errs.get(0).getMessage(), errs.get(0).getLineNo());
    }

    @Test
    public void testValidateMathsWithEXtraDeclaredVarIsOK() throws XMLException {
        ASTNode OK_MATH = Libsedml.parseFormulaString("v1 * v2 + p1");
        SEDMLDocument doc = setUpWithMath(OK_MATH);
        DataGenerator dg = doc.getSedMLModel().getDataGenerators().get(0);
        validator = new SchematronValidatorTSS(null, doc.getSedMLModel());
        dg.addVariable(TestUtils.createAnyVariableWithID("OTHER", ""));
        assertEquals(3, validator.validate().size()); // 3 vars with undeclared
                                                      // tasks
    }

    @Test
    public void testValidateMathsWithSymbols() throws XMLException {
        SymbolRegistry.getInstance().addSymbolFactory(new SedMLSymbolFactory());
        ASTNode OK_MATH = Libsedml.parseFormulaString("sum(v1) ");
        SEDMLDocument doc = setUpWithMath(OK_MATH);
        validator = new SchematronValidatorTSS(null, doc.getSedMLModel());
        assertEquals(2, validator.validate().size()); // no vars
    }

    private SedML createSedmlWithMissingModel() {
        SedML sedml = TestUtils.createEmptySedml();
        AbstractTask task = new Task("id", "name", "modelid", "simRef");
        sedml.setTasks(Arrays.asList(new AbstractTask[] { task }));
        Simulation simRef = new UniformTimeCourse("simRef", "simName", 0, 0, 0,
                0, TestUtils.createDefaultAlgorithm());
        sedml.setSimulations(Arrays.asList(new Simulation[] { simRef }));
        return sedml;
    }

    @Test
    public final void testValidateStartGtOutputStartIsError()
            throws XMLException {
        UniformTimeCourse sim = new UniformTimeCourse("id", "name", 10, 0, 20,
                200, TestUtils.createDefaultAlgorithm());
        SEDMLDocument doc = Libsedml.createDocument();
        doc.getSedMLModel().addSimulation(sim);
        validator = new SchematronValidatorTSS(null, doc.getSedMLModel());
        assertEquals(1, validator.validate().size());
    }

    @Test
    public final void testValidateOutputEndltOutputStartIsError()
            throws XMLException {
        UniformTimeCourse sim = new UniformTimeCourse("id", "name", 0, 10, 5,
                200, TestUtils.createDefaultAlgorithm());
        SEDMLDocument doc = Libsedml.createDocument();
        doc.getSedMLModel().addSimulation(sim);
        validator = new SchematronValidatorTSS(new Document(),
                doc.getSedMLModel());
        assertEquals(1, validator.validate().size());
    }

    @Test
    public final void testValidateNumStepsltZeroIsError() throws XMLException {
        UniformTimeCourse sim = new UniformTimeCourse("id", "name", 0, 10, 20,
                -200, TestUtils.createDefaultAlgorithm());
        SEDMLDocument doc = Libsedml.createDocument();
        doc.getSedMLModel().addSimulation(sim);
        validator = new SchematronValidatorTSS(new Document(),
                doc.getSedMLModel());
        assertEquals(1, validator.validate().size());
    }
}
