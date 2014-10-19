package com.ifmo.math4.schemes;

/**
 * Created by warrior on 13.10.14.
 */
public class ExplicitDownstreamScheme extends AbstractExplicitScheme {

    public ExplicitDownstreamScheme(double velocity, double kappa, double dx, double dt, double[] initialValues) {
        super(velocity, kappa, dx, dt, initialValues);
    }

    @Override
    protected double calculateInnerNodeValue(int i) {
        return   r              * previousTimeLayer[i - 1] +
                (1 - 2 * r + s) * previousTimeLayer[i]     +
                (r - s)         * previousTimeLayer[i + 1];
    }
}
