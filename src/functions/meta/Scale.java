package functions.meta;

import functions.Function;

public class Scale implements Function {
    private Function f;
    private double scaleX;
    private double scaleY;

    public Scale(Function f, double scaleX, double scaleY) {
        this.f = f;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public double getLeftDomainBorder() {
        if (scaleX >= 0) {
            return scaleX * f.getLeftDomainBorder();
        } else {
            return f.getLeftDomainBorder() / Math.abs(scaleX);
        }
    }

    @Override
    public double getRightDomainBorder() {
        if (scaleX >= 0) {
            return scaleX * f.getRightDomainBorder();
        } else {
            return f.getRightDomainBorder() / Math.abs(scaleX);
        }
    }

    @Override
    public double getFunctionValue(double x) {
        if (scaleY >= 0) {
            return scaleY * f.getFunctionValue(x);
        } else {
            return f.getFunctionValue(x) / Math.abs(scaleY);
        }
    }
}
