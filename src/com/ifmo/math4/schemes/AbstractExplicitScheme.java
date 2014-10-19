package com.ifmo.math4.schemes;

/**
 * Created by warrior on 14.10.14.
 */
public abstract class AbstractExplicitScheme extends AbstractScheme {

    public AbstractExplicitScheme(double velocity, double kappa, double dx, double dt, double[] initialValues) {
        super(velocity, kappa, dx, dt, initialValues);
    }

    @Override
    protected double[] nextTimeLayerInternal() {
        double[] nextLayer = new double[nodeNumber];
        for (int i = 0; i < nodeNumber; i++) {
            if (i == 0) {
                nextLayer[i] = leftPoint;
            } else if (i == nodeNumber - 1) {
                nextLayer[i] = rightPoint;
            } else {
                nextLayer[i] = calculateInnerNodeValue(i);
            }
        }
        return nextLayer;
    }

    protected abstract double calculateInnerNodeValue(int i);
}
