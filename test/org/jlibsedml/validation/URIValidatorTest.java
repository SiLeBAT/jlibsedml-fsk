package org.jlibsedml.validation;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.jlibsedml.Model;
import org.jlibsedml.XMLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class URIValidatorTest {
    Model valid1 = new Model("id", "name", "language", "file.txt");
    Model valid2 = new Model("id", "name", "language", "urn:biomodels:db:DBID");
    Model invalid1 = new Model("id", "name", "language", "<XX#$");
    ISedMLValidator val;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void testValidate() throws XMLException {
        val = new URIValidator(Arrays.asList(new Model[] { valid1 }));
        assertEquals(0, val.validate().size());
    }

    @Test
    public final void testValidate2() throws XMLException {
        val = new URIValidator(Arrays.asList(new Model[] { valid2 }));
        assertEquals(0, val.validate().size());
    }

    @Test
    public final void testInvalidURICreatesError() throws XMLException {
        val = new URIValidator(Arrays.asList(new Model[] { invalid1 }));
        assertEquals(1, val.validate().size());
    }

}
