package org.jlibsedml.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;

import org.jlibsedml.AbstractTask;
import org.jlibsedml.DataGenerator;
import org.jlibsedml.DataSet;
import org.jlibsedml.Output;
import org.jlibsedml.Report;
import org.jlibsedml.SedML;
import org.jlibsedml.TestUtils;
import org.jmathml.ASTCi;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SedMLResultsProcessorTest {
    SedMLResultsProcesser2 pcsr;
    SedML sedml;
    Output out;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSedMLResultsProcesser2() {
        pcsr = new SedMLResultsProcesser2(null, TestUtils.createAnyOutput());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSedMLResultsProcesser2b() {
        pcsr = new SedMLResultsProcesser2(TestUtils.createEmptySedml(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSedMLResultsProcesserThrowsIAEIfOuputNotInSedml() {
        pcsr = new SedMLResultsProcesser2(TestUtils.createEmptySedml(),
                TestUtils.createAnyOutput());
    }

    @Test
    public final void testProcess() {
        createBasicOutPut();
        assertNoErrors();
    }

    @Test
    public final void testProcessFailsIfDGCAnnotBeResolvedBecauseOuputIsEmpty() {
        createBasicOutPut();
        pcsr.process(Collections.EMPTY_MAP);
        assertFalse(pcsr.isProcessed());
        assertTrue(get1stError().contains(
                SedMLResultsProcesser2.NO_DG_INOUTPUT_MSG));

    }

    @Test(expected = IllegalArgumentException.class)
    public final void testProcessFailsPArameterIsNull() {
        createBasicOutPut();
        pcsr.process(null);
        assertFalse(pcsr.isProcessed());
        assertTrue(get1stError().contains(
                SedMLResultsProcesser2.NO_DG_INOUTPUT_MSG));

    }

    @Test
    public final void testProcessFailsIfDGCAnnotBeResolved() {
        createBasicOutPut();
        ((Report) out).addDataSet(TestUtils.createAnyDataSet("DS"));
        pcsr.process(Collections.EMPTY_MAP);
        assertFalse(pcsr.isProcessed());
        assertTrue(get1stError().contains(
                SedMLResultsProcesser2.MISSING_DG_MESSAGE));

    }

    @Test
    public final void testProcessFailsIfDGHasUnknownVariable() {
        createBasicOutPut();
        DataSet ds = TestUtils.createAnyDataSet("DS");
        DataGenerator dg = new DataGenerator(ds.getDataReference(), "DG",
                new ASTCi("UNKNOWN"));
        sedml.addDataGenerator(dg);
        ((Report) out).addDataSet(ds);
        pcsr.process(Collections.EMPTY_MAP);
        assertFalse(pcsr.isProcessed());
        assertTrue(get1stError().contains(
                SedMLResultsProcesser2.COULD_NOT_RESOLVE_MATHML_MSG));

    }

    private String get1stError() {
        return pcsr.getProcessingReport().getMessages().get(0).getMessage();
    }

    private void createBasicOutPut() {
        sedml = TestUtils.createEmptySedml();
        out = TestUtils.createAnyOutput();
        sedml.addOutput(out);
        pcsr = new SedMLResultsProcesser2(sedml, out);
    }

    @Test
    public final void testGetProcessedResultIsNullBeforePRocessing() {
        createBasicOutPut();
        assertNull(pcsr.getProcessedResult());
    }

    @Test
    public final void testGetProcessingReportIsClearedAfterEachOnvocation() {
        createBasicOutPut();
        assertNoErrors();
    }

    @Test
    public final void testIsProcessed() {
        createBasicOutPut();
        assertFalse(pcsr.isProcessed());
    }

    @Test
    public final void testValidProcessing() {
        sedml = TestUtils.createValidOutputConfig();
        pcsr = new SedMLResultsProcesser2(sedml, sedml.getOutputs().get(0));
        assertFalse(pcsr.isProcessed());
        HashMap<AbstractTask, IRawSedmlSimulationResults> map = new HashMap<AbstractTask, IRawSedmlSimulationResults>();
        map.put(sedml.getTasks().get(0),
                TestUtils.createResultsForValidOuputConfig());
        pcsr.process(map);
        assertTrue(pcsr.isProcessed());
        assertNoErrors();
        assertNotNull(pcsr.getProcessedResult());
    }

    private void assertNoErrors() {
        assertEquals(0, pcsr.getProcessingReport().getMessages().size());
    }

}
