package org.jlibsedml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jdom.JDOMException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LibSEDMLTest {

    static final File TEST_ARCHIVE = new File("TestData/sedml.sedx");

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCreateDocumentIsValid() {
        SEDMLDocument doc = Libsedml.createDocument();
        assertNotNull(doc);
        assertFalse(doc.hasErrors());
    }

    @Test
    public void testCreateMiaseArchive() throws Exception {
        byte[] b = createSedxArchive();
        assertTrue(b.length > 0);
    }

    private byte[] createSedxArchive() throws XMLException, JDOMException,
            IOException {
        SEDMLDocument doc = new SEDMLDocument(TestUtils.readSedml3());
        File modelFile = new File("TestData/BIOMD0000000012.xml");
        byte[] bytes = Libsedml
                .writeSEDMLArchive(
                        new ArchiveComponents(
                                Arrays.asList(new IModelContent[] { new FileModelContent(
                                        modelFile) }), doc), "mysedml");
        return bytes;
    }

    @Test
    public void testCreateSedxWriteReadRoundTrip() throws Exception {
        byte[] miase = createSedxArchive();
        File tmp = File.createTempFile("sedx", ".sedx");
        FileUtils.writeByteArrayToFile(tmp, miase);
     
        ArchiveComponents ac = Libsedml.readSEDMLArchive(new FileInputStream(
                tmp));
        assertEquals("BIOMD0000000012.xml", ac.getModelFiles().get(0).getName());
        assertNotNull(ac.getSedmlDocument());
        IModelContent modelFile = ac.getModelFiles().get(0);
        assertTrue(modelFile.getContents().contains("<sbml xmlns=\"http://www.sbml.org/sbml/level2\" metaid=\"_153818\" level=\"2\" version=\"1\">"));
        tmp.deleteOnExit();
    }


    @Test
    public void testParseFormulaStringReturnsNullIfMathStringIsNullOrEmpty() {
        assertNull(Libsedml.parseFormulaString(""));
        assertNull(Libsedml.parseFormulaString(null));
    }

    @Test
    public void testIsSEDMLOKForValidFile() {
        File OKFILE = new File(TestUtils.SEDML12);
        assertTrue(Libsedml.isSEDML(OKFILE));
    }
    
    @Test
    public void testReadFromStream() throws XMLException, IOException {
        File OKFILE = new File(TestUtils.SEDML12);
       FileInputStream fis = new FileInputStream(OKFILE);
       SEDMLDocument doc = Libsedml.readDocument(fis, "UTF-8");
       assertNotNull(doc.getSedMLModel());
       assertEquals(0, doc.validate().size());
       fis.close();
       fis = new FileInputStream(OKFILE);
       doc = Libsedml.readDocument(fis, null);
       assertNotNull(doc.getSedMLModel());
       assertEquals(0, doc.validate().size());
       
    }


    @Test
    public void testIsSEDMLOKFalseForNonSEDML_XMLFile() {
        File NOTOKFILE = new File("TestData/abc_1.xml");
        assertFalse(Libsedml.isSEDML(NOTOKFILE));
    }

    @Test
    public void testIsSEDMLOKFalseForUnreadbableFile() {
        File NOTOKFILE = new File("TestData/sedml.zip");
        assertFalse(Libsedml.isSEDML(NOTOKFILE));
    }
    
    @Test
    public void testExportBugReport () throws IOException, XMLException{
        String testDataFolder = "TestData";
        SEDMLDocument anySEDML = Libsedml
                .readDocument(new File("TestData/sedMLBIOM12.xml"));
        List<IModelContent> models = new ArrayList<IModelContent>();
        for (String s : new File(testDataFolder).list()) {
            if (s.endsWith(".xml") && s.startsWith("BIOMD")) {
                File modelFile = new File(s);
                FileModelContent fmc = new FileModelContent(modelFile);
                models.add(fmc);
            }
        }

        byte[] sedx = Libsedml.writeSEDMLArchive(
                new ArchiveComponents(models, anySEDML), "any");

        File outfile = File.createTempFile("sedxExport", ".sedx");
        FileOutputStream fos = new FileOutputStream(outfile);
        fos.write(sedx);
        fos.flush();
        fos.close();
        
        // now open, has 2 files:
        ArchiveComponents archive = Libsedml.readSEDMLArchive(new FileInputStream(outfile));
        assertEquals(2, archive.getModelFiles().size());
    
    }

}
