package org.jlibsedml.extensions;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.JDOMException;
/**
 * Helper code for XML boilerplate code for tests
 * @author richard
 */
public class TestUtils {
    /**
     * Generates an org.jdom.Document from an XML string
     * @param in
     * @return
     * @throws JDOMException
     * @throws IOException
     */
	static Document getDoc(String in) throws JDOMException, IOException {
		return new XMLUtils().readDoc(new ByteArrayInputStream(in.getBytes()));
	}

}
