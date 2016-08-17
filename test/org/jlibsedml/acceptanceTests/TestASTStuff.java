package org.jlibsedml.acceptanceTests;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jlibsedml.Libsedml;
import org.jmathml.ASTNode;
import org.jmathml.EvaluationContext;
import org.jmathml.IEvaluationContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestASTStuff {
    Logger log = LoggerFactory.getLogger(TestASTStuff.class);

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSquare() {
        euler(createTwoX(), createIntegralTwoX(), 3, 0.01, 0);
    }

    @Test
    public void testPrecedence() {
        assertEquals(6, 3d / 2d * 4d, 0.001);
    }

    @Test
    public void testSin() {
        euler(createDerivative(), getCosIntegral(), 3, 0.01, 1);
    }

    @Test
    public void testGrowth() {
        improvedEuler(getgrowth(), null, 10, 0.5, 1000);
    }

    private ASTNode createDerivative() {
        ASTNode node = Libsedml.parseFormulaString("- sin(t)");
        return node;
    }

    private ASTNode createTwoX() {
        return Libsedml.parseFormulaString("t * 2");
    }

    private ASTNode createIntegralTwoX() {
        return Libsedml.parseFormulaString("t * t");
    }

    public double euler(ASTNode f, ASTNode exactFunc, double end, double h,
            double initVal) {
        Map<String, Iterable<Double>> con = new HashMap<String, Iterable<Double>>();

        int numSteps = (int) (end / h);
        double currx = 0;

        double exact = 0.0;
        double y = initVal;
        StringBuffer sb  = new StringBuffer();
        for (double d = 0; d < numSteps; d++) {
            currx += h;
            con.put("t", Arrays.asList(currx));
            con.put("y", Arrays.asList(y));
            IEvaluationContext context = new EvaluationContext(con);
            // exact =exactFunc.evaluate(context).getValue();
            sb.append("x = " + currx + ", exact: " + exact + "\n");

            y = y + f.evaluate(context).getValue() * h;
            sb.append("calc: " + y + ", % diff = "
                    + Math.abs((y - exact) / y) * 100 + "\n");
        }
        log.info(sb.toString());
        return y;
    }

    public double improvedEuler(ASTNode f, ASTNode exactFunc, double end,
            double h, double initVal) {
        Map<String, Iterable<Double>> con = new HashMap<String, Iterable<Double>>();
        int numSteps = (int) (end / h);
        double currx = 0;

        double exact = 0.0;
        double y = initVal;
        double y_euler = 0.0;
        StringBuffer sb  = new StringBuffer();
        for (double d = 0; d < numSteps; d++) {
            double y_old = y;
            con.put("t", Arrays.asList(currx));
            con.put("y", Arrays.asList(y));
            IEvaluationContext context = new EvaluationContext(con);
            // exact =exactFunc.evaluate(context).getValue();
            sb.append("x = " + currx + ", exact: " + exact +"\n");
            y_euler = y + f.evaluate(context).getValue() * h;
            currx += h;
            con.put("t", Arrays.asList(currx));
            con.put("y", Arrays.asList(y_euler));
            context = new EvaluationContext(con);
            double p1 = f.evaluate(context).getValue();
            sb.append("p1: " + p1 +"\n");
            con.put("t", Arrays.asList(currx - h));
            con.put("y", Arrays.asList(y_old));
            context = new EvaluationContext(con);
            double p2 = f.evaluate(context).getValue();
            sb.append("p2: " + p2+"\n");

            y = y + (h * (p1 + p2) / 2);
            sb.append("calc at: " + currx + "=" + y + ", % diff = "
                    + Math.abs((y - exact) / y) * 100 +"\n");
        }
        log.info(sb.toString());
        return y;
    }

    ASTNode getgrowth() {
        return Libsedml.parseFormulaString("0.8 * y");
    }

    ASTNode getCosIntegral() {
        return Libsedml.parseFormulaString("cos(t)");
    }
}
