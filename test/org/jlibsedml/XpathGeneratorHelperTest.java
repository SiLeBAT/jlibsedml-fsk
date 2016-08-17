package org.jlibsedml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jlibsedml.execution.FileModelResolver;
import org.jlibsedml.execution.IModelResolver;
import org.jlibsedml.modelsupport.SUPPORTED_LANGUAGE;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class XpathGeneratorHelperTest {
    SedML sed;
    Task t1 = new Task ("id", null, "m1", "sim1");
    Model m1 = new Model ("m1", "name",  SUPPORTED_LANGUAGE.SBML_GENERIC_ID,"TestData/BIOMD0000000012.xml");
    IdName id1 = new IdName("PX", "protein x");
    IdName id2 = new IdName("PY", "protein x");
    IdName unknown = new IdName("UNKNOWN", "protein x");
    IModelResolver fmr = new FileModelResolver();
    @Before
    public void setUp() throws Exception {
        sed = new SEDMLDocument().getSedMLModel();
        sed.addTask(t1);
        sed.addModel(m1);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void testAddIdentifiersAsDataGenerators() {
       
        assertEquals(0,sed.getDataGenerators().size());
        sed.addIdentifiersAsDataGenerators(t1,"id", false, fmr,id1, id2);
        
        assertEquals(2,sed.getDataGenerators().size());
        assertNotNull(sed.getDataGeneratorWithId("PX_dg"));
        assertNotNull(sed.getDataGeneratorWithId("PY_dg"));
        }
    
    @Test
    public final void testAddUnknownIdIdentifiersNotAdded() {
       
        assertEquals(0,sed.getDataGenerators().size());
        sed.addIdentifiersAsDataGenerators(t1, "id",false,fmr, unknown);
        assertEquals(0,sed.getDataGenerators().size());
      
    }
    
    @Test
    public final void testAddUnknownIdWithAllOrNOthingAddsNone() {
        assertEquals(0,sed.getDataGenerators().size());
        assertFalse(sed.addIdentifiersAsDataGenerators(t1,"id", true,fmr, id1, id2,unknown));
        assertEquals(0,sed.getDataGenerators().size());
      
    }
    @Test
    public final void testAddUnknownIdWithAllOrNOthingFalseAddMatches() {
        assertEquals(0,sed.getDataGenerators().size());
        assertTrue(sed.addIdentifiersAsDataGenerators(t1,"id", false, fmr,id1, id2,unknown));
        assertEquals(2,sed.getDataGenerators().size());
      
    }
    @Test
    public final void testDetachedTaskReturnsFalse() {
        Task t2 = new Task ("id", null, "m1", "sim1");
        assertEquals(0,sed.getDataGenerators().size());
        assertFalse(sed.addIdentifiersAsDataGenerators(t2,"id", false, fmr,id1, id2,unknown));
        assertEquals(0,sed.getDataGenerators().size());
      
    }

}
