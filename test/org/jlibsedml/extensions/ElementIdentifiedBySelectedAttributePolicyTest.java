package org.jlibsedml.extensions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.filter.ElementFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ElementIdentifiedBySelectedAttributePolicyTest {

	ISelectionChangedPolicy policy;
	Document doc;
	
	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetDescription() {
		policy = new ElementIdentifiedBySelectedAttributePolicy(null);
		assertTrue(policy.getDescription().length()>1);
	}

	@Test
	public final void testGetXPathForAttribute() throws JDOMException, IOException {
		String xmltoTest="<a><b name='c'/></a>";
		doc = getDoc(xmltoTest);
		policy = new ElementIdentifiedBySelectedAttributePolicy(doc);
		List els = doc.getRootElement().getContent(new ElementFilter());
		Attribute a = ((Element)els.get(0)).getAttribute("name");
		assertEquals("/a:a/a:b[@name='c']",policy.getXPathForAttribute(a));
	}

	Document getDoc(String in) throws JDOMException, IOException {
		return new XMLUtils().readDoc(new ByteArrayInputStream(in.getBytes()));
	}
	@Test
	public final void testGetXPathForElementReturnsXPathToElementIfNoAttributes() throws JDOMException, IOException {
		String xmltoTest="<a><b name='c'/></a>";
		doc = getDoc(xmltoTest);
		policy = new ElementIdentifiedBySelectedAttributePolicy(doc);
		assertEquals("/a:a",policy.getXPathForElement(doc.getRootElement()));
	}

}
