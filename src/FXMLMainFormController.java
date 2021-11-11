import document.TabulatedFunctionDoc;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class FXMLMainFormController {

    @FXML
    private TextField edX;

    @FXML
    private TextField edY;

    @FXML
    private Label numOfPoint;

    @FXML
    private Label pointX;

    @FXML
    private Label pointY;

    @FXML
    private TableView<FunctionPointT> table;

    @FXML
    private TableColumn<FunctionPointT, Double> xColumn;

    @FXML
    private TableColumn<FunctionPointT, Double> yColumn;

    private TabulatedFunctionDoc tabFDoc;

    public void initialize() {
        tabFDoc = new TabulatedFunctionDoc();
        tabFDoc.newFunction(0,10,5);
        tabFDoc.saveFunctionAs("tabFDoc");
        xColumn.setCellValueFactory(new PropertyValueFactory<FunctionPointT,Double>("X"));
        yColumn.setCellValueFactory(new PropertyValueFactory<FunctionPointT,Double>("Y"));
        for (int i = 0; i < tabFDoc.getPointsCount(); i++) {
            table.getItems().add(new FunctionPointT(tabFDoc.getPointX(i), tabFDoc.getPointY(i)));
        }
    }

    @FXML
    void btNewClick1(ActionEvent event) {
        FunctionPointT functionPointT = new FunctionPointT(Double.parseDouble(edX.getText()), Double.parseDouble(edY.getText()));
        table.getItems().add(functionPointT);
        edX.clear();
        edY.clear();
        redraw();
    }

    @FXML
    void btNewClick2(ActionEvent event) {
        table.getItems().removeAll(table.getSelectionModel().getSelectedItem());
        redraw();
    }

    public void redraw() {
        tabFDoc.saveFunction();
    }
}
