package org.jlibsedml.testcases;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import org.jlibsedml.Libsedml;
import org.jlibsedml.SEDMLDocument;
import org.jlibsedml.SedMLError;
import org.jlibsedml.XMLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class SBMLTCTEst {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    // Failing due to proxy
//    @Ignore
//    // only run this when a new test suite is released.
//    @Test
//    public void testAllTC() throws XMLException {
//        File root = new File("SBML_test_cases");
//        File[] dirs = root.listFiles();
//        for (File child : dirs) {
//            if (child.isDirectory()) {
//                String[] sed = child.list(new FilenameFilter() {
//
//                    public boolean accept(File dir, String name) {
//                        return name.contains("sedml");
//                    }
//                });
//                for (String s : sed) {
//                    File sedFile = new File(child + File.separator + s);
//
//                    SEDMLDocument doc = Libsedml.readDocument(sedFile);
//                    List<SedMLError> errs = doc.validate();
//                    for (SedMLError err : errs) {
//                     //   System.err.println(err.getMessage());
//                    }
//                }
//            }
//        }
//    }
}
