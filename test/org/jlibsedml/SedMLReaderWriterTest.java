package org.jlibsedml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jdom.DataConversionException;
import org.jdom.Element;
import org.jmathml.ASTRootNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SedMLReaderWriterTest {

	SEDMLWriter writer = new SEDMLWriter();
	SEDMLReader reader = new SEDMLReader();
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetXMLCurve() {
		
		Curve c1 = TestUtils.createAnyCurve("c1");
		c1.addNote(createNote());
		Element el = writer.getXML(c1);
		assertCurveAttributesSet(c1, el);
		Curve c2 = reader.getCurve(el);
		assertHasNote(c2);
		assertCurveAttributesSet(c2, el);
	}
	
	@Test
	public final void testGetAlgorithm(){
		Algorithm alg = TestUtils.createDefaultAlgorithm();
		alg.addNote(createNote());
		Element el = writer.getXML(alg);
		Algorithm alg2=reader.getAlgorithm(el);
		assertHasNote(alg2);
		
	}
	
	@Test
	public final void testGetTask(){
	    AbstractTask task = TestUtils.createValidOutputConfig().getTasks().get(0);
		task.addNote(createNote());
		Element el = writer.getXML(task);
		AbstractTask task2=reader.getTask(el);
		assertHasNote(task2);
	}
	
	@Test
	public final void testReadWriteSimulation(){
		Simulation sim = TestUtils.createAnySimulation("id");
		sim.addNote(createNote());
		Element el = writer.getXML(sim);
		Simulation sim2=reader.getSimulation(el);
		assertHasNote(sim2);
	}

	private void assertHasNote(SEDBase obj) {
		assertEquals("Hello", obj.getNotes().get(0).getNotesElement().getText());		
	}

	private Notes createNote() {
		return new Notes(new Element("p").setText("Hello"));
	}

	private void assertCurveAttributesSet(Curve c1, Element el) {
		assertEquals(c1.getId(),el.getAttribute(SEDMLTags.OUTPUT_ID).getValue());
		assertEquals(c1.getName(),el.getAttribute(SEDMLTags.OUTPUT_NAME).getValue());
		assertEquals(Boolean.toString(c1.getLogX()),el.getAttribute(SEDMLTags.OUTPUT_LOG_X).getValue());
		assertEquals(Boolean.toString(c1.getLogY()),el.getAttribute(SEDMLTags.OUTPUT_LOG_Y).getValue());
		assertEquals(c1.getXDataReference(),el.getAttribute(SEDMLTags.OUTPUT_DATA_REFERENCE_X).getValue());
		assertEquals(c1.getYDataReference(),el.getAttribute(SEDMLTags.OUTPUT_DATA_REFERENCE_Y).getValue());
		
	}
	
	private void assertSurfaceAttributesSet(Surface c1, Element el) {
		assertEquals(c1.getId(),el.getAttribute(SEDMLTags.OUTPUT_ID).getValue());
		assertEquals(c1.getName(),el.getAttribute(SEDMLTags.OUTPUT_NAME).getValue());
		assertEquals(Boolean.toString(c1.getLogX()),el.getAttribute(SEDMLTags.OUTPUT_LOG_X).getValue());
		assertEquals(Boolean.toString(c1.getLogY()),el.getAttribute(SEDMLTags.OUTPUT_LOG_Y).getValue());
		assertEquals(c1.getXDataReference(),el.getAttribute(SEDMLTags.OUTPUT_DATA_REFERENCE_X).getValue());
		assertEquals(c1.getYDataReference(),el.getAttribute(SEDMLTags.OUTPUT_DATA_REFERENCE_Y).getValue());
		
	}

	@Test
	public final void testGetXMLSurface() {
		Surface s1 = TestUtils.createAnySurface("s1");
		s1.addNote(createNote());
		Element el = writer.getXML(s1);
		assertSurfaceAttributesSet(s1, el);
		assertSurfaceAttributes(s1, el);
		Surface s2 = reader.getSurface(el);
		assertSurfaceAttributesSet(s2, el);
		assertSurfaceAttributes( s2,el);
		assertHasNote(s2);
		
	}

	private void assertSurfaceAttributes(Surface s2, Element el) {
		assertEquals(Boolean.toString(s2.getLogZ()),el.getAttribute(SEDMLTags.OUTPUT_LOG_Z).getValue());
		assertEquals(s2.getZDataReference(),el.getAttribute(SEDMLTags.OUTPUT_DATA_REFERENCE_Z).getValue());
	}

	@Test
	public final void testGetXMLDataSet() {
		DataSet ds = TestUtils.createAnyDataSet("ds1");
		ds.addNote(createNote());
		Element el = writer.getXML(ds);
		
		checkDatasetAttributes(ds, el);
		DataSet d2 = reader.getDataset(el);
		checkDatasetAttributes(d2, el);
		assertHasNote(d2);
	}

	private void checkDatasetAttributes(DataSet ds, Element el) {
		assertEquals(ds.getId(),el.getAttribute(SEDMLTags.OUTPUT_ID).getValue());
		assertEquals(ds.getName(),el.getAttribute(SEDMLTags.OUTPUT_NAME).getValue());
		assertEquals(ds.getLabel(),el.getAttribute(SEDMLTags.OUTPUT_DATASET_LABEL).getValue());
		assertEquals(ds.getDataReference(),el.getAttribute(SEDMLTags.OUTPUT_DATA_REFERENCE).getValue());
	}
	
	@Test
	public void testVariableInModelSetsCorrectAttribute(){
		
		Variable var = TestUtils.createAnyVariableWithID("varID", "modelREF");
		var.addNote(createNote());
		ComputeChange change = new ComputeChange(new XPathTarget("target"), new ASTRootNode());
		change.addVariable(var);
		
		Element changeXML = writer.getXML(change);
		Element listOfVars = changeXML.getChild(SEDMLTags.COMPUTE_CHANGE_VARS);
		Element varEl = (Element)listOfVars.getChildren().get(0);
		assertTrue(varEl.getAttribute(SEDMLTags.VARIABLE_MODEL)!=null);
		assertTrue(varEl.getAttribute(SEDMLTags.VARIABLE_TASK)==null);
				assertVarAttributes(var, varEl, true, true);
		Variable var2=reader.createVariable(varEl, true);
		assertHasNote(var2);

		assertVarAttributes(var2, varEl, true, true);
		
	}
	
	@Test
	public void testParameterReadWrite() throws DataConversionException {
		Parameter p = TestUtils.createAnyParameterWithID("id", "name");
		p.addNote(createNote());
		Element pEl = writer.getXML(p);
		assertParameterAttributes(pEl,p);
		Parameter p2 = reader.createParameter(pEl);
		assertParameterAttributes(pEl,p2);
		assertHasNote(p2);
	}
	
	@Test
	public void testPlot2dReadWrite() throws DataConversionException {
		Plot2D p = TestUtils.createAnyPlot2d();
		p.addNote(createNote());
		Element pEl = writer.getXML(p);
		assertPlot2dattributes(pEl,p);
		Plot2D p2 = (Plot2D)reader.getOutput(pEl);
		assertPlot2dattributes(pEl,p2);
		assertHasNote(p2);
	}
	
	@Test
	public void testPlot3dReadWrite() throws DataConversionException {
		Plot3D p = TestUtils.createAnyPlot3d();
		p.addNote(createNote());
		Element pEl = writer.getXML(p);
		assertPlot3dattributes(pEl,p);
		Plot3D p2 = (Plot3D)reader.getOutput(pEl);
		assertPlot3dattributes(pEl,p2);
		assertHasNote(p2);
	}
	
	@Test
	public void testReportReadWrite() throws DataConversionException {
		Report p = TestUtils.createEmptyReport();
		p.addNote(createNote());
		Element pEl = writer.getXML(p);
		assertReportattributes(pEl,p);
		Report p2 = (Report)reader.getOutput(pEl);
		assertReportattributes(pEl,p2);
		assertHasNote(p2);
	}
	
	private void assertReportattributes(Element pEl, Report p) {
		assertEquals(p.getId(),pEl.getAttribute(SEDMLTags.OUTPUT_ID).getValue());
		assertEquals(p.getName(),pEl.getAttribute(SEDMLTags.OUTPUT_NAME).getValue());
		
		
	}

	private void assertPlot3dattributes(Element pEl, Plot3D p) {
		assertEquals(p.getId(),pEl.getAttribute(SEDMLTags.OUTPUT_ID).getValue());
		assertEquals(p.getName(),pEl.getAttribute(SEDMLTags.OUTPUT_NAME).getValue());		
	}

	private void assertPlot2dattributes(Element pEl, Plot2D p) {
		assertEquals(p.getId(),pEl.getAttribute(SEDMLTags.OUTPUT_ID).getValue());
		assertEquals(p.getName(),pEl.getAttribute(SEDMLTags.OUTPUT_NAME).getValue());
	}

	@Test
	public void testChangeAttributeReadWrite () throws DataConversionException{
		ChangeAttribute  c = TestUtils.createAnyChangeAttr("id");
		c.addNote(createNote());
		Element cEl = writer.getXML(c);
		assertChangeAttributes(cEl, c);
		ChangeAttribute c2 =(ChangeAttribute) reader.getChange(cEl);
		assertChangeAttributes(cEl,c2);
		assertHasNote(c2);
	}
	
	@Test
	public void testChangeXMLReadWriteWithMultipleElements () throws DataConversionException{
	  
	}
	
	@Test
	public void testChangeXMLReadWrite () throws DataConversionException{
		ChangeXML  c = TestUtils.createAnyChangeXML();
		c.addNote(createNote());
		Element cEl = writer.getXML(c);
		assertChangeXMLAttributes(cEl, c);
		ChangeXML c2 =(ChangeXML) reader.getChange(cEl);
		assertEquals("myEl",c2.getNewXML().getXml().get(0).getName());
		assertHasNote(c2);
	}
	
	@Test
	public void testRemoveXMLReadWrite () throws DataConversionException{
		RemoveXML  c = TestUtils.createAnyRemoveXML();
		c.addNote(createNote());
		Element cEl = writer.getXML(c);
		assertRemoveXMLAttributes(cEl, c);
		RemoveXML c2 =(RemoveXML) reader.getChange(cEl);
		assertRemoveXMLAttributes(cEl, c2);
		assertHasNote(c2);
	}
	
	@Test
	public void testAddXMLReadWrite () throws DataConversionException{
		AddXML  c = TestUtils.createAnyAddXML();
		c.addNote(createNote());
		Element cEl = writer.getXML(c);
		assertAddXMLAttributes(cEl, c);
		AddXML c2 =(AddXML) reader.getChange(cEl);
		assertEquals("myEl",c2.getNewXML().getXml().get(0).getName());
		assertHasNote(c2);
	}
	
	@Test
	public void testAddXMLReadWriteWith2XMLelements () throws DataConversionException{
		AddXML  c = TestUtils.createAdd2XMLelements();
		assertEquals(2,c.getNewXML().numElements());
		c.addNote(createNote());
		Element cEl = writer.getXML(c);
		assertAddXMLAttributes(cEl, c);
		AddXML c2 =(AddXML) reader.getChange(cEl);
		assertEquals("parameter",c2.getNewXML().getXml().get(0).getName());
		assertEquals("V_mT",c2.getNewXML().getXml().get(0).getAttribute("id").getValue());
		assertEquals("V_mT2",c2.getNewXML().getXml().get(1).getAttribute("id").getValue());
		assertHasNote(c2);
		assertEquals(2,c2.getNewXML().numElements());
	}
	
	
	@Test
	public void testCompChangeReadWrite () throws DataConversionException{
		ComputeChange  c = TestUtils.createAnyComputeChange();
		c.addNote(createNote());
		Element cEl = writer.getXML(c);
		assertComputeChange(cEl, c);
		ComputeChange c2 =(ComputeChange) reader.getChange(cEl);
		assertComputeChange(cEl,c2);
		assertHasNote(c2);
	}
	
	private void assertComputeChange(Element cEl, ComputeChange c) throws DataConversionException {
		assertEquals(c.getTargetXPath().getTargetAsString(),cEl.getAttribute(SEDMLTags.CHANGE_ATTR_TARGET).getValue());
		Element varList = cEl.getChild(SEDMLTags.COMPUTE_CHANGE_VARS);
		Element  var =(Element) varList.getChildren().get(0);
		assertVarAttributes( c.getListOfVariables().get(0),var, true, true);
		Element pList = cEl.getChild(SEDMLTags.COMPUTE_CHANGE_PARAMS);
		Element  p =(Element) pList.getChildren().get(0);
		assertParameterAttributes(p, c.getListOfParameters().get(0));
	}

	private void assertChangeXMLAttributes(Element cEl, ChangeXML c) {
		assertEquals(c.getNewXML().getXml().get(0),cEl.getChild("newXML").getChildren().get(0));
		assertEquals(c.getTargetXPath().getTargetAsString(),cEl.getAttribute(SEDMLTags.CHANGE_ATTR_TARGET).getValue());
	}
	
	private void assertAddXMLAttributes(Element cEl, AddXML c) {
		assertEquals(c.getNewXML().getXml().get(0),cEl.getChild("newXML").getChildren().get(0));
		assertEquals(c.getTargetXPath().getTargetAsString(),cEl.getAttribute(SEDMLTags.CHANGE_ATTR_TARGET).getValue());
	}
	
	private void assertRemoveXMLAttributes(Element cEl, RemoveXML c) {
		assertEquals(c.getTargetXPath().getTargetAsString(),cEl.getAttribute(SEDMLTags.CHANGE_ATTR_TARGET).getValue());
	}

	private void assertChangeAttributes(Element cEl, ChangeAttribute c) {
		assertEquals(c.getNewValue(),cEl.getAttribute(SEDMLTags.CHANGE_ATTR_NEWVALUE).getValue());
		assertEquals(c.getTargetXPath().getTargetAsString(),cEl.getAttribute(SEDMLTags.CHANGE_ATTR_TARGET).getValue());
		
	}

	private void assertParameterAttributes(Element pEl, Parameter p) throws DataConversionException {
		assertEquals(p.getId(),pEl.getAttribute(SEDMLTags.PARAMETER_ID).getValue());
		assertEquals(p.getName(),pEl.getAttribute(SEDMLTags.PARAMETER_NAME).getValue());
		assertEquals(p.getValue(),pEl.getAttribute(SEDMLTags.PARAMETER_VALUE).getDoubleValue(),0.0001);
		
	}
	
	@Test
	public void testReadWriteMetaId() throws DataConversionException{
		ComputeChange  c = TestUtils.createAnyComputeChange();
		c.setMetaId("meta1");
		Element cEl = writer.getXML(c);
		assertEquals("meta1", cEl.getAttribute(SEDMLTags.META_ID_ATTR_NAME).getValue());
		Change c1 = reader.getChange(cEl);
		assertEquals("meta1",c1.getMetaId());
		
	}

	@Test
	public void testVariableAsSymbol(){
		
		Variable var = TestUtils.createAnySymbolWithID("varID", "modelREF");
		var.addNote(createNote());
		ComputeChange change = new ComputeChange(new XPathTarget("target"), new ASTRootNode());
		change.addVariable(var);
		
		Element changeXML = writer.getXML(change);
		Element listOfVars = changeXML.getChild(SEDMLTags.COMPUTE_CHANGE_VARS);
		Element varEl = (Element)listOfVars.getChildren().get(0);
		assertVarAttributes(var, varEl, true, false);
		assertTrue(varEl.getAttribute(SEDMLTags.VARIABLE_MODEL)!=null);
		assertEquals(VariableSymbol.TIME.getUrn(),varEl.getAttribute(SEDMLTags.VARIABLE_SYMBOL).getValue());
		assertNull(varEl.getAttribute(SEDMLTags.VARIABLE_TARGET));
		
		Variable var2=reader.createVariable(varEl, true);
		assertHasNote(var2);
		assertVarAttributes(var2, varEl, true, false);
	}
	
	void assertVarAttributes(Variable var, Element varEl, boolean isModel, boolean isVar) {
		assertEquals(var.getId(),varEl.getAttribute(SEDMLTags.OUTPUT_ID).getValue());
		assertEquals(var.getName(),varEl.getAttribute(SEDMLTags.OUTPUT_NAME).getValue());
		if(isModel==true){
			assertEquals(var.getReference(),varEl.getAttribute(SEDMLTags.VARIABLE_MODEL).getValue());
		} else
			assertEquals(var.getReference(),varEl.getAttribute(SEDMLTags.VARIABLE_TASK).getValue());
		
		if(isVar==true){
			assertEquals(var.getTarget(),varEl.getAttribute(SEDMLTags.VARIABLE_TARGET).getValue());
		} else
			assertEquals(var.getSymbol().getUrn(),varEl.getAttribute(SEDMLTags.VARIABLE_SYMBOL).getValue());
		
		
	}
	
	@Test
	public void testVariableInDGSetsCorrectAttribute() throws DataConversionException{
		
		Variable var = TestUtils.createAnyVariableWithID("varID", "taskREF");
		DataGenerator dg = new DataGenerator("id", "name", new ASTRootNode());
		dg.addNote(createNote());
		dg.addVariable(var);
		
		Element dgEl = writer.getXML(dg);
		Element listOfVars = dgEl.getChild(SEDMLTags.DATAGEN_ATTR_VARS_LIST);
		Element varEl = (Element)listOfVars.getChildren().get(0);
		assertVarAttributes(var, varEl, false, true);
		assertTrue(varEl.getAttribute(SEDMLTags.VARIABLE_MODEL)==null);
		assertTrue(varEl.getAttribute(SEDMLTags.VARIABLE_TASK)!=null);
		assertEquals("taskREF",varEl.getAttribute(SEDMLTags.VARIABLE_TASK).getValue());
		Variable var2=reader.createVariable(varEl, false);
		DataGenerator dg2 = reader.getDataGenerator(dgEl);
		assertHasNote(dg2);
		assertVarAttributes(var2, varEl, false, true);	
		
	}

}
