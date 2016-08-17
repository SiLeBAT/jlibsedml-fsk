package org.jlibsedml.acceptanceTests;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.jlibsedml.Libsedml;
import org.jmathml.ASTNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class RK4thOrder {

    Logger log = LoggerFactory.getLogger(RK4thOrder.class);
    private int numVars; // number of variables
    protected int order; // number of DEs
    private double dt; // step-length
    protected double[] y; // variable list
    private double[][] results;

    /*
     * Constructor gives variables their values at initial time. The number of
     * variables is deduced from the size of the array passed to the
     * constructor. The step length is also set
     */
    public RK4thOrder(double[] vars, double h) {
        numVars = vars.length;
        y = new double[numVars];
        for (int i = 0; i < numVars; i++)
            y[i] = vars[i];
        dt = h; // set the step length
    }

    /*
     * Abstract method. In the concrete subclass, this method will return an
     * array of the values of RHSs of the differential equations given an array
     * of variable values.
     */
    public abstract double[] diffEquns(double[] y);

    /*
     * Fourth-order Runge-Kutta-Merson fornumerical integration of DEs
     */
    private void integrate() {
        double a[] = new double[order];
        double b[] = new double[order];
        double c[] = new double[order];
        double d[] = new double[order];
        double x[] = new double[order];
        double f[]; // RHS�s of the DEs
        for (int i = 0; i < order; i++) {
            x[i] = y[i];
        }
        f = diffEquns(y);
        for (int i = 0; i < order; i++) {
            a[i] = dt * f[i];
            y[i] = x[i] + a[i] / 2;
        }
        f = diffEquns(y);
        for (int i = 0; i < order; i++) {
            b[i] = dt * f[i];
            y[i] = x[i] + b[i] / 2;
        }
        f = diffEquns(y);
        for (int i = 0; i < order; i++) {
            c[i] = dt * f[i];
            y[i] = x[i] + c[i];
        }
        f = diffEquns(y);
        for (int i = 0; i < order; i++) {
            d[i] = dt * f[i];
            y[i] = x[i] + (a[i] + 2 * b[i] + 2 * c[i] + d[i]) / 6;
        }

    }

    /**
     * 
     * @param a
     *            start
     * @param b
     *            end
     * @param opInt
     *            output interval
     */
    public void solve(double a, double b, double opInt) {
        order = numVars; // in general, order <= numVars
        int m = (int) Math.floor((b - a) / dt); // number of integration steps
        results = new double[m][order + 1];
        double t = a; // time
        if (Math.abs(Math.IEEEremainder(opInt, dt)) > 1e-6) {
            log.warn("Warning: opInt not a multiple of h!");
        }
        StringBuffer sb = new StringBuffer();
        // display heading:
        sb.append("time\n");
        // display initial values:
        DecimalFormat df = new DecimalFormat("##.##");
        sb.append(df.format(t) + "\t");
        for (int i = 0; i < numVars; i++) {
            sb.append(" " + y[i]);
        }
        sb.append("\n");
        int resIndex = 0;
        // integrate over m steps
        for (int i = 0; i < m; i++) {
            integrate(); // perform Runge-Kutta over one step
            for (int j = 0; j < numVars; j++) {

            }
            t += dt;
            results[resIndex][0] = t;
            sb.append(df.format(t) + "\t");
            for (int j = 0; j < numVars; j++) {
                sb.append(" " + y[j]);
                results[resIndex][j + 1] = y[j];
            }
            sb.append("\n");
            resIndex++;
        }
        log.info(sb.toString());
    }

    public double[][] getResults() {
        return results;
    }

    public static void main(String args[]) {
        double[] vars = { 105, 8 }; // initial values

        final ASTNode eq1 = Libsedml
                .parseFormulaString("0.4 * x - 0.04 * x * y");
        final ASTNode eq2 = Libsedml.parseFormulaString("0.02 * x * y - 2 * y");

        // look up for varnames in equations with index of y
        final Map<Integer, String> arrayIndex2Name = new HashMap<Integer, String>();
        arrayIndex2Name.put(0, "x");
        arrayIndex2Name.put(1, "y");
        final ASTNode[] eqns = new ASTNode[] { eq1, eq2 };

        final Map<String, Double> con = new HashMap<String, Double>();
        ODESystem model = new ODESystem(vars, 0.1, eqns, arrayIndex2Name, null);

        model.solve(0, 20, 1);
    }
}
