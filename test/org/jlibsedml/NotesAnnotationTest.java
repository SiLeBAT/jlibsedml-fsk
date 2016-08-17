package org.jlibsedml;


import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NotesAnnotationTest {
	Model model1 = TestUtils.createAnyModelWithID("id1");
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testBasicGetAddRemoveNotes(){
		assertNotNull(model1.getNotes());
		Element para = createANote();
		Notes note = new Notes(para);
		
		model1.addNote(note);
		assertEquals(note, model1.getNotes().get(0));
		
		model1.removeNote(note);
		assertEquals(0, model1.getNotes().size());
	}
	
	@Test
	public void testBasicGetAddRemoveAnnotation() throws XMLException{
		assertNull (model1.getAnnotation());
		Element para = new Element("myElement");
		para.setText("An example annotation content");
		para.setNamespace(Namespace.getNamespace("http://my.app.ns/level1"));
		Annotation ann = new Annotation(para);
		Annotation ann2 = new Annotation((Element)para.clone());
		SedML sed = new SEDMLDocument().getSedMLModel();
		model1.addAnnotation(ann);
	//	model1.addAnnotation(ann2);
		sed.addModel(model1);
		assertEquals(ann, model1.getAnnotation());
		
		model1.removeAnnotation(ann);
		assertNull(model1.getAnnotation());
	}
	
	@Test
	public void testNoteNSIsXHTML(){
		Element para = createANote();
		Notes note = new Notes(para);
		assertEquals(SEDMLTags.XHTML_NS, note.getNotesElement().getNamespace().getURI());	
	}
	
	@Test
	public void readNotesFromSEDMLFile() throws JDOMException, IOException, XMLException{
		SedML sedml = TestUtils.readSedml12();
		List<Notes> notes = sedml.getNotes();
		Element notesElement = notes.get(0).getNotesElement();
		assertNotNull(notesElement.getText());
	}
	
	@Test
	public void readGenerateXMLNotes() throws JDOMException, IOException, XMLException{
		Element para = createANote();
		Element para2 = createANote();
		Notes note = new Notes(para);
		Notes note2 = new Notes(para2);
		
		model1.setNote(note);
		model1.setNote(note2);
		SEDMLDocument doc = Libsedml.createDocument();
		SedML sedml = doc.getSedMLModel();
		sedml.addModel(model1);
		String written = doc.writeDocumentToString();
		
		SEDMLDocument newdoc = Libsedml.readDocumentFromString(written);
		assertEquals(1, newdoc.getSedMLModel().getModels().get(0).getNotes().size());
		assertTrue(written.contains("<p xmlns=\"http://www.w3.org/1999/xhtml\">An example paragraph</p>"));
	}

	private Element createANote() {
		Element para = new Element("p");
		para.setText("An example paragraph");
		return para;
	}
}
