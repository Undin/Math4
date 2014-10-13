package com.ifmo.math4.schemes;

import java.util.function.Function;

/**
 * Created by warrior on 14.10.14.
 */
public abstract class AbstractExplicitScheme extends AbstractScheme {

    public AbstractExplicitScheme(double velocity, double kappa, double dx, double dt, double[] initialValues, Function<Double, Double> leftPoint, Function<Double, Double> rightPoint) {
        super(velocity, kappa, dx, dt, initialValues, leftPoint, rightPoint);
    }

    @Override
    protected double[] nextTimeLayerInternal() {
        double[] nextLayer = new double[previousTimeLayer.length];
        for (int i = 0; i < nextLayer.length - 1; i++) {
            nextLayer[i] = calculateNodeValue(i);
        }
        return nextLayer;
    }

    protected abstract double calculateNodeValue(int i);
}
