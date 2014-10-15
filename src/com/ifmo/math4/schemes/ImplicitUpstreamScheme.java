package com.ifmo.math4.schemes;

import java.util.function.Function;

/**
 * Created by warrior on 13.10.14.
 */
public class ImplicitUpstreamScheme extends AbstractImplicitScheme {

    public ImplicitUpstreamScheme(double velocity, double kappa, double dx, double dt, double[] initialValues, Function<Double, Double> leftPoint, Function<Double, Double> rightPoint) {
        super(velocity, kappa, dx, dt, initialValues, leftPoint, rightPoint);
    }

    @Override
    protected double[] nextTimeLayerInternal() {
        return null;
    }

    @Override
    protected double[][] getSystemOfCoefficients() {
        return new double[0][];
    }

}
