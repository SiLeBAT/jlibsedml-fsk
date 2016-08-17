package org.jlibsedml.modelsupport;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;

import org.jlibsedml.execution.IModelResolver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BioModelsRetrieverTest {

    IModelResolver resolver = new BioModelsModelsRetriever();

    final static String MIRIAM_WITH_SUFFIX = "urn:miriam:biomodels.db:BIOMD0000000128.xml";
    final static String MIRIAM_WITH_NO_SUFFIX = "urn:miriam:biomodels.db:BIOMD0000000128";

    @Before
    public void setUp() throws Exception {
        resolver = new BioModelsModelsRetriever();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void testGetModelXMLForNullReturnsNull()
            throws URISyntaxException {
        assertNull(resolver.getModelXMLFor(null));
        URI wrongURI = new URI("myURI/wrong");
        assertNull(resolver.getModelXMLFor(wrongURI));
    }

    // Failing due to proxy
//    @Test
//    public final void testGetModelXMLForMiriamWithSuffix()
//            throws URISyntaxException {
//        URI okURI = new URI(MIRIAM_WITH_SUFFIX);
//        String model = resolver.getModelXMLFor(okURI);
//        assertNotNull(model);
//        assertTrue(model.contains("<sbml"));
//    }

    @Test
    public final void testGetModelXMLForMiriamDoesNotHangForNotFoundModel()
            throws URISyntaxException {
        URI notokURI = new URI("urn:miriam:biomodels.db:BIOMD000000128"); // 1
                                                                          // char
                                                                          // missing
        String model = resolver.getModelXMLFor(notokURI);
        assertNull(model);
    }

    @Test
    public final void testGetModelXMLForMiriamDoesNotHangForNotFoundModel3()
            throws URISyntaxException {
        URI notokURI = new URI("urn:miriam:biomodels.db:BIOMD000000.xml"); // 1
                                                                           // char
                                                                           // missing
        String model = resolver.getModelXMLFor(notokURI);
        assertNull(model);
    }

    @Test
    public final void testGetModelXMLForMiriamDoesNotHangForNotFoundModel2()
            throws URISyntaxException {
        URI notokURI = new URI("urn:miriam:biomodels.db"); // 1 char missing
        String model = resolver.getModelXMLFor(notokURI);
        assertNull(model);

    }

    // Failing due to proxy
//    @Test
//    public final void testGetModelXMLForMiriamWithNoSuffix()
//            throws URISyntaxException {
//        URI okURI = new URI(MIRIAM_WITH_NO_SUFFIX);
//        String model = resolver.getModelXMLFor(okURI);
//        assertNotNull(model);
//        assertTrue(model.contains("<sbml"));
//    }

}
