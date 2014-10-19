package com.ifmo.math4.schemes;

/**
 * Created by warrior on 13.10.14.
 */
public abstract class AbstractScheme {

    protected final double velocity;
    protected final double kappa;
    protected final double dx;
    protected final double dt;
    protected final double leftPoint;
    protected final double rightPoint;

    protected final int nodeNumber;

    protected final double s;
    protected final double r;

    protected double[] previousTimeLayer;

    protected int currentTimeLayerNumber = 0;

    public AbstractScheme(double velocity, double kappa, double dx, double dt, double[] initialValues) {
        this.velocity = velocity;
        this.kappa = kappa;
        this.dx = dx;
        this.dt = dt;
        this.previousTimeLayer = initialValues;
        this.nodeNumber = initialValues.length;
        this.leftPoint = initialValues[0];
        this.rightPoint = initialValues[initialValues.length - 1];

        s = velocity * dt / dx;
        r = kappa * dt / (dx * dx);
    }

    public double[] nextTimeLayer() {
        currentTimeLayerNumber++;
        previousTimeLayer = nextTimeLayerInternal();
        return previousTimeLayer;
    }

    protected abstract double[] nextTimeLayerInternal();
}
