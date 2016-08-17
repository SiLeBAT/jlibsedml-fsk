package org.jlibsedml.acceptanceTests;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jmathml.ASTNode;
import org.jmathml.EvaluationContext;

public class ODESystem extends RK4thOrder {
    private ASTNode[] funcs;
    private Map<Integer, String> arrayIndex2Name;
    Map<String, Iterable<Double>> con = new HashMap<String, Iterable<Double>>();

    /**
     * @param vars
     * @param h
     * @param funcs
     * @param arrayIndex2Name
     * @param vars2
     */
    public ODESystem(double[] vars, double h, ASTNode[] funcs,
            Map<Integer, String> arrayIndex2Name,
            Map<String, Iterable<Double>> vars2) {
        super(vars, h);
        this.funcs = funcs;
        this.arrayIndex2Name = arrayIndex2Name;
        if (vars2 != null)
            this.con = vars2;
        // TODO Auto-generated constructor stub
    }

    @Override
    public double[] diffEquns(double[] y) {
        EvaluationContext context;
        for (int i = 0; i < y.length; i++) {
            con.put(arrayIndex2Name.get(i), Arrays.asList(y[i]));
        }
        double[] f = new double[y.length];

        for (int i = 0; i < y.length; i++) {
            context = new EvaluationContext(con);
            if (funcs[i] != null) {
                f[i] = funcs[i].evaluate(context).getValue();
            }
        }
        return f;
    }
}
