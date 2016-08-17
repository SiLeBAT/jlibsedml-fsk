package org.jlibsedml.extensions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jlibsedml.Plot2D;
import org.jlibsedml.SedML;
import org.jlibsedml.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ElementSearchVisitorTest {

    ElementSearchVisitor vis;
    SedML toTest;
    @Before
    public void setUp() throws Exception {
        toTest= TestUtils.readSedml12();
       
    }

    @After
    public void tearDown() throws Exception {
        vis.clear();
    }

    @Test
    public final void testVisitSedMLSimulation() {
        vis=new ElementSearchVisitor("simulation1");
        toTest.accept(vis);
        assertEquals(toTest.getSimulation("simulation1"), vis.getFoundElement());
    }
    
    @Test
    public final void testVisitSedMModel() {
        vis=new ElementSearchVisitor("model2");
        toTest.accept(vis);
        assertEquals(toTest.getModelWithId("model2"), vis.getFoundElement());
    }
    
    @Test
    public final void testVisitSedMLTask() {
        vis=new ElementSearchVisitor("task2");
        toTest.accept(vis);
        assertEquals(toTest.getTaskWithId("task2"), vis.getFoundElement());
    }
    
    @Test
    public final void testVisitSedMLDG() {
        vis=new ElementSearchVisitor("TetR");
        toTest.accept(vis);
        assertEquals(toTest.getDataGeneratorWithId("TetR"), vis.getFoundElement());
    }
    
    @Test
    public final void testVisitSedMLVar() {
        vis=new ElementSearchVisitor("v2");
        toTest.accept(vis);
        assertEquals(toTest.getDataGeneratorWithId("TetR").getListOfVariables().get(0), vis.getFoundElement());
    }
    
    @Test
    public final void testVisitPlot2d() {
        vis=new ElementSearchVisitor("plot2_damped_oscillations");
        toTest.accept(vis);
        assertEquals(toTest.getOutputWithId("plot2_damped_oscillations"), vis.getFoundElement());
    }
    
    @Test
    public final void testVisitCv() {
        vis=new ElementSearchVisitor("c7");
        toTest.accept(vis);
        assertEquals(((Plot2D)toTest.getOutputWithId("plot3_normalized_protein_levels")).getListOfCurves().get(0), vis.getFoundElement());
    }

    @Test
    public final void testGetFoundElement() {
        vis=new ElementSearchVisitor("TetR");
        assertNull(vis.getFoundElement()); // null before use
        toTest.accept(vis);
        assertNotNull(vis.getFoundElement());
        vis.clear(); //resets
        assertNull(vis.getFoundElement()); // null after being cleared.
    }

    

}
