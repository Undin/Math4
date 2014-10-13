package com.ifmo.math4.schemes;

import java.util.function.Function;

/**
 * Created by warrior on 13.10.14.
 */
public class ImplicitDownstreamScheme extends AbstractScheme {

    public ImplicitDownstreamScheme(double velocity, double kappa, double dx, double dt, double[] initialValues, Function<Double, Double> leftPoint, Function<Double, Double> rightPoint) {
        super(velocity, kappa, dx, dt, initialValues, leftPoint, rightPoint);
    }

    @Override
    public double[] next() {
        return null;
    }
}
