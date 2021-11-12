package document;

import functions.FunctionPoint;
import functions.InappropriateFunctionPointException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class FXMLMainFormController implements Initializable {

    @FXML
    private TextField edX;

    @FXML
    private TextField edY;

    @FXML
    private Label numOfPoint;

    @FXML
    private TableView<FunctionPointT> table;

    @FXML
    private TableColumn<FunctionPointT, Double> xColumn;

    @FXML
    private TableColumn<FunctionPointT, Double> yColumn;

    @FXML
    private MenuItem exitMenuItem;

    @FXML
    private MenuItem newFileMenuItem;

    @FXML
    private MenuItem openFileMenuItem;

    @FXML
    private MenuItem saveFileAsNewItem;

    @FXML
    private MenuItem saveFileMenuItem;

    @FXML
    private Button addPointButton;

    private FunParametresDialog funParametresDialog;
    private FileChooser fileChooser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        funParametresDialog = new FunParametresDialog();
        fileChooser = new FileChooser();
        xColumn.setCellValueFactory( new PropertyValueFactory<>("X"));
        yColumn.setCellValueFactory( new PropertyValueFactory<>("Y"));
        numOfPoint.setText("Point " + 0 + " of " + Main.tabFDoc.getPointsCount());
        initializeMenu();
    }

    @FXML
    void btNewClick1(ActionEvent event) {
        Main.tabFDoc.addPoint(new FunctionPoint(Double.parseDouble(edX.getText()), Double.parseDouble(edY.getText())));
        edX.clear();
        edY.clear();
    }

    @FXML
    void btNewClick2(ActionEvent event) {
        int index = table.getSelectionModel().getSelectedIndex();
        Main.tabFDoc.deletePoint(index);
    }

    public void redraw() {
        table.getItems().clear();
        for (int i = 0; i < Main.tabFDoc.getPointsCount(); i++) {
            table.getItems().add(new FunctionPointT(Main.tabFDoc.getPointX(i), Main.tabFDoc.getPointY(i)));
        }
        numOfPoint.setText("Point " + (table.getSelectionModel().getSelectedIndex()+1) + " of " + Main.tabFDoc.getPointsCount());
    }

    @FXML
    public void btNewClick3(MouseEvent event) {
        numOfPoint.setText("Point " + (table.getSelectionModel().getSelectedIndex()+1) + " of " + Main.tabFDoc.getPointsCount());
    }

    private void initializeMenu() {
        newFileMenuItem.setOnAction( event -> {
            if (cancelBecauseNotSaved()) return;
            Optional<TabFParametres> params = funParametresDialog.showAndWait();
            params.ifPresent(tabFParametres -> {
                Main.tabFDoc.newFunction(
                        tabFParametres.leftBorderX,
                        tabFParametres.rightBorderX,
                        tabFParametres.pointCount
                );
                redraw();
            });
        });
        saveFileMenuItem.setOnAction(event -> Main.tabFDoc.saveFunction());
        saveFileAsNewItem.setOnAction(event -> saveFileAsAction());
        openFileMenuItem.setOnAction(event -> {
            if (cancelBecauseNotSaved()) return;
            File selectedFile = fileChooser.showOpenDialog(getWindow());
            if (selectedFile != null) {
                Main.tabFDoc.loadFunction(selectedFile.getAbsolutePath());
                redraw();
            }
        });
        exitMenuItem.setOnAction(event -> getWindow().close());

    }

    private void saveFileAsAction() {
            File saveFile = fileChooser.showSaveDialog(getWindow());
            Main.tabFDoc.saveFunctionAs(saveFile.getAbsolutePath());
    }

    private Stage getWindow() {
        return (Stage) addPointButton.getScene().getWindow();
    }

    private boolean cancelBecauseNotSaved() {
        if (Main.tabFDoc.isModified()) {
            Optional<ButtonType> result = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "У вас не сохранена функция, хотите ли сохранить?",
                    ButtonType.CANCEL, ButtonType.NO, ButtonType.YES
            ).showAndWait();

            if (result.isPresent()) {
                ButtonType buttonType = result.get();

                if (ButtonType.CANCEL == buttonType) return true;

                if (ButtonType.YES == buttonType) {
                    Main.tabFDoc.saveFunction();
                }
            } else {
                return true;
            }
        }
        return false;
    }
}
