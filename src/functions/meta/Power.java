package functions.meta;

import functions.Function;

public class Power implements Function {
    private Function f;
    private double p;

    public Power(Function f, double p) {
        this.f = f;
        this.p = p;
    }

    @Override
    public double getLeftDomainBorder() {
        return f.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder() {
        return f.getRightDomainBorder();
    }

    @Override
    public double getFunctionValue(double x) {
        return Math.pow(f.getFunctionValue(x), p);
    }
}
