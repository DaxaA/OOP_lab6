package functions;

import java.io.Serializable;

public class ArrayTabulatedFunction implements TabulatedFunction, Serializable {
    private FunctionPoint[] pM;
    private int pC;

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException {
        if (leftX >= rightX || pointsCount < 2) {
            throw new IllegalArgumentException();
        }
        pM = new FunctionPoint[pointsCount + pointsCount / 2];
        pM[0] = new FunctionPoint(leftX, 0);
        for (int i = 1; i < pointsCount; i++) {
            pM[i] = new FunctionPoint(pM[i - 1].getX() + (rightX - leftX) / (pointsCount - 1), 0);
        }
        pC = pointsCount;
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX || values.length < 3) {
            throw new IllegalArgumentException();
        }
        pM = new FunctionPoint[values.length + values.length / 2];
        pM[0] = new FunctionPoint(leftX, values[0]);
        for (int i = 1; i < values.length; i++) {
            pM[i] = new FunctionPoint(pM[i - 1].getX() + (rightX - leftX) / (values.length - 1), values[i]);
        }
        pC = values.length;
    }

    public ArrayTabulatedFunction(FunctionPoint[] mass) throws IllegalArgumentException {
        if (mass.length < 2) {
            throw new IllegalArgumentException();
        }
        pC = mass.length;
        for (int i = 1; i < mass.length; i++) {
            if (mass[i - 1].getX() >= mass[i].getX()) {
                throw new IllegalArgumentException();
            }
        }
        pM = new FunctionPoint[mass.length + mass.length / 2];
        System.arraycopy(mass, 0, pM, 0, mass.length);
    }

    @Override
    public double getLeftDomainBorder() {
        return this.pM[0].getX();
    }

    @Override
    public double getRightDomainBorder() {
        return this.pM[pC - 1].getX();
    }

    @Override
    public double getFunctionValue(double x) {
        if (x >= getLeftDomainBorder() && x <= getRightDomainBorder()) {
            int first = 0;
            int last = pC;
            int mid = first + (last - first) / 2;
            if (pM.length == 0) { //если точек нет
                return Double.NaN;
            }
            if (x < pM[0].getX()) { //если x выходит за правую границу
                return Double.NaN;
            }
            if (pM[pC - 1].getX() < x) { //если x левее левой границы
                return Double.NaN;
            }
            while (first < last) { //бинарный поиск
                if (x <= pM[mid].getX()) { //если x меньше x средней точки
                    last = mid; //значит отбрасываем вторую часть после середины
                } else {
                    first = mid + 1; //отбрасываем первую часть до середины
                }
                mid = first + (last - first) / 2; //устанавливаем середину в оставшейся части(новой области)
            }
            if (x == pM[last].getX()) { //если нашли точку с таким же x, то возвращаем значение фукнции
                return pM[last].getY();
            } else { //уравнение прямой: y=kx+b
                double k = (pM[last].getY() - pM[last - 1].getY()) / (pM[last].getX() - pM[last - 1].getX()); //k = (y1-y2)/(x1-x2)
                double b = pM[last].getY() - k * pM[last].getX(); //b = y-kx
                //System.out.println("k = " + k + ", b = " + b);
                return k * x + b;
            }
        } else {
            return Double.NaN;
        }
    }

    @Override
    public int getPointsCount() {
        return pC;
    }

    @Override
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index > pC) {
            throw new FunctionPointIndexOutOfBoundsException();
        } else {
            return pM[index];
        }
    }

    @Override
    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 0 || index > pC) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        if (index == 0 && point.getX() < pM[1].getX()) {
            pM[index] = point;
        } else if (index == pC - 1 && point.getX() > pM[pC - 2].getX()) {
            pM[index] = point;
        } else {
            if (point.getX() < pM[index - 1].getX() || point.getX() > pM[index + 1].getX()) {
                throw new InappropriateFunctionPointException();
            } else {
                pM[index] = point;
            }
        }
    }

    @Override
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index > pC) {
            throw new FunctionPointIndexOutOfBoundsException();
        } else {
            return pM[index].getX();
        }
    }

    @Override
    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 0 || index > pC) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        if ((index == 0 && x < pM[1].getX()) || (index == pC - 1 && x > pM[pC - 2].getX())) {
            pM[index].setX(x);
        } else {
            if (x < pM[index - 1].getX() || x > pM[index + 1].getX()) {
                throw new InappropriateFunctionPointException();
            } else {
                pM[index].setX(x);
            }
        }
    }

    @Override
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index > pC) {
            throw new FunctionPointIndexOutOfBoundsException();
        } else {
            return pM[index].getY();
        }
    }

    @Override
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index > pC) {
            throw new FunctionPointIndexOutOfBoundsException();
        } else {
            pM[index].setY(y);
        }
    }

    @Override
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException {
        if (pC < 3) {
            throw new IllegalStateException();
        } else {
            if (index < 0 || index > pC) {
                throw new FunctionPointIndexOutOfBoundsException();
            } else {
                System.arraycopy(pM, index + 1, pM, index, getPointsCount() - index - 1);
                --pC;
            }
        }
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        if(point.getX() <= getLeftDomainBorder() || point.getX() >= getRightDomainBorder()){
            throw new InappropriateFunctionPointException();
        }
        int first = 0;
        int last = getPointsCount();
        int mid = first + (last - first) / 2;
        while (first < last) { //бинарный поиск
            if (point.getX() == pM[mid].getX()) throw new InappropriateFunctionPointException();
            if (point.getX() < pM[mid].getX()) { //если x меньше x средней точки
                last = mid; //значит отбрасываем вторую часть после середины
            } else {
                first = mid + 1; //отбрасываем первую часть до середины
            }
            mid = first + (last - first) / 2; //устанавливаем середину в оставшейся части(новой области)
        }
        if (pC >= pM.length) {
            FunctionPoint[] pMNew = new FunctionPoint[pC + pC / 2];
            System.arraycopy(pM, 0, pMNew, 0, pC);
            pM = pMNew;
        }
        System.arraycopy(pM, last, pM, last + 1, getPointsCount() - last);
        pM[last] = point;
        ++pC;
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (FunctionPoint poi : pM)
            result += (poi == null ? 0 : poi.hashCode());
        return result + pC;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof TabulatedFunction tabulatedFunction) {
            if (obj instanceof ArrayTabulatedFunction arrayTabulatedFunction) {
                if (arrayTabulatedFunction.pC != pC) {
                    return false;
                }
                for (int i = 0; i < pC; i++) {
                    if (pM[i].getX() != (arrayTabulatedFunction.pM[i].getX())) {
                        return false;
                    }
                    if (pM[i].getY() != (arrayTabulatedFunction.pM[i].getY())) {
                        return false;
                    }
                }
            } else {
                if (tabulatedFunction.getPointsCount() != pC) {
                    return false;
                }
                for (int i = 0; i < pC; i++) {
                    if (!(this.getPoint(i).equals(tabulatedFunction.getPoint(i)))) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public Object clone() {
        FunctionPoint[] newPm = new FunctionPoint[pC];
        for (int i = 0; i < pC; i++) {
            newPm[i] = (FunctionPoint) pM[i].clone();
        }
        return new ArrayTabulatedFunction(newPm);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pC; i++) {
            sb.append(pM[i].toString()).append(", ");
        }
        sb.deleteCharAt(sb.length() - 1).deleteCharAt(sb.length() - 1);
        return ("{" + sb + "}");
    }
}