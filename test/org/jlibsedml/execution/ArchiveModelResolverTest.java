package org.jlibsedml.execution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import org.jlibsedml.ArchiveComponents;
import org.jlibsedml.Libsedml;
import org.jlibsedml.Model;
import org.jlibsedml.SEDMLDocument;
import org.jlibsedml.XMLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArchiveModelResolverTest {

    final File SEDX = new File("TestData/sedx.sedx");
    ArchiveComponents ac = null;

    @Before
    public void setUp() throws Exception {
        ac = Libsedml.readSEDMLArchive(new FileInputStream(SEDX));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void testArchiveModelResolver() throws FileNotFoundException,
            XMLException {
        new ArchiveModelResolver(ac);
    }

    @Test
    public final void testGetModelXMLFor() throws URISyntaxException {
        ArchiveModelResolver amr = new ArchiveModelResolver(ac);
        SEDMLDocument doc = ac.getSedmlDocument();
        for (Model m : doc.getSedMLModel().getModels()) {
            String modelWithChangesApplied = amr.getModelXMLFor(m
                    .getSourceURI());
          //  System.err.println(modelWithChangesApplied);
            assertNotNull(modelWithChangesApplied);
            assertFalse(modelWithChangesApplied.contains("=\"250\""));
            ModelResolver mr = new ModelResolver(doc.getSedMLModel());
            mr.add(amr);
            String modified = mr.getModelString(m);
            assertTrue(modified.contains("=\"250\""));
        }
    }
}
