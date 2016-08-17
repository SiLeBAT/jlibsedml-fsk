package org.jlibsedml.testcases;

import java.io.File;

import org.junit.Assume;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IgnoreIfSedMLDirNotSpecifiedRule implements MethodRule {

    private static Logger log = LoggerFactory.getLogger(IgnoreIfSedMLDirNotSpecifiedRule.class);

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        if (method.getMethod().isAnnotationPresent(IgnoreIfSedMLDirNotPresent.class)) {
            if (System.getProperty("sedMLDir") != null) {
                String rootDir = System.getProperty("sedMLDir");
                File dir = new File(rootDir);
                if (!dir.exists() || !dir.isDirectory() || !dir.canRead()) {
                    log.warn("sedMLDir property is set but is not a readable folder!");
                    return new IgnoreStatement();
                } else {
                    return base;
                }
            } else {
                return new IgnoreStatement();
            }
           
        } else {
            return base;
        }
    }
    
    private static class IgnoreStatement extends Statement {
        @Override
        public void evaluate() {
          log.info("ignoring test");
          Assume.assumeTrue( "Ignored", false );
        }
      }

}
