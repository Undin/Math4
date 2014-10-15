package com.ifmo.math4.schemes;

import java.util.function.Function;

/**
 * Created by Whiplash on 15.10.2014.
 */
public abstract class AbstractImplicitScheme extends AbstractScheme {

    public AbstractImplicitScheme(double velocity, double kappa, double dx, double dt, double[] initialValues, Function<Double, Double> leftPoint, Function<Double, Double> rightPoint) {
        super(velocity, kappa, dx, dt, initialValues, leftPoint, rightPoint);
    }

    @Override
    protected double[] nextTimeLayerInternal() {
        double[][] coefficients = getSystemOfCoefficients();
        if (coefficients != null && coefficients.length == nodeNumber && coefficients[0].length == 4) {
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

    protected abstract double[][] getSystemOfCoefficients();
    
}
