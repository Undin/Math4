package com.ifmo.math4.schemes;

/**
 * Created by Whiplash on 15.10.2014.
 */
public class StaggeredGridScheme extends AbstractScheme {
    private double[] beforePrevTimeLayer;

    public StaggeredGridScheme(double velocity,
                               double kappa,
                               double dx,
                               double dt,
                               double[] firstLayer,
                               double[] secondLayer) {
        super(velocity, kappa, dx, dt, secondLayer);
        this.beforePrevTimeLayer = firstLayer;
    }

    @Override
    protected double[] nextTimeLayerInternal() {
        double[] nextLayer = new double[nodeNumber];
        nextLayer[0] = leftPoint;
        nextLayer[nodeNumber - 1] = rightPoint;
        for (int i = 1; i < nodeNumber - 1; i++) {
            nextLayer[i] =
                    (beforePrevTimeLayer[i]
                    - s * (previousTimeLayer[i + 1] - previousTimeLayer[i - 1])
                    + 2 * r * (previousTimeLayer[i + 1] + previousTimeLayer[i - 1] -  beforePrevTimeLayer[i])) /
                            (1 + 2 * r);
        }
        beforePrevTimeLayer = previousTimeLayer;
        previousTimeLayer = nextLayer;
        return nextLayer;
    }
}
