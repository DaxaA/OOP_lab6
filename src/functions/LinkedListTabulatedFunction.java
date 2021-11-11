package functions;

import java.io.Serializable;

public class LinkedListTabulatedFunction implements TabulatedFunction, Serializable {
    private static class FunctionNode {
        private FunctionPoint point = null;
        private FunctionNode prev = null;
        private FunctionNode next = null;
    }

    private int length;
    private int currIndex;
    private FunctionNode mainHead = new FunctionNode();
    private FunctionNode head, tail, current;

    {
        mainHead.next = mainHead;
        mainHead.prev = mainHead;
        //head = mainHead;
        tail = mainHead;
        current = mainHead;
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX || pointsCount < 2) {
            throw new IllegalArgumentException();
        }
        length = pointsCount;
        head = new FunctionNode();
        mainHead.next = head;
        current = head;
        head.prev = mainHead;
        current.point = new FunctionPoint(leftX, 0);
        current.next = new FunctionNode();
        current.next.prev = current;
        current = current.next;
        currIndex++;
        for (int i = 1; i < length; i++) {
            current.point = new FunctionPoint(current.prev.point.getX() + (rightX - leftX) / (pointsCount - 1), 0);
            current.next = new FunctionNode();
            current.next.prev = current;
            current = current.next;
            currIndex++;
        }
        tail.prev = current;
        tail.next = mainHead;
        mainHead.prev = tail;
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        length = values.length;
        head = new FunctionNode();
        mainHead.next = head;
        current = head;
        current.point = new FunctionPoint(leftX, values[0]);
        current.next = new FunctionNode();
        head.prev = mainHead;
        current.next.prev = current;
        current = current.next;
        currIndex++;
        for (int i = 1; i < values.length; i++) {
            current.point = new FunctionPoint(current.prev.point.getX() + (rightX - leftX) / (values.length - 1), values[i]);
            current.next = new FunctionNode();
            current.next.prev = current;
            current = current.next;
            currIndex++;
        }
        tail = current.prev;
        tail.next = mainHead;
        //mainHead.prev = tail;
    }

    public LinkedListTabulatedFunction(FunctionPoint[] mass) {
        if (mass.length < 2) {
            throw new IllegalArgumentException();
        }
        for (int i = 1; i < mass.length; i++) {
            if (mass[i - 1].getX() >= mass[i].getX()) {
                mainHead.next = mainHead;
                mainHead.prev = mainHead;
                head = mainHead;
                tail = mainHead;
                current = mainHead;
                throw new IllegalArgumentException();
            } else {
                current.next = new FunctionNode();
                current.next.prev = current;
                current.next.point = new FunctionPoint(mass[i - 1].getX(), mass[i - 1].getY());
                current = current.next;
            }
        }
        current.next = new FunctionNode();
        current.next.prev = current;
        current.next.point = new FunctionPoint(mass[mass.length - 1].getX(), mass[mass.length - 1].getY());
        current = current.next;
        length = mass.length;
        head = mainHead.next;
        tail = current;
        tail.next = mainHead;
        //mainHead.prev = tail;
    }

    public FunctionNode getNodeByIndex(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index > length) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        int fromCurrent = Math.abs(currIndex - index);
        int fromFinish = length - index - 1;
        if (fromFinish < index) {
            if (fromFinish < fromCurrent) {
                current = tail;
                currIndex = length - 1;
            }
        } else {
            if (index < fromCurrent) {
                current = head;
                currIndex = 0;
            }
        }
        if (index < currIndex) {
            while (currIndex != index) {
                current = current.prev;
                currIndex--;
            }
        } else {
            while (currIndex != index) {
                current = current.next;
                currIndex++;
            }
        }
        return current;
    }

    public FunctionNode addNodeToTail() {
        tail.next = new FunctionNode();
        tail.next.prev = tail;
        tail.next.next = mainHead;
        tail = tail.next;
        length++;
        mainHead.prev = tail;
        return tail;
    }

    public FunctionNode addNodeByIndex(int index) {
        if (index < 0 || index > length) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        if (index == length) {
            return addNodeToTail();
        }
        getNodeByIndex(index);
        FunctionNode addedNode = new FunctionNode();
        addedNode.next = current;
        addedNode.prev = current.prev;
        current.prev.next = addedNode;
        current.prev = addedNode;
        current = addedNode;
        length++;
        return current;
    }

    public FunctionNode deleteNodeByIndex(int index) {
        if (index < 0 || index >= length) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        getNodeByIndex(index);
        FunctionNode node = current;
        current.prev.next = current.next;
        current.next.prev = current.prev;
        current = current.prev;
        --currIndex;
        --length;
        head = mainHead.next;
        tail = mainHead.prev;
        return node;
    }

    @Override
    public double getLeftDomainBorder() throws IllegalStateException {
        if (length == 0) throw new IllegalStateException();
        return head.point.getX();
    }

    @Override
    public double getRightDomainBorder() throws IllegalStateException {
        if (length == 0) throw new IllegalStateException();
        return tail.point.getX();
    }

    @Override
    public double getFunctionValue(double x) throws FunctionPointIndexOutOfBoundsException {
        if (length == 0) throw new IllegalStateException();
        if (x < head.point.getX() || x > tail.point.getX()) throw new FunctionPointIndexOutOfBoundsException();
        current = head;
        currIndex = 0;
        while (current.point.getX() > x) {
            current = current.next;
            currIndex++;
        }
        if (current.point.getX() == x) return current.point.getX();
        double k = (current.next.point.getY() - current.point.getY()) / (current.next.point.getX() - current.point.getX());
        double b = current.next.point.getY() - k * current.next.point.getX();
        return k * x + b;
    }

    @Override
    public int getPointsCount() {
        return length;
    }

    @Override
    public FunctionPoint getPoint(int index) {
        if (length == 0) throw new IllegalStateException();
        if (index < 0 || index >= length) throw new FunctionPointIndexOutOfBoundsException();
        return getNodeByIndex(index).point;
    }

    @Override
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (length == 0) throw new IllegalStateException();
        if (index < 0 || index >= length) throw new FunctionPointIndexOutOfBoundsException();
        double left = Double.MIN_VALUE;
        double right = Double.MAX_VALUE;
        FunctionNode node = getNodeByIndex(index);
        if (node.prev != null) left = node.prev.point.getX();
        if (node.next != null) right = node.next.point.getX();
        if (left > point.getX() || right < point.getX()) throw new InappropriateFunctionPointException();
        node.point = new FunctionPoint(point);
    }

    @Override
    public double getPointX(int index) {
        if (length == 0) throw new IllegalStateException();
        if (index < 0 || index >= length) throw new FunctionPointIndexOutOfBoundsException();
        return getNodeByIndex(index).point.getX();
    }

    @Override
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (length == 0) throw new IllegalStateException();
        if (index < 0 || index >= length) throw new FunctionPointIndexOutOfBoundsException();
        double left = Double.MIN_VALUE;
        double right = Double.MAX_VALUE;
        FunctionNode node = getNodeByIndex(index);
        if (node.prev != null) left = node.prev.point.getX();
        if (node.next != null) right = node.next.point.getX();
        if (left > x || right < x) throw new InappropriateFunctionPointException();
        node.point.setX(x);
    }

    @Override
    public double getPointY(int index) {
        if (length == 0) throw new IllegalStateException();
        if (index < 0 || index >= length) throw new FunctionPointIndexOutOfBoundsException();
        return getNodeByIndex(index).point.getY();
    }

    @Override
    public void setPointY(int index, double y) {
        if (length == 0) throw new IllegalStateException();
        if (index < 0 || index >= length) throw new FunctionPointIndexOutOfBoundsException();
        getNodeByIndex(index).point.setY(y);
    }

    @Override
    public void deletePoint(int index) {
        if (length < 3) throw new IllegalStateException();
        if (index < 0 || index >= length) throw new FunctionPointIndexOutOfBoundsException();
        deleteNodeByIndex(index);
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        if (length != 0 && (point.getX() < head.point.getX() || point.getX() > tail.point.getX()))
            throw new InappropriateFunctionPointException();
        if (length == 0) {
            head = new FunctionNode();
            head.point = new FunctionPoint(point);
            length++;
            tail = head;
            current = head;
            currIndex = 0;
            return;
        }
        current = head;
        currIndex = 0;
        while (current.point.getX() < point.getX()) {
            current = current.next;
            currIndex++;
        }
        if (current.point.getX() == point.getX()) throw new InappropriateFunctionPointException();
        addNodeByIndex(currIndex).point = point;
    }

    @Override
    public int hashCode() {
        currIndex = 0;
        current = head;
        int hash = 0;
        while (current != mainHead) {
            hash += current.point.hashCode();
            current = current.next;
            currIndex++;
        }
        return hash + length;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof TabulatedFunction tabulatedFunction) {
            if (obj instanceof LinkedListTabulatedFunction linkedListTabulatedFunction) {
                if (linkedListTabulatedFunction.length != length) {
                    return false;
                }
                currIndex = 0;
                current = head;
                FunctionNode currFunc = linkedListTabulatedFunction.head;
                while (current != mainHead) {
                    if (current.point.getX() != currFunc.point.getX()) {
                        return false;
                    }
                    if (current.point.getY() != currFunc.point.getY()) {
                        return false;
                    }
                    current = current.next;
                    currFunc = currFunc.next;
                    currIndex++;
                }
            } else {
                if (tabulatedFunction.getPointsCount() != length) {
                    return false;
                }
                for (int i = 0; i < length; i++) {
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
        FunctionPoint[] points = new FunctionPoint[length];
        currIndex = 0;
        current = head;
        while (current != mainHead) {
            points[currIndex] = current.point;
            current = current.next;
            currIndex++;
        }
        return new LinkedListTabulatedFunction(points);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        current = head;
        currIndex = 0;
        while (current != mainHead) {
            sb.append(current.point.toString()).append(", ");
            current = current.next;
            currIndex++;
        }
        sb.deleteCharAt(sb.length() - 1).deleteCharAt(sb.length() - 1);
        return ("{" + sb + "}");
    }
}