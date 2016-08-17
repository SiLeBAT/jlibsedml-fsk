package org.jlibsedml.validation;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.jdom.Document;
import org.jlibsedml.Algorithm;
import org.jlibsedml.IIdentifiable;
import org.jlibsedml.Simulation;
import org.jlibsedml.TestUtils;
import org.jlibsedml.modelsupport.KisaoOntology;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class KisaoIDValidatorTest {
    KisaoIDValidator validator;
    class KisaoIDValidatorTSS extends KisaoIDValidator{
    	KisaoIDValidatorTSS(List<Simulation> simulations, Document doc) {
			super(simulations, doc);}

		int  getLineNumberOfError(String elementKind,IIdentifiable identifiable) {
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
	public final void testValidate() {
		Simulation sim = TestUtils.createAnySimulation("sim");
		sim.setAlgorithm(new Algorithm(KisaoOntology.ALGORITHM_WITH_DETERMINISTIC_RULES.getId()));
		validator=new KisaoIDValidatorTSS(Arrays.asList(new Simulation[]{sim}),null);
		assertEquals(0, validator.validate().size());
		
		sim.setAlgorithm(new Algorithm("Non-standard ID"));
		validator=new KisaoIDValidatorTSS(Arrays.asList(new Simulation[]{sim}),null);
		assertEquals(1, validator.validate().size());
	}

}
