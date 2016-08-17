package org.jlibsedml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ReportTest {
    Report report;
	@Before
	public void setUp() throws Exception {
		report = TestUtils.createEmptyReport();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetAllDataGeneratorReferences() {
		assertNotNull(report.getAllDataGeneratorReferences());
		assertTrue(report.getAllDataGeneratorReferences().isEmpty());
		DataSet ds1 = TestUtils.createAnyDataSet("id1");
		report.addDataSet(ds1);
		assertEquals(1,report.getAllDataGeneratorReferences().size());
	}

	@Test
	public final void testGetAllIndependentDataGeneratorReferences() {
		assertNotNull(report.getAllIndependentDataGeneratorReferences());
		assertTrue(report.getAllIndependentDataGeneratorReferences().isEmpty());
	}

	@Test
	(expected=IllegalArgumentException.class)
	public final void testReportFailsIfNullID() {
		report = new Report(null, "name");
	}
	@Test
	public final void testReportOKWithNullOrEmptyName() {
		report = new Report("id", "");
		report = new Report("id", null);
	}

	@Test
	public final void testGetListOfDataSets() {
		assertNotNull(report.getListOfDataSets());
		assertTrue(report.getListOfDataSets().isEmpty());
	}

	@Test
	public final void testAddRemoveDataSet() {
		DataSet ds1 = TestUtils.createAnyDataSet("id1");
		report.addDataSet(ds1);
		assertEquals(1,report.getListOfDataSets().size());
		report.addDataSet(ds1); // no dups
		assertEquals(1,report.getListOfDataSets().size());
		report.removeDataSet(ds1);
		assertEquals(0,report.getListOfDataSets().size());
		
	}

	@Test
	public final void testIsPlot2d() {
		assertFalse(report.isPlot2d());
	}

	@Test
	public final void testIsPlot3d() {
		assertFalse(report.isPlot3d());
	}

	@Test
	public final void testIsReport() {
		assertTrue(report.isReport());
	}

}
