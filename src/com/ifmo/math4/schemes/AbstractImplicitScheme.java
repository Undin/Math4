package com.ifmo.math4.schemes;

/**
 * Created by Whiplash on 15.10.2014.
 */
public abstract class AbstractImplicitScheme extends AbstractScheme {

    public AbstractImplicitScheme(double velocity, double kappa, double dx, double dt, double[] initialValues) {
        super(velocity, kappa, dx, dt, initialValues);
    }

    @Override
    protected double[] nextTimeLayerInternal() {
        double[][] coefficients = getSystemOfCoefficients();
        if (coefficients == null || coefficients.length != nodeNumber || coefficients[0].length != 4) {
            throw new IllegalStateException("getSystemOfCoefficients return incorrect array");
        }

        return solveSystem(coefficients);
    }

    private double[] solveSystem(double[][] m) {
        double[] x = new double[nodeNumber];
        double[] alpha = new double[nodeNumber];
        double[] beta = new double[nodeNumber];
        alpha[0] = -m[0][2] / m[0][1];
        beta[0] = m[0][3] / m[0][1];
        for (int i = 1; i < nodeNumber; i++) {
            alpha[i] = -m[i][2] / (m[i][0] * alpha[i - 1] + m[i][1]);
            beta[i] = (m[i][3] - m[i][0] * beta[i - 1]) / (m[i][0] * alpha[i - 1] + m[i][1]);
        }
        x[nodeNumber - 1] = (m[nodeNumber - 1][3] - m[nodeNumber - 1][0] * beta[nodeNumber - 2]) / (m[nodeNumber - 1][0] * alpha[nodeNumber - 2] + m[nodeNumber - 1][1]);
        for (int i = nodeNumber - 2; i >= 0; i--) {
            x[i] = alpha[i] * x[i + 1] + beta[i];
        }
        return x;
    }

    /**
     * This method should return a system of coefficients in the following format:
     * double[nodeNumber][4]
     * We need only 4 coefficients on every row because the system is 3-diagonal
     * First 3 values of result[i] should contain (i - 1), i and (i + 1) coefficients of the row
     * and the 4th - a constant member.
     * result[0][0] and result[nodeNumber - 1][2] are considered absent and ignored.
     *
     * @return array of coefficients for a system of equations needed to calculate the next step of the algorithm.
     */
    protected abstract double[][] getSystemOfCoefficients();
    
}
