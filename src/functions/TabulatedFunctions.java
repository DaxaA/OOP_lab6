package functions;

import java.io.*;

public class TabulatedFunctions {
    private TabulatedFunctions() {
    }

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) throws IllegalArgumentException {
        if (leftX < function.getLeftDomainBorder() || function.getRightDomainBorder() < rightX) {
            throw new IllegalArgumentException();
        }
        ArrayTabulatedFunction points = new ArrayTabulatedFunction(leftX, rightX, pointsCount);
        for(int i = 1; i < pointsCount; i++) {
            points.setPointY(i, function.getFunctionValue(points.getPointX(i)));
        }
        return points;
        /*FunctionPoint[] points = new FunctionPoint[pointsCount];
        points[0] = new FunctionPoint(leftX, function.getFunctionValue(leftX));
        double pass = (rightX - leftX) / (pointsCount - 1);
        for (int i = 1; i < pointsCount; i++) {
            points[i] = new FunctionPoint(points[i - 1].getX() + pass, function.getFunctionValue(points[i - 1].getX() + pass));
        }
        return new ArrayTabulatedFunction(points);*/
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        int count = function.getPointsCount();
        DataOutputStream data = new DataOutputStream(out);
        data.writeInt(count);
        for (int i = 0; i < count; i++) {
            data.writeDouble(function.getPointX(i));
            data.writeDouble(function.getPointY(i));
        }
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream data = new DataInputStream(in);
        int count = data.readInt();
        FunctionPoint[] points = new FunctionPoint[count];
        for (int i = 0; i < count; i++) {
            points[i] = new FunctionPoint(data.readDouble(), data.readDouble());
        }
        return new ArrayTabulatedFunction(points);
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) {
        PrintWriter writer = new PrintWriter(out);
        writer.println(function.getPointsCount());
        for (int i = 0; i < function.getPointsCount(); i++) {
            writer.println(function.getPointX(i) + " " + function.getPointY(i));
        }
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(in);
        tokenizer.nextToken();
        int count = (int) tokenizer.nval;
        FunctionPoint[] points = new FunctionPoint[count];
        double x, y;
        for (int i = 0; i < count; i++) {
            tokenizer.nextToken();
            x = tokenizer.nval;
            tokenizer.nextToken();
            y = tokenizer.nval;
            points[i] = new FunctionPoint(x, y);
        }
        return new ArrayTabulatedFunction(points);
    }
}
