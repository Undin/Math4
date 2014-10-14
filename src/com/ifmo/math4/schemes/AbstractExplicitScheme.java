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
        double[] nextLayer = new double[nodeNumber];
        for (int i = 0; i < nodeNumber; i++) {
            double time = dt * currentTimeLayerNumber;
            if (i == 0) {
                nextLayer[i] = leftPoint.apply(time);
            } else if (i == nodeNumber - 1) {
                nextLayer[i] = rightPoint.apply(time);
            } else {
                nextLayer[i] = calculateInnerNodeValue(i);
            }
        }
        return nextLayer;
    }

    protected abstract double calculateInnerNodeValue(int i);
}
