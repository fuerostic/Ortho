package com.example.ortho;

public class PointValue {

    double xValue;
    double yValue;

    public PointValue(double xValue, double yValue) {
        this.xValue = xValue;
        this.yValue = yValue;
    }

    public PointValue() {
    }

    public double getxValue() {
        return xValue;
    }

    public void setxValue(double xValue) {
        this.xValue = xValue;
    }

    public double getyValue() {
        return yValue;
    }

    public void setyValue(double yValue) {
        this.yValue = yValue;
    }
}
