package org.jlibsedml.execution;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.URI;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FileModelResolverTest {
    IModelResolver fileResolver;
    static URI TEST_XML1;
    URI TEST_NON_XML_EXISTING_FILE;
    URI TEST_XML_FILE_SCHEME;

    @Before
    public void setUp() throws Exception {
        fileResolver = new FileModelResolver();
        TEST_XML1 = new URI("TestData/BIOMD0000000012.xml");
        TEST_NON_XML_EXISTING_FILE = new URI("TestData/License.txt");
        TEST_XML_FILE_SCHEME = new URI("TestData/BIOMD0000000012.xml");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void testGetModelXMLFor() {
        String modelXML = fileResolver.getModelXMLFor(TEST_XML1);
        assertNotNull(modelXML);
    }

    @Test
    public final void testGetModelXMLForURIWithFileSchemeFailsWithRelativePath() {
        String modelXML = fileResolver.getModelXMLFor(TEST_XML_FILE_SCHEME);
        assertNotNull(modelXML);
    }

    @Test
    public final void testGetModelXMLForNonXMLFileReturnsNull() {
        String modelXML = fileResolver
                .getModelXMLFor(TEST_NON_XML_EXISTING_FILE);
        assertNull(modelXML);
    }
}
