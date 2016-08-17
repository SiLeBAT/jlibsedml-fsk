package org.jlibsedml;


import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.jlibsedml.modelsupport.SBMLSupport;
import org.jlibsedml.modelsupport.SBMLSupport.ParameterAttribute;
import org.jmathml.ASTCi;
import org.jmathml.ASTNode;
import org.jmathml.ASTRootNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WriteSedmlDocumentProgrammaticallyTest {
     private SBMLSupport support = new SBMLSupport();
     private final String BASE_MODEL_ID="vdp";
     Logger log = LoggerFactory.getLogger(WriteSedmlDocumentProgrammaticallyTest.class);
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void writeSedMLFile () throws XMLException, IOException{
	 SEDMLDocument doc = new SEDMLDocument();
	 SedML model = doc.getSedMLModel();
	
	 
	 Model basic = new Model(BASE_MODEL_ID, "VanderPol oscillator",
			       "sbml","vdp.xml");
	 
	 model.addModel(basic);
	 
	 // create model with parameter changes for damped oscillations
	 Model damped = createDampedOscillationModel(basic);
	 model.addModel(damped);
	 
	 // create model with changes for Hopf oscillation
	 Model hopf= createHopfOscillation(basic);
	 model.addModel(hopf);
	 
	 Simulation basicSim = new UniformTimeCourse("sim1", "basicSimulation",0,0,25,250,new Algorithm("KISAO:0000019"));
	
	 model.addSimulation(basicSim);
	 
	 Task task1 = new Task("runBasic", "Run basic model", basic.getId(), basicSim.getId());
	 Task task2 = new Task("runDamped", "Run damped oscillations model", damped.getId(), basicSim.getId());
	 Task task3 = new Task("runhopf", "Run Hopf oscillation model", hopf.getId(), basicSim.getId());
	 model.setTasks(Arrays.asList(new AbstractTask[]{task1, task2, task3}));
	 
	 DataGenerator time = createDGForTime("runBasic");
	 DataGenerator dgx1Basic = createDGFor("x1Basic","x1", "runBasic");
	 DataGenerator dgx2Basic = createDGFor("dgx2Basic","x2", "runBasic");
	 DataGenerator dgx1Damped = createDGFor("dgx1Damped","x1", "runDamped");
	 DataGenerator dgx2Damped = createDGFor("dgx2Damped","x2", "runDamped");
	 DataGenerator dgx1Hopf = createDGFor("dgx1Hopf","x1", "runhopf");
	 DataGenerator dgx2Hopf = createDGFor("dgx2Hopf","x2", "runhopf");
	 model.setDataGenerators(Arrays.asList(time, dgx1Basic,dgx2Basic,dgx1Damped,dgx2Damped,dgx1Hopf,dgx2Hopf));
	 
	 Plot2D pl1 = new Plot2D("basicPlot", "basic Plot");
	 Curve cv1 = new Curve("curve1ID","",false,false,time.getId(),dgx1Basic.getId());
	 Curve cv2 = new Curve("curve2ID","",false,false,time.getId(),dgx2Basic.getId());
	 pl1.addCurve(cv1);
	 pl1.addCurve(cv2);
	 model.addOutput(pl1);
	 
	 Plot2D pl2 = new Plot2D("dampedPlot", "Damped oscillations");
	 Curve cv3 = new Curve("curve3ID","",false,false,time.getId(),dgx1Damped.getId());
	 Curve cv4 = new Curve("curve4ID","",false,false,time.getId(),dgx2Damped.getId());
	 pl2.addCurve(cv3);
	 pl2.addCurve(cv4);
	 model.addOutput(pl2);
	 
	 Plot2D pl3 = new Plot2D("hopfPlot", "Hopf oscillations");
	 Curve cv5 = new Curve("curve5ID","",false,false,time.getId(),dgx1Hopf.getId());
	 Curve cv6 = new Curve("curve6ID","",false,false,time.getId(),dgx2Hopf.getId());
	 pl3.addCurve(cv5);
	 pl3.addCurve(cv6);
	 model.addOutput(pl3);
	 
	 
	// printErrors(doc);
	 log.debug(doc.writeDocumentToString());
	 assertEquals(0, doc.validate().size());
	 IModelContent fmc = new FileModelContent(new File("TestData/vdp.xml"));
	 ArchiveComponents ac = new ArchiveComponents(Arrays.asList(new IModelContent[]{fmc}), doc);
	 byte [] archive =Libsedml.writeSEDMLArchive(ac, "mysedml");
	 File out = File.createTempFile("VdpSimulations", ".zip");
	 log.info("Writing zip file to {}", out.getAbsolutePath()); 
	 FileOutputStream fos = new FileOutputStream(out);
	 fos.write(archive);
	 fos.close();
	}

	private DataGenerator createDGForTime(String t1) {
		 ASTNode root = new ASTRootNode();	 
		 
		 root.addChildNode( new ASTCi("time"));
		 DataGenerator dg = new DataGenerator("timeDG", "time",  root);
		 Variable var = new Variable("time","time_var",t1, VariableSymbol.TIME);
		 dg.addVariable(var);
		 return dg;
	}

	private DataGenerator createDGFor(String varId, String modelRef, String taskId) {
		 ASTNode root = Libsedml.parseFormulaString(varId+"var");
		 
		 DataGenerator dg = new DataGenerator(varId, varId,  root);
		 SBMLSupport support = new SBMLSupport();
		 Variable var = new Variable(varId+"var",varId+"var",taskId, support.getXPathForSpecies(modelRef));
		 dg.addVariable(var);
		 return dg;
	}

	private Model createHopfOscillation(Model basic) {
		Model hopf = new Model(basic, "hopf");
		 hopf.setSource(BASE_MODEL_ID);		 
		 String target1=support.getXPathForGlobalParameter("b",ParameterAttribute.value);
		 Change hopfChange1 = new ChangeAttribute(new XPathTarget(target1), "1");
		 hopf.addChange(hopfChange1);

		return hopf;
	}

	private Model createDampedOscillationModel(Model basic) {
		Model damped = new Model(basic, "damped");
		 damped.setSource(BASE_MODEL_ID);
		 String target1=support.getXPathForGlobalParameter("a",ParameterAttribute.value);
		 Change dampedChange1 = new ChangeAttribute(new XPathTarget(target1), "5");
		 
		 String target2=support.getXPathForGlobalParameter("eps",ParameterAttribute.value);
		 Change dampedChange2 = new ChangeAttribute(new XPathTarget(target2), "1");
		 
		 String target3=support.getXPathForGlobalParameter("b",ParameterAttribute.value);
		 Change dampedChange3 = new ChangeAttribute(new XPathTarget(target3), "10");
		 damped.addChange(dampedChange1);
		 damped.addChange(dampedChange2);
		 damped.addChange(dampedChange3);
		return damped;
	}

	
	@After
	public void tearDown() throws Exception {
	}

}
