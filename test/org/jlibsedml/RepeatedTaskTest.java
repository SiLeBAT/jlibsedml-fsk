package org.jlibsedml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jlibsedml.UniformRange.UniformType;
import org.jmathml.ASTCi;
import org.jmathml.ASTNode;
import org.jmathml.ASTPlus;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RepeatedTaskTest {

    //TODO needs assertions
    @Test
    public  void repeatedTaskTest() throws JDOMException, IOException, XMLException {
        Logger   log = LoggerFactory.getLogger(RepeatedTask.class);
        SEDMLDocument sedmlDocument = new SEDMLDocument();
        SedML sedmlModel = sedmlDocument.getSedMLModel();
        
        Model model = new Model("model1", "Model 1 from VCell", "urn:sedml:language:sbml", "urn:miriam:biomodels.db:BIOMD0000000021");
        sedmlModel.addModel(model);
        
        Algorithm a1 = new Algorithm("KISAO:0000030");
        Simulation s1 = new UniformTimeCourse("simul1", "simul1", 0, 5, 7, 3, a1);
        AlgorithmParameter ap1 = new AlgorithmParameter("KISAO:0000211", "23");
        AlgorithmParameter ap2 = new AlgorithmParameter("KISAO:0000212", "25");
        Algorithm a2 = new Algorithm("KISAO:0000032");
        a2.addAlgorithmParameter(ap1);
        a2.addAlgorithmParameter(ap2);
        Simulation s2 = new OneStep("simul2", "simul2", a2, 5);
        Algorithm a3 = new Algorithm("KISAO:0000034");
        Simulation s3 = new SteadyState("simul3", "", a3);
        Simulation s4 = new SteadyState("simul4", null, a3);
        sedmlModel.addSimulation(s1);
        sedmlModel.addSimulation(s2);
        sedmlModel.addSimulation(s3);
        sedmlModel.addSimulation(s4);
       
        List<Simulation> simulations = sedmlModel.getSimulations();
        for(Simulation simulation : simulations) {
            log.debug(simulation.toString());
        }

// <task id="task1" modelReference="model1" simulationReference="simulation1" />
        Task task = new Task("task1", "Classic task", "model1", "simul1");
        sedmlModel.addTask(task);
        
/*
    <listOfRanges>
        <vectorRange id="current">
            <value> 1 </value>
            <value> 4 </value>
            <value> 10 </value>
        </vectorRange>
    </listOfRanges>
*/
       ArrayList<Double> values = new ArrayList<Double> (Arrays.asList(2.0, 3.0, 10.0));
       VectorRange r1 = new VectorRange("current", values);
       UniformRange r2 = new UniformRange("other", 3.006, 11.111, 27, UniformType.LINEAR);
       Variable var0 = new Variable("val0", "current range value", "model1", "#current");
       Variable var1 = new Variable("val1", "current range value", "model1", "#current");
       ASTNode math1 = new ASTPlus();
       ASTNode var0_Node = new ASTCi(var0.getId());
       ASTNode var1_Node = new ASTCi(var1.getId());
       math1.addChildNode(var0_Node);
       math1.addChildNode(var1_Node);

       Parameter p = new Parameter("Kf", "Kf", 0.5);
       Map<String, AbstractIdentifiableElement> variables1 = new HashMap<String, AbstractIdentifiableElement> ();
       Map<String, AbstractIdentifiableElement> parameters1 = new HashMap<String, AbstractIdentifiableElement> ();
       variables1.put(var0.getId(), var0);
       variables1.put(var1.getId(), var1);
       parameters1.put(p.getId(), p);
       FunctionalRange r3 = new FunctionalRange("frange3", "index", variables1, parameters1, math1);
       UniformRange r4 = new UniformRange("other", 5.432, 7.89, 11, UniformType.LOG);

/*
<repeatedTask id="task3" modelReference="model1" resetModel="false" range="current">
 */
       RepeatedTask t = new RepeatedTask("task3", "Repeated Task", false, "current");
       t.addRange(r1);
       t.addRange(r2);
       t.addRange(r3);
       t.addRange(r4);  // should fail, range id "other" already in use
/*
<listOfChanges>
    <setValue target="/sbml/model/listOfParameters/parameter[@id='w']" modelReference="model1" >
        <math>
            <ci> current </ci>
        </math>
    </setValue>
</listOfChanges>
*/ 
       Parameter p2 = new Parameter("Kr", "Kr", 0.4);
       Variable var2 = new Variable("val2", "current range value", "model1", "#current");
       Variable var3 = new Variable("val3", "current range value", "model1", "#current");
       ASTNode math2 = new ASTPlus();
       ASTNode var2_Node = new ASTCi(var2.getId());
       ASTNode var3_Node = new ASTCi(var3.getId());
       math2.addChildNode(var2_Node);
       math2.addChildNode(var3_Node);
       SetValue setValue = new SetValue(new XPathTarget("/sbml/model/listOfParameters/parameter[@id='sum']"), r3.getId(), "model1");
       setValue.addParameter(p2);
       setValue.addVariable(var2);
       setValue.addVariable(var3);
       setValue.setMath(math2);
       t.addChange(setValue);
/*
    <listOfSubTasks>
        <subTask task="task1" />
    </listOfSubTasks>
    
    <listOfSubTasks>
        <subTask order="1" task="task2" />
    </listOfSubTasks>
    
</repeatedTask>
*/
       t.addSubtask(new SubTask("1", "task0"));         // order + id attributes
       t.addSubtask(new SubTask(task.getId()));         // just id
       t.addSubtask(new SubTask("task2"));
       // the next 4 subtasks should not be added to subtask list (should even raise RuntimeException??)
       t.addSubtask(new SubTask("task2"));      // duplicate, will not be added again
       t.addSubtask(new SubTask(""));           // not a valid name
       t.addSubtask(new SubTask(null));         // not valid
       t.addSubtask(new SubTask("third", "task9")); // invalid order, must be string representation of an integer
       t.addSubtask(new SubTask(t.getId()));    // "this" task cannot be in the list of subtasks
       // and now a subtask with dependent tasks
       SubTask st = new SubTask("task7");
       st.addDependentTask(new SubTask("task2"));
       st.addDependentTask(new SubTask("task4"));
       // the next 2 dependent tasks should not show (should even raise RuntimeException??)
       st.addDependentTask(new SubTask(t.getId()));         // repeated task should not depend on itself
       st.addDependentTask(new SubTask(st.getTaskId()));    // subtask should not depend on itself
       t.addSubtask(st);
       
       sedmlModel.addTask(t);
       List<AbstractTask> taskss = sedmlModel.getTasks();
       for(AbstractTask tsk : taskss) {
           log.debug(tsk.toString());
       }
       File outfile = File.createTempFile("repeatedTaskDemo", ".xml");
       log.info("Writing repeated task demo to {}", outfile.getAbsolutePath());
       sedmlDocument.writeDocument(outfile);
       
       log.debug("");
       log.debug(" ------------------------------------------------------------------");
       log.debug("");

       sedmlModel = SEDMLReader.readFile(outfile);
       Namespace namespace = sedmlModel.getNamespace();
       List<Model> mmm = sedmlModel.getBaseModels();
       for(Model mm : mmm) {
           log.debug(mm.toString());
       }
       List<Simulation> sss = sedmlModel.getSimulations();
       for(Simulation ss : sss) {
           log.debug(ss.toString());
       }
       List<AbstractTask> ttt = sedmlModel.getTasks();
       for(AbstractTask tt : ttt) {
           log.debug(tt.toString());
       }
       List<DataGenerator> ddd = sedmlModel.getDataGenerators();
       for(DataGenerator dd : ddd) {
           log.debug(dd.toString());
       }
       List<Output> ooo = sedmlModel.getOutputs();
       for(Output oo : ooo) {
           log.debug(oo.toString());
       }
       log.debug("");
    }

    
}
