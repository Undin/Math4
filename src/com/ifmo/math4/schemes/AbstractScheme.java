package com.ifmo.math4.schemes;

import java.util.function.Function;

/**
 * Created by warrior on 13.10.14.
 */
public abstract class AbstractScheme {

    protected final double velocity;
    protected final double kappa;
    protected final double dx;
    protected final double dt;
    protected final Function<Double, Double> leftPoint;
    protected final Function<Double, Double> rightPoint;

    protected double[] previousTimeLayer;

    public AbstractScheme(double velocity, double kappa, double dx, double dt, double[] initialValues, Function<Double, Double> leftPoint, Function<Double, Double> rightPoint) {
        this.velocity = velocity;
        this.kappa = kappa;
        this.dx = dx;
        this.dt = dt;
        this.previousTimeLayer = initialValues;
        this.leftPoint = leftPoint;
        this.rightPoint = rightPoint;
    }

    public abstract double[] nextTimeLayer();
}
