package org.jlibsedml.modelsupport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class KisaoOntologyTest {

    String ROOT_KISAO_ID = "KISAO:0000000";
    String KISAO_TERM_CVODE = "KISAO:0000019";

    String GENERAL_ODE = "KISAO:0000433";

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        assertNotNull(KisaoOntology.ALGORITHM_WITH_DETERMINISTIC_RULES);
        assertEquals(264, KisaoOntology.getInstance().getTerms().size());
    }

    @Test
    public void testFindById() {
        KisaoTerm t17 = KisaoOntology.getInstance()
                .getTermById("KISAO:0000017");
        assertNotNull(t17);
        assertEquals(1, t17.getIsa().size());

    }

    @Test
    public void testCVODE_ISA_ODE() {

        KisaoTerm ODE = KisaoOntology.getInstance().getTermById(GENERAL_ODE);
        KisaoTerm CVODE = KisaoOntology.getInstance().getTermById(
                KISAO_TERM_CVODE);
        assertTrue(CVODE.is_a(ODE));
        assertFalse(ODE.is_a(CVODE));
    }

//    @Test
//    public void testEveryElementISA_ROOT_ELEMENT() {
//
//        KisaoTerm ROOT = KisaoOntology.getInstance().getTermById(ROOT_KISAO_ID);
//        for (KisaoTerm term : KisaoOntology.getInstance().getTerms()) {
//            if (term.isObsolete()) {
//                continue;
//            }
//            if(!term.is_a(ROOT)) {
//                System.err.println(term);
//            }
//            assertTrue(term.is_a(ROOT));
//        }
//
//    }

}
