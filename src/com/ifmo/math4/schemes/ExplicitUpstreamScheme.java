package com.ifmo.math4.schemes;

import java.util.function.Function;

/**
 * Created by warrior on 13.10.14.
 */
public class ExplicitUpstreamScheme extends AbstractExplicitScheme {

    public ExplicitUpstreamScheme(double velocity, double kappa, double dx, double dt, double[] initialValues, Function<Double, Double> leftPoint, Function<Double, Double> rightPoint) {
        super(velocity, kappa, dx, dt, initialValues, leftPoint, rightPoint);
    }

    @Override
    protected double calculateInnerNodeValue(int i) {
        return (r + s)         * previousTimeLayer[i - 1] +
               (1 - 2 * r - s) * previousTimeLayer[i]     +
                r              * previousTimeLayer[i + 1];
    }
}
