package document;

import functions.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TabulatedFunctionDoc implements TabulatedFunction{
    private TabulatedFunction tabFun;
    private String file = "";
    private boolean modified = false;
    private boolean fileNameAssigned = false;
    private FXMLMainFormController ctrl;

    public TabulatedFunction getTabFun() {
        return tabFun;
    }
    public void setTabFun(TabulatedFunction tabFun) {
        this.tabFun = tabFun;
    }

    public void newFunction(double leftX, double rightX, int pointsCount) {
        tabFun = new ArrayTabulatedFunction(leftX, rightX, pointsCount);
        modified = false;
    }
    public void tabulateFunction(Function function, double leftX, double rightX, int pointsCount) {
        tabFun = TabulatedFunctions.tabulate(function, leftX, rightX, pointsCount);
        modified = false;
        CallRedraw();
    }
    public void saveFunctionAs(String fileName) {
        file = fileName;
        fileNameAssigned = true;
        JSONObject pointJSON = new JSONObject();
        JSONArray pointArray = new JSONArray();
        JSONObject funArray = new JSONObject();
        for (int i = 0; i < getPointsCount(); i++) {
            pointArray.add(getPointX(i));
            pointArray.add(getPointY(i));
            pointJSON.put("p" + i, pointArray);
            pointArray = new JSONArray();
        }
        funArray.put("tabFun", pointJSON);
        try (FileWriter writer = new FileWriter(file + ".json")) {
            writer.write(funArray.toJSONString());
            writer.flush();
            writer.close();
            modified = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadFunction(String fileName) {
        file = fileName;
        fileNameAssigned = true;
        try (FileReader reader = new FileReader(file)) {
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(reader);
            JSONObject point = (JSONObject) obj;
            FunctionPoint[] pM = new FunctionPoint[point.size()];
            for (int i = 0; i < point.size(); i++) {
                JSONArray pointValue = (JSONArray) point.get("p" + i);
                pM[i] = new FunctionPoint((Double) pointValue.get(0), (Double) pointValue.get(1));
            }
            tabFun = new ArrayTabulatedFunction(pM);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
    public void saveFunction() {
        if (fileNameAssigned) saveFunctionAs(file);
    }

    @Override
    public int getPointsCount() {
        return tabFun.getPointsCount();
    }
    @Override
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        return tabFun.getPoint(index);
    }
    @Override
    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        tabFun.setPoint(index, point);
        modified = true;
        CallRedraw();
    }
    @Override
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        return tabFun.getPointX(index);
    }
    @Override
    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        tabFun.setPointX(index, x);
        modified = true;
        CallRedraw();
    }
    @Override
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        return tabFun.getPointY(index);
    }
    @Override
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        tabFun.setPointY(index, y);
        modified = true;
        CallRedraw();
    }
    @Override
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException {
        tabFun.deletePoint(index);
        modified = true;
        CallRedraw();
    }
    @Override
    public void addPoint(FunctionPoint point) {
        try {
            tabFun.addPoint(point);
            modified = true;
            CallRedraw();
        } catch (InappropriateFunctionPointException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        TabulatedFunctionDoc clone = new TabulatedFunctionDoc();
        clone.tabFun = (TabulatedFunction) tabFun.clone();
        clone.file = file;
        clone.modified = modified;
        clone.fileNameAssigned = fileNameAssigned;
        return clone;
    }
    @Override
    public int hashCode() {
        return tabFun.hashCode();
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof TabulatedFunction) {
            if (((TabulatedFunction) obj).getPointsCount() != getPointsCount()) return false;
            for (int i = 0; i < getPointsCount(); i++) {
                if (!(getPoint(i).equals(((TabulatedFunction) obj).getPoint(i)))) return false;
            }
            return true;
        }
        return false;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < getPointsCount(); i++) {
            builder.append(getPoint(i).toString()).append(", ");
        }
        builder.deleteCharAt(builder.length() - 1).deleteCharAt(builder.length() - 1);
        return "[" + builder + "]";
    }
    @Override
    public double getLeftDomainBorder() {
        return tabFun.getLeftDomainBorder();
    }
    @Override
    public double getRightDomainBorder() {
        return tabFun.getRightDomainBorder();
    }
    @Override
    public double getFunctionValue(double x) {
        return tabFun.getFunctionValue(x);
    }

    public void registerRedrawFunctionController(FXMLMainFormController fxmlMainFormController) {
        ctrl = fxmlMainFormController;
        CallRedraw();
    }
    public void CallRedraw() {
        ctrl.redraw();
    }

    public boolean isModified() {
        return modified;
    }
}
