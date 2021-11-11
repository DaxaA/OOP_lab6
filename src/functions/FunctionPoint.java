package functions;

import java.io.Serializable;

public class FunctionPoint implements Serializable {
    private double x;
    private double y;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double tempX) {
        x = tempX;
    }

    public void setY(double tempY) {
        y = tempY;
    }

    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public FunctionPoint(FunctionPoint point) {
        this.x = point.x;
        this.y = point.y;
    }

    public FunctionPoint() {
        this.x = 0.0;
        this.y = 0.0;
    }

    @Override
    public int hashCode() {
        int result = 1;
        long bitsX = Double.doubleToLongBits(x);
        long bitsY = Double.doubleToLongBits(y);
        result = 31 * result + (int) (bitsX ^ (bitsX >>> 32));
        result = 31 * result + (int) (bitsY ^ (bitsY >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj.hashCode() != this.hashCode()) return false;
        if (obj instanceof FunctionPoint && obj.hashCode() == this.hashCode()) {
            return (((FunctionPoint) obj).x == x) && ((FunctionPoint) obj).y == y;
        }
        return false;
    }

    @Override
    protected Object clone() {
        return new FunctionPoint(this);
    }

    @Override
    public String toString() {
        return ("(" + x + ", " + y + ")");
    }
}