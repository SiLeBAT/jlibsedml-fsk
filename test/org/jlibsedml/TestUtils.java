package org.jlibsedml;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jlibsedml.execution.IModel2DataMappings;
import org.jlibsedml.execution.IRawSedmlSimulationResults;
import org.jmathml.ASTNode;

public class TestUtils {
    public static final String SEDML3 = "TestData/sedml_3.xml";
    public static final String SEDML12 = "TestData/sedMLBIOM12.xml";

    public static SedML readSedml3() throws JDOMException, IOException,
            XMLException {
        return readFile(new File(SEDML3));
    }

    public static SedML readSedml12() throws JDOMException, IOException,
            XMLException {
        return readFile(new File(SEDML12));
    }

    public static SedML readFile(File file) throws JDOMException, IOException,
            XMLException {
        SAXBuilder builder = new SAXBuilder();

        Document doc = builder.build(file);
        Element sedRoot = doc.getRootElement();
        try {
            SEDMLElementFactory.getInstance().setStrictCreation(false);
            SEDMLReader reader = new SEDMLReader();
            SedML sedML = reader.getSedDocument(sedRoot);
            return sedML;
        } finally {
            SEDMLElementFactory.getInstance().setStrictCreation(true);
        }
    }

    public static SedML createEmptySedml() {
        return new SedML(1, 1, Namespace.getNamespace(SEDMLTags.SEDML_L1V1_NS));
    }

    public static Simulation createAnySimulation(String id) {
        return new UniformTimeCourse(id, "name", 0, 0, 0, 0,
                createDefaultAlgorithm());
    }

    public static Algorithm createDefaultAlgorithm() {
        return new Algorithm("KISAO:0000001");
    }

    public static Curve createAnyCurve(String id) {
        return new Curve(id, "name", false, false, "dg1", "dg2");
    }

    public static Surface createAnySurface(String id) {
        return new Surface(id, "name", false, false, false, "dg1", "dg2", "dg3");
    }

    public static DataSet createAnyDataSet(String id) {
        return new DataSet(id, "name", "label", "ds");
    }

    public static Model createAnyModelWithID(String id) {
        return new Model(id, "anyname", "ANY_LANG", "file:///a/b/c.txt");
    }

    public static Variable createAnyVariableWithID(String id, String ref) {
        return new Variable(id, "varName", ref, "Xpath");
    }

    public static SedML createValidOutputConfig() {
        SedML sedml = createEmptySedml();
        Model m = new Model("mref", null, "lang", "src.xml");
        Simulation sim = new UniformTimeCourse("sref", null, 0, 0, 10, 100,
                new Algorithm("KISAO:0000019"));
        Task t1 = new Task("taskid", "", "mref", "sref");
        ASTNode node = Libsedml.parseFormulaString(" x + y");
        DataGenerator dg = new DataGenerator("dgid", "dgName", node);
        dg.addVariable(new Variable("x", "", t1.getId(), "[@id='x']"));
        dg.addVariable(new Variable("y", "", t1.getId(), "[@id='y']"));
        Report rep = new Report("reID", "");
        DataSet ds = new DataSet("dsid", "", "label", dg.getId());
        rep.addDataSet(ds);
        sedml.addModel(m);
        sedml.addSimulation(sim);
        sedml.addOutput(rep);
        sedml.addDataGenerator(dg);
        sedml.addTask(t1);
        return sedml;
    }

