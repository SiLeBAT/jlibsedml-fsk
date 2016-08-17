package org.jlibsedml;


import java.io.StringReader;
import java.util.Iterator;

import org.jdom.Document;
import org.jdom.contrib.input.LineNumberElement;
import org.jdom.contrib.input.LineNumberSAXBuilder;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SAXParserTest {

    Logger log = LoggerFactory.getLogger(SAXParserTest.class);
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testSaxParser() throws Exception {
		
		SAXBuilder builder = new LineNumberSAXBuilder();
		Document doc = builder.build(new StringReader(xml));

		for (Iterator iter = doc.getDescendants(new ElementFilter());
         iter.hasNext(); ) {
			LineNumberElement e = (LineNumberElement) iter.next();
			log.info(
				e.getName() + ": lines " + e.getStartLine() + " to " + e.getEndLine());
		}
	}
	private static String xml =
		"<a>\n<b/>\n<c/>\n<d>\n<e/>\n<f/>\n</d>\n</a>\n";

}
