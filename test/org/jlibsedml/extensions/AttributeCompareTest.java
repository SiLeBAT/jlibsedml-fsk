package org.jlibsedml.extensions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AttributeCompareTest {
    XMLCompare cmp = new XMLCompare();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void testAttributeequals() {
        Attribute a1 = new Attribute("n", "v", createTestNS());
        Attribute a2 = new Attribute("n", "v", createTestNS());
        assertFalse(a1.equals(a2));
        assertTrue(cmp.equals(a1, a2));
    }

    Namespace createTestNS() {
        return Namespace.getNamespace("p", "url");
    }

    Namespace createTestNS2() {
        return Namespace.getNamespace("p2", "url2");
    }

    @Test
    public final void testSingleElementequals() {
        Element e1 = new Element("n1", createTestNS());
        e1.setAttribute(new Attribute("a", "1"));
        Element e2 = new Element("n1", createTestNS());
        e2.setAttribute(new Attribute("a", "1"));
        assertTrue(cmp.equals(e1, e2));
    }

    @Test
    public final void testElementNotEqualWithDifferentNames() {
        Element e1 = new Element("n1", createTestNS());
        Element e2 = new Element("n2", createTestNS());

        assertFalse(cmp.equals(e1, e2));
    }

    @Test
    public final void testElementNotEqualWithSameNameButDifferentNS() {
        Element e1 = new Element("n1", createTestNS2());
        Element e2 = new Element("n1", createTestNS());
        assertFalse(cmp.equals(e1, e2));
    }

    @Test
    public final void testElementNotEqualIfE2HasAttr() {
        Element e1 = new Element("n1", createTestNS());
        Element e2 = new Element("n1", createTestNS());
        e2.setAttribute(new Attribute("a", "1"));
        assertFalse(cmp.equals(e1, e2));
    }

    @Test
    public final void testElementNotEqualIfE1HasAttr() {
        Element e1 = new Element("n1", createTestNS());
        Element e2 = new Element("n1", createTestNS());
        e1.setAttribute(new Attribute("a", "1"));
        assertFalse(cmp.equals(e1, e2));
    }

    @Test
    public final void testElementNotEqualIfAttrsAreDifferent() {
        Element e1 = new Element("n1", createTestNS());
        Element e2 = new Element("n1", createTestNS());
        e1.setAttribute(new Attribute("a", "1"));
        e2.setAttribute(new Attribute("a", "2"));
        assertFalse(cmp.equals(e1, e2));
    }

    @Test
    public final void testElemenEqualIfAttrsAndNSTheSame() {
        Element e1 = new Element("n1", createTestNS());
        Element e2 = new Element("n1", createTestNS());
        e1.setAttribute(new Attribute("a", "1"));
        e2.setAttribute(new Attribute("a", "1"));
        assertTrue(cmp.equals(e1, e2));
    }

    @Test
    public final void testElementNotEqualIfContentDiffers() {
        Element e1 = new Element("n1", createTestNS());
        Element e2 = new Element("n1", createTestNS());
        e1.setAttribute(new Attribute("a", "1"));
        e2.setAttribute(new Attribute("a", "1"));
        e1.setText("text");
        e2.setText("text2");
        assertFalse(cmp.equals(e1, e2));

    }

}
