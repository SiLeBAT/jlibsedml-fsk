package org.jlibsedml;


import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.jlibsedml.modelsupport.KisaoOntology;
import org.jlibsedml.modelsupport.KisaoTerm;
import org.jlibsedml.modelsupport.SBMLSupport;
import org.jlibsedml.modelsupport.SUPPORTED_LANGUAGE;
import org.jmathml.ASTNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WriteLotkaVolterraTest {
     private SBMLSupport support = new SBMLSupport();
     private final String BASE_MODEL_ID="vdp";
     
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void writeSedMLFile () throws XMLException, IOException{
	 SEDMLDocument doc = new SEDMLDocument();
	 SedML sedml = doc.getSedMLModel();
	 
	 Simulation sim = new UniformTimeCourse("id", "name", 0, 0, 10, 100,new Algorithm("KISAO:0000032"));
	 sedml.addSimulation(sim);
	 
	 Model model = new Model("modelid", "Predator prey", SUPPORTED_LANGUAGE.SBML_GENERIC.getURN(), "Predator_Prey.xml");
	 sedml.addModel(model);
	 
	 Task task1 = new Task("task1Id", "Run basic model", model.getId(), sim.getId());
	 sedml.addTask(task1);
	 
	 ASTNode xroot = Libsedml.parseFormulaString("x");
	 DataGenerator dgx = new DataGenerator("dg1", "dg1name",  xroot);
	 SBMLSupport support = new SBMLSupport();
	 Variable var = new Variable("x", "x",task1.getId(), support.getXPathForSpecies("x"));
	 
	 ASTNode yroot = Libsedml.parseFormulaString("y");
	 DataGenerator dgy = new DataGenerator("dgy", "dgyname",  yroot);
	 Variable vary = new Variable("y", "y",task1.getId(), support.getXPathForSpecies("y"));
	 
	 ASTNode time = Libsedml.parseFormulaString("time");
	 DataGenerator dgtime = new DataGenerator("dgtime", "dgtime",  time);
	 Variable vartime = new Variable("time", "time",task1.getId(), VariableSymbol.TIME);

	 // now add this variable to the data generator:
	 dgx.addVariable(var);
	 dgy.addVariable(vary);
	 dgtime.addVariable(vartime);

	 // and add the data generator to the document:
	 sedml.addDataGenerator(dgx);
	 sedml.addDataGenerator(dgy);
	 sedml.addDataGenerator(dgtime);
	 
     Plot2D plot1 = new Plot2D("basicPlot", "basic Plot");
	 Curve cv1 = new Curve("curve1ID","",false,false,dgtime.getId(),dgx.getId());
	 Curve cv2 = new Curve("curve2ID","",false,false,dgtime.getId(),dgy.getId());
	 plot1.addCurve(cv1);
	 plot1.addCurve(cv2);
	 sedml.addOutput(plot1);
	 
	 Plot2D plot2 = new Plot2D("limitCycle", "limitCycle");
	 Curve cv3 = new Curve("curve3ID","",false,false,dgx.getId(),dgy.getId());
	 plot2.addCurve(cv3);
	 sedml.addOutput(plot2);
	 
	 assertEquals(0,doc.validate().size());
	 
	 // if we have no errors, cross-references are OK.
	 
	 List<AbstractTask> tasks = sedml.getTasks();
	 AbstractTask taskRead = tasks.get(0);
	 Simulation sim1 = sedml.getSimulation(taskRead.getSimulationReference());
	 Model mRead = sedml.getModelWithId(taskRead.getModelReference());
	 
	 // check algorithm - can we read it ?
	 String kisao=sim1.getAlgorithm().getKisaoID();
	 KisaoTerm kt = KisaoOntology.getInstance().getTermById(kisao);
	 if(!kt.is_a(KisaoOntology.ALGORITHM_WITH_DETERMINISTIC_RULES)) {
	   // we can only handle ODE based systems; handle this here    
	 }
	 
	 // check model - can we handle it??
	 String lang = mRead.getLanguage();
	 if(!lang.contains("sbml")){
		 // we can read sbml only, so fail here if need be
	 }
	 
	
	 
	 
	 
	}


	private String getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@After
	public void tearDown() throws Exception {
	}

}
