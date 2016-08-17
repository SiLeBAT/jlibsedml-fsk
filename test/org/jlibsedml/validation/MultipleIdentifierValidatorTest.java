package org.jlibsedml.validation;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.jlibsedml.SEDMLDocument;
import org.jlibsedml.SedML;
import org.jlibsedml.TestUtils;
import org.jlibsedml.XMLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MultipleIdentifierValidatorTest {
    SedML sedml;

    @Before
    public void setUp() throws Exception {
        sedml = TestUtils.readFile(new File(
                "TestData/SEDMLFragments/predatorPreySedmlWithDups.xml"));
    }

    @After
    public void tearDown() throws Exception {
        
    }

    @Test
    public final void testValidateDetectsDuplicateIdsInTopLevelElements()
            throws XMLException {
        assertEquals(5, new SEDMLDocument(sedml).validate().size());
    }
}
