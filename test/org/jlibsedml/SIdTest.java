package org.jlibsedml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SIdTest {

    String[] validIDs = new String[] { "_", "_abZx_3xC", "abZx_3xC" };
    String[] invalidIDs = new String[] { "", null, "1abcaaaAA", "-test123",
            "abca/aaAA" };

    @Test
    public void testValidIds() {
        for (String valid : invalidIDs) {
            assertFalse(SId.isValidSId(valid));
        }
        for (String valid : validIDs) {
            assertTrue(SId.isValidSId(valid));
        }
    }

}
