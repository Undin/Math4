package com.ifmo.math4.schemes;

/**
 * Created by warrior on 13.10.14.
 */
public class ImplicitUpstreamScheme extends AbstractImplicitScheme {

    public ImplicitUpstreamScheme(double velocity, double kappa, double dx, double dt, double[] initialValues) {
        super(velocity, kappa, dx, dt, initialValues);
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
