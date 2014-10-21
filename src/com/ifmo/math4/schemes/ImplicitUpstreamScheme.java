package com.ifmo.math4.schemes;

/**
 * Created by warrior on 13.10.14.
 */
public class ImplicitUpstreamScheme extends AbstractImplicitScheme {

    public ImplicitUpstreamScheme(double velocity, double kappa, double dx, double dt, double[] initialValues) {
        super(velocity, kappa, dx, dt, initialValues);
    }

    @Override
    protected double[][] getSystemOfCoefficients() {
        double[][] result = new double[nodeNumber][4];
        for (int i = 0; i < nodeNumber; i++) {
            result[i][0] = -r;
            result[i][1] = 1 - s + 2 * r;
            result[i][2] = s - r;
            result[i][3] = previousTimeLayer[i];
        }
        result[0][3] += r * leftPoint;
        result[nodeNumber - 1][3] -= (s - r) * rightPoint;
        return result;
    }

}
