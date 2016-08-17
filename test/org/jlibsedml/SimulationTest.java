package org.jlibsedml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SimulationTest {
    Simulation simulation1, same, simulation2;
    final String EXPECED_ID="id";
    final String OTHER_ID="id2";
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testHashCodeBasedOnID() {
		simulation1=TestUtils.createAnySimulation(EXPECED_ID);
		same=TestUtils.createAnySimulation(EXPECED_ID);
		simulation2=TestUtils.createAnySimulation(OTHER_ID);
		
		assertFalse(simulation1.hashCode() == simulation2.hashCode());
		assertTrue(simulation1.hashCode() == same.hashCode());
	}

	@Test
	public final void testSimulationAllowsNullName() {
		new Simulation("id", null,TestUtils.createDefaultAlgorithm()) {
			public String getSimulationKind() {return"";}
			@Override
			public String getElementName() {	return "";}
		};
	}
	
	@Test
	(expected=IllegalArgumentException.class)
	public final void testSimulationDoesNotAllowNullID() {
		new Simulation(null, "",TestUtils.createDefaultAlgorithm()) {
			public String getSimulationKind() {return"";}
			@Override
			public String getElementName() {	return "";}
		};
	}

	@Test
	public final void testEqualsBasedOnID() {
		simulation1=TestUtils.createAnySimulation(EXPECED_ID);
		same=TestUtils.createAnySimulation(EXPECED_ID);
		simulation2=TestUtils.createAnySimulation(OTHER_ID);
		
		assertFalse(simulation1.equals(simulation2));
		assertTrue(simulation1.equals(same));
	}

}
