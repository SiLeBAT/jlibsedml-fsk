import org.jlibsedml.*;
import org.jlibsedml.modelsupport.SBMLSupport;
import org.jlibsedml.modelsupport.SUPPORTED_LANGUAGE;
import org.jmathml.ASTNode;

public class Main {

    public static void main(String[] args) {

        SBMLSupport support = new SBMLSupport();

        // create new SED-ML document object
        SEDMLDocument doc = new SEDMLDocument();
        SedML sedml = doc.getSedMLModel();

        // Adds dummy simulation
        Simulation sim = new FskSimulation("sim1", "simulation-eins", "R", "deterministic");
        sedml.addSimulation(sim);
        sedml.addSimulation(new FskSimulation("sim2", "simulation-zwei", "Matlab", "statistic"));
        sedml.addSimulation(new FskSimulation("sim3", "simulation-drei", "Python", "probabilistic"));

        // Adds model
        Model model = new Model("Model1", "Virus Decay Model Brookes", SUPPORTED_LANGUAGE.SBML_GENERIC.getURN(),
                "model.xml");
        model.addChange(new ChangeAttribute(new XPathTarget(support.getXPathForGlobalParameter("temp")), support.getXPathForGlobalParameter("temp")));
        model.addChange(new ChangeAttribute(new XPathTarget(support.getXPathForGlobalParameter("time")), "80"));
        sedml.addModel(model);

        // Adds task
        Task task = new Task("Task1", null, model.getId(), sim.getId());
        sedml.addTask(task);

        // Adds data generator
        ASTNode root = Libsedml.parseFormulaString("VirusEndConcentration");
        DataGenerator dg = new DataGenerator("VirusEndConcentration", "Virus concentration after decay", root);
        String xpath = support.getXPathForGlobalParameter("DecayedConc");
        dg.addVariable(new Variable("DecayedConc", null, task.getId(), xpath));
        sedml.addDataGenerator(dg);

        {
            // First script is embedded
            String contents = "hist(result, breaks=50, " +
                    "main='PREVALENCE OF PARENTS FLOCKS', xlab='Prevalence', col='32')";
            contents = contents + "\n" + contents;
//            sedml.addOutput(new Script("id", "name", "text/x-r", contents, true));

            // Second script is referenced
            sedml.addOutput(new Script("id1", "name1", "text/x-python", "script.py", false));
            sedml.addOutput(new Script("id2", "name2", "text/x-r", "script.r", false));

            // TODO: Issue in SedML document: Only one Script is allowed
        }

        System.out.println(doc.writeDocumentToString());
//        File f = new File("C:/Temp/mysim.sedml");
//        doc.writeDocument(f);
    }
}
