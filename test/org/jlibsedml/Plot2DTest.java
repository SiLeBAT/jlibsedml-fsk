package org.jlibsedml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jmathml.ASTRootNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Plot2DTest {
	
	Plot2D plot2d;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testPlot2D() {
		assertNotNull(createplot2d("any"));
	}
	
	

	@Test
	public final void testGetCurvesUsingDataGeneratorAsXAxis() {
		String DGIDX="xDG1";
		String DGIDY="yDG2";
		plot2d=createplot2d("any");
		Curve c1 = new Curve("a1", "", false, false, DGIDX, "yDG1");
		Curve c2 = new Curve("a2", "", false, false, "xDG2", DGIDY);
		plot2d.addCurve(c1);
		plot2d.addCurve(c2);
		DataGenerator dg = new DataGenerator(DGIDX, "", new ASTRootNode());
		DataGenerator dgy = new DataGenerator(DGIDY, "", new ASTRootNode());
		
		assertEquals(1, plot2d.getCurvesUsingDataGeneratorAsXAxis(dg).size());
		assertEquals(DGIDX, plot2d.getCurvesUsingDataGeneratorAsXAxis(dg).get(0).getXDataReference());
		
		assertEquals(1, plot2d.getCurvesUsingDataGeneratorAsYAxis(dgy).size());
		assertEquals(DGIDY, plot2d.getCurvesUsingDataGeneratorAsYAxis(dgy).get(0).getYDataReference());
		assertEquals(2, plot2d.getAllIndependentDataGeneratorReferences().size());
		assertEquals(DGIDX, plot2d.getAllIndependentDataGeneratorReferences().get(0));
	}

	@Test
	public void testGetAddRemoveCurves(){
		plot2d=createplot2d("any");
		assertNotNull(plot2d.getListOfCurves());
		
		Curve c1 = TestUtils.createAnyCurve("ID");
		plot2d.addCurve(c1);
		assertEquals(1,plot2d.getListOfCurves().size());
		plot2d.removeCurve(c1);
		assertEquals(0,plot2d.getListOfCurves().size());
	}
	

	@Test
	public final void testIsPlot2d() {
		plot2d=createplot2d("any");
		assertTrue(plot2d.isPlot2d());
		assertFalse(plot2d.isPlot3d());
		assertFalse(plot2d.isReport());
	}

	Plot2D createplot2d (String id){
		Plot2D plot2d=new Plot2D(id, "");
		return plot2d;
	}
	
	
	
	
}
