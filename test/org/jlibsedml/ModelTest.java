package org.jlibsedml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.jlibsedml.validation.SEDMLSchemaValidator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelTest {
    Logger log = LoggerFactory.getLogger(ModelTest.class);
    Model model1 = TestUtils.createAnyModelWithID("id1");
    Model sameAsModel1 = TestUtils.createAnyModelWithID("id1");
    Model different = TestUtils.createAnyModelWithID("id12");

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void testHashCodeBasedOnID() {
        assertEquals(model1.hashCode(), sameAsModel1.hashCode());
        assertFalse(model1.hashCode() == different.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testModelDoesNotAllowNulArgs() {
        new Model(null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testModelDoesNotAllowEmptyStringArgs() {
        new Model("", "", "", "");
    }

    @Test
    public final void testEqualsBasedOnID() {
        assertEquals(model1, sameAsModel1);
        assertFalse(model1.equals(different));
    }

    @Test
    public final void testToString() {
        assertTrue(model1.toString().contains(model1.getId())
                && model1.toString().contains(model1.getName()));
    }

    @Test
    public final void testGetSourceURI() throws URISyntaxException {
        URI uri = model1.getSourceURI();
        log.info("auth: " + uri.getRawAuthority() + ",frag= "
                + uri.getFragment() + ", host:" + uri.getRawUserInfo()
                + ", scheme :" + uri.getScheme() + " , path :"
                + uri.getRawPath() + ", query: " + uri.getQuery() + ",  ui:"
                + uri.getUserInfo());
        log.info(uri.toString());
    }

    @Test
    public final void testGetSourceURIIsRelative() throws URISyntaxException {

        assertFalse(model1.isSourceURIRelative());
        model1.setSource("relative.xml");
        assertTrue(model1.isSourceURIRelative());
        model1.setSource("../relative.xml");
        assertTrue(model1.isSourceURIRelative());
        model1.setSource("./relative");
        assertTrue(model1.isSourceURIRelative());

    }

    @Test
    public void testGetBaseModels() {
        Model model1 = TestUtils.createAnyModelWithID("id1");
        model1.setSource("externalRef");
        Model model2 = new Model("id2", "", "lang", "id1");
        Model model3 = new Model("id3", "", "lang", "id2");
        SedML sedml = TestUtils.createEmptySedml();
        sedml.addModel(model2);
        sedml.addModel(model1);

        sedml.addModel(model3);
        assertEquals(1, sedml.getBaseModels().size());
        assertEquals(model1, sedml.getBaseModels().get(0));

    }

    @Test
    public void testGetChangesReturnsInSchemaOrder() throws XMLException {
        Model model1 = TestUtils.createAnyModelWithID("id");
        Change catt = TestUtils.createAnyChangeAttr("att1");
        Change aXML = TestUtils.createAnyAddXML();
        Change cXML = TestUtils.createAnyChangeXML();
        Change ccXML = TestUtils.createAnyComputeChange();
        Change dXML = TestUtils.createAnyRemoveXML();
        // add in non-schema order
        List<Change> allChanges = Arrays
                .asList(new Change[] { aXML, ccXML, dXML, cXML, catt });
        for (Change x : allChanges) {
            model1.addChange(x);
        }
        // check that these are returned in an order suitable for the schema.
        SEDMLDocument doc = new SEDMLDocument();
        doc.getSedMLModel().addModel(model1);
        List<SedMLError> errs = new SEDMLSchemaValidator(doc.getSedMLModel())
                .validate();
        printErrors(errs);
        assertTrue(errs.isEmpty());

        assertEquals(5, model1.getListOfChanges().size());
        model1.removeChange(catt);
        assertEquals(4, model1.getListOfChanges().size());
        model1.clearChanges();
        assertEquals(0, model1.getListOfChanges().size());
        assertFalse(model1.hasChanges());

    }

    private void printErrors(List<SedMLError> errs) {
        for (SedMLError err : errs) {
            log.info("Validation error at {}", err.getMessage());
        }
    }
}
