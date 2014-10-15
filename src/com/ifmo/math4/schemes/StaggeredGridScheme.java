package com.ifmo.math4.schemes;

import java.util.function.Function;

/**
 * Created by Whiplash on 15.10.2014.
 */
public class StaggeredGridScheme extends AbstractScheme {

    public StaggeredGridScheme(double velocity, double kappa, double dx, double dt, double[] initialValues, Function<Double, Double> leftPoint, Function<Double, Double> rightPoint) {
        super(velocity, kappa, dx, dt, initialValues, leftPoint, rightPoint);
    }

    @Override
    protected double[] nextTimeLayerInternal() {
        return null;
    }
}