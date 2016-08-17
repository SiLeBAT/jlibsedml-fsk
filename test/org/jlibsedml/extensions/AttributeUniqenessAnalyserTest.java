package org.jlibsedml.extensions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AttributeUniqenessAnalyserTest {
	AttributeUniqenessAnalyser ana;
	@Before
	public void setUp() throws Exception {
		ana = new AttributeUniqenessAnalyser();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetMostUniqueAttributeBasic() throws JDOMException, IOException {
		String str = "<a> <b name=\"x\"/> <b name=\"x\"/> <b id=\"y\"/> </a>";
		Document doc = TestUtils.getDoc(str);
		Element b3 = (Element)doc.getRootElement().getChildren("b").get(2);
		assertEquals("id",ana.getUniqueAttributeForElement(b3).name);	
	}
	
	@Test
	public final void testGetMostUniqueAttributeWithNS() throws JDOMException, IOException {
		String str = "<a xmlns:ns=\"mysns/ns\"> <ns:b name=\"x\"/> <ns:b name=\"x\"/> <ns:b id=\"y\"/> </a>";
		Document doc = TestUtils.getDoc(str);
		Element b3 = (Element)doc.getRootElement().getChildren("b",Namespace.getNamespace("ns", "mysns/ns")).get(2);
		assertEquals("id",ana.getUniqueAttributeForElement(b3).name);	
		assertEquals(3,ana.getIndexForElementAmongstSiblings(b3));	
	}
	
	@Test
	public final void testGetMostUniqueAttributeMustReturnAttForElement() throws JDOMException, IOException {
		
		String str = "<a> " +
				"		<b id=\"y\" name=\"z\"/>" +
				"		 <b id=\"y\"/> " +
				"		<b id=\"y\"/> " +
				"	</a>";
		Document doc = TestUtils.getDoc(str);
		Element b3 = (Element)doc.getRootElement().getChildren("b").get(2);
		// The id-value pair is not unique (name=z is) so null is returned.
		assertNull(ana.getUniqueAttributeForElement(b3));
		
		// but for this element, it is a unique identifier.
		Element b1 = (Element)doc.getRootElement().getChildren("b").get(0);
		assertEquals("name",ana.getUniqueAttributeForElement(b1).name);
	}
	
	@Test
	public final void testNoAttribute() throws JDOMException, IOException {
		String str = "<a> <b id=\"y\" name=\"z\"/> <b id=\"y\"/> <b>content</b> </a>";
		Document doc = TestUtils.getDoc(str);
		Element b3 = (Element)doc.getRootElement().getChildren("b").get(2);
		// even though the id value is not unique (name=z is)  it is not used as the element we're interested
		// in does not have an 'id' value.
		assertNull("id",ana.getUniqueAttributeForElement(b3));
		assertEquals(3,ana.getIndexForElementAmongstSiblings(b3));
		
		// but for this element, it is a unique identifier.
		Element b1 = (Element)doc.getRootElement().getChildren("b").get(0);
		assertEquals("name",ana.getUniqueAttributeForElement(b1).name);
		assertEquals(1,ana.getIndexForElementAmongstSiblings(b1));
	}
	
	@Test
	public final void testGetIndexIsElementNSAware() throws JDOMException, IOException {
		String str = "<a xmlns:ns=\"mysns/ns\" xmlns:ns2=\"mysns/ns2\">" +
				"		 <ns:b name=\"x\"/>" +
				"		 <ns2:b name=\"z\"/>" +
				"		 <ns:b id=\"y\"/>" + // this is the 2nd ns: element.
				"		 </a>";
		Document doc = TestUtils.getDoc(str);
		
		Element b3 = (Element)doc.getRootElement().getChildren("b",Namespace.getNamespace("ns", "mysns/ns")).get(1);
		
		assertEquals("id",ana.getUniqueAttributeForElement(b3).name);
		assertEquals(2,ana.getIndexForElementAmongstSiblings(b3));
		
	
	}
	@Test
	public final void testGetAttIsAttNSAware() throws JDOMException, IOException {
		String str = "<a xmlns:ns=\"mysns/ns\" xmlns:ns2=\"mysns/ns2\">" +
				"		 <b name=\"x\"/>" +
				"		 <b name=\"x\"/>" +
				"		 <b ns:name=\"x\"/>" + // this is the 2nd ns: element.
				"		 </a>";
		Document doc = TestUtils.getDoc(str);
		
		Element b3 = (Element)doc.getRootElement().getChildren("b").get(2);
		
		assertEquals("name",ana.getUniqueAttributeForElement(b3).name);
		assertEquals("ns",ana.getUniqueAttributeForElement(b3).ns.getPrefix());
		assertEquals(3,ana.getIndexForElementAmongstSiblings(b3)); // this is 3rd element
		
	
	}

}
