package org.jlibsedml.modelsupport;

import java.io.IOException;
import java.net.MalformedURLException;

import org.jlibsedml.webtools.WebFile;

abstract class AbstractResourceRetriever {

    String getResource(final String url) {

        try {
            WebFile webResource = new WebFile(url);
            Object content = webResource.getContent();
            if (content instanceof String) {
                return stripNonValidXMLCharacters((String) content);
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }
    
     String stripNonValidXMLCharacters(String in) {
        StringBuffer out = new StringBuffer(); // Used to hold the output.
        char current; // Used to reference the current character.

        if (in == null || ("".equals(in))) return ""; // vacancy test.
        for (int i = 0; i < in.length(); i++) {
            current = in.charAt(i); // NOTE: No IndexOutOfBoundsException caught here; it should not happen.
            if ((current == 0x9) ||
                (current == 0xA) ||
                (current == 0xD) ||
                ((current >= 0x20) && (current <= 0xD7FF)) ||
                ((current >= 0xE000) && (current <= 0xFFFD)) ||
                ((current >= 0x10000) && (current <= 0x10FFFF)))
                out.append(current);
        }
        return out.toString();
    }    
}