    public static IRawSedmlSimulationResults createResultsForValidOuputConfig() {
        return new IRawSedmlSimulationResults() {
            double[][] data = new double[][] { { 1, 2 }, { 10, 20 },
                    { 100, 300 } };

            public int getNumDataRows() {
                // TODO Auto-generated method stub
                return 3;
            }

            public int getNumColumns() {
                return 2;
            }

            public Double[] getDataByColumnId(String colID) {

                int indx = getIndexByColumnID(colID);
                Double[] rc = new Double[getNumDataRows()];
                for (int j = 0; j < getNumDataRows(); j++) {
                    rc[j] = data[j][indx];
                }
                return rc;
            }

            public int getIndexByColumnID(String colID) {
                int indx = -1;
                int i = 0;
                for (String hdr : getColumnHeaders()) {
                    if (colID.equals(hdr)) {
                        indx = i;
                    }
                    i++;
                }
                return indx;
            }

            public double[][] getData() {
                return data;
            }

            public String[] getColumnHeaders() {
                return new String[] { "x", "y" };
            }

            public IModel2DataMappings getMappings() {
                return new IModel2DataMappings() {

                    public boolean hasMappingFor(String spId) {
                        return true;
                    }

                    public String getColumnTitleFor(String speciesID) {
                        return speciesID;
                    }

                    public int getColumnIndexFor(String speciesID) {
                        for (int i = 0; i < getColumnHeaders().length; i++) {
                            if (getColumnHeaders()[i]
                                    .equalsIgnoreCase(speciesID)) {
                                return i;
                            }
                        }
                        return -1;
                    }
                };
            }

            public Double[] getDataByColumnIndex(int index) {
                Double[] rc = new Double[getNumDataRows()];
                for (int j = 0; j < getNumDataRows(); j++) {
                    rc[j] = data[j][index];
                }
                return rc;
            }

        };
    }

    /**
     * Creates a JDOM document from the given XML string (via a Reader)
     * 
     * @param reader
     * @return
     * @throws IOException
     */
    public static Element readXML(Reader reader) throws IOException {
        try {
            SAXBuilder builder = new SAXBuilder(false);
            Document sDoc = builder.build(reader);
            Element root = sDoc.getRootElement();
            return root;
        } catch (JDOMException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Variable createAnySymbolWithID(String id, String ref) {
        return new Variable(id, "varName", ref, VariableSymbol.TIME);
    }

    public static Parameter createAnyParameterWithID(String id, String name) {
        return new Parameter(id, name, 1);
    }

    public static Output createAnyOutput() {
        return new Report("id", "name");
    }

    public static Report createEmptyReport() {
        return new Report("id", "name");
    }

    public static ChangeAttribute createAnyChangeAttr(String string) {
        return new ChangeAttribute(new XPathTarget("target"), "value");
    }

    public static ChangeXML createAnyChangeXML() {
        return new ChangeXML(new XPathTarget("target/path"), new NewXML(
                Arrays.asList(new Element("myEl"))));
    }

    public static AddXML createAnyAddXML() {
        return new AddXML(new XPathTarget("target/path"), new NewXML(
                Arrays.asList(new Element("myEl"))));
    }

    public static AddXML createAdd2XMLelements() {
        Element el1 = createSBMLParameterELement("V_mT");
        Element el2 = new Element("parameter");
        el2.setAttribute("metaid", "metaid_0000011");
        el2.setAttribute("id", "V_mT2");
        el2.setAttribute("value", "0.71");
        return new AddXML(new XPathTarget("target/path"), new NewXML(
                Arrays.asList(el1, el2)));
    }

    public static Element createSBMLParameterELement(String id) {
        Element el1 = new Element("parameter");
        el1.setAttribute("metaid", "metaid_0000010");
        el1.setAttribute("id", "V_mT");
        el1.setAttribute("value", "0.7");
        return el1;
    }

    public static RemoveXML createAnyRemoveXML() {
        return new RemoveXML(new XPathTarget("target/path"));
    }

    public static ComputeChange createAnyComputeChange() {
        ComputeChange cc = new ComputeChange(new XPathTarget("/target"),
                Libsedml.parseFormulaString("c * 2 + k1"));
        cc.addVariable(new Variable("c", "c", "id", "path2model"));
        cc.addParameter(new Parameter("k1", "k1", 1));
        return cc;
    }

    public static Plot2D createAnyPlot2d() {
        return new Plot2D("id", "name");
    }

    public static Plot3D createAnyPlot3d() {
        return new Plot3D("id", "name");
    }

    public static Model createAModel() {
        return new Model("id", "name", "lang", "source");
    }

}
