package com.ifmo.math4.schemes;

/**
 * Created by Whiplash on 15.10.2014.
 */
public class StaggeredGridScheme extends AbstractScheme {

    public StaggeredGridScheme(double velocity, double kappa, double dx, double dt, double[] initialValues) {
        super(velocity, kappa, dx, dt, initialValues);
    }

    @Override
    protected double[] nextTimeLayerInternal() {
        return null;
    }
}
