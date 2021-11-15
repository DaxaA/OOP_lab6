package document;

import functions.Function;
import functions.FunctionPoint;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
    private Menu tabulateButton;

    @FXML
    private MenuItem saveFileMenuItem;

    @FXML
    private Button adding;

    private ParametersConnection parametersConnection;
    private FileChooser fileChooser;
    private Loader loader;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        parametersConnection = new ParametersConnection();
        fileChooser = new FileChooser();
        loader = new Loader();
        xColumn.setCellValueFactory( new PropertyValueFactory<>("X"));
        yColumn.setCellValueFactory( new PropertyValueFactory<>("Y"));
        numOfPoint.setText("Point " + 0 + " of " + Main.tabFDoc.getPointsCount());
        initializeMenu();
    }

    @FXML
    void btNewClick1() {
        Main.tabFDoc.addPoint(new FunctionPoint(Double.parseDouble(edX.getText()), Double.parseDouble(edY.getText())));
        edX.clear();
        edY.clear();
    }

    @FXML
    void btNewClick2() {
        int index = table.getSelectionModel().getSelectedIndex();
        Main.tabFDoc.deletePoint(index);
    }

    public void redraw() {
        table.getItems().clear();
        for (int i = 0; i < Main.tabFDoc.getPointsCount(); i++) {
            table.getItems().add(new FunctionPointT(Main.tabFDoc.getPointX(i), Main.tabFDoc.getPointY(i)));
        }
        //numOfPoint.setText("Point " + (table.getSelectionModel().getSelectedIndex()+1) + " of " + Main.tabFDoc.getPointsCount());
        btNewClick3();
    }

    @FXML
    public void btNewClick3() {
        numOfPoint.setText("Point " + (table.getSelectionModel().getSelectedIndex()+1) + " of " + Main.tabFDoc.getPointsCount());
    }

    private void initializeMenu() {
        newFileMenuItem.setOnAction( event -> {
            if (cancelBecauseNotSaved()) return;
            Optional<TabulatedFunctionParameters> params = parametersConnection.showAndWait();
            params.ifPresent(tabulatedFunctionParameters -> {
                try {
                    Main.tabFDoc.newFunction(
                            tabulatedFunctionParameters.leftBorderX,
                            tabulatedFunctionParameters.rightBorderX,
                            tabulatedFunctionParameters.pointCount
                    );
                    redraw();
                } catch (IllegalArgumentException e) {
                    Error.errorProcession(e, "New function error");
                }
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
        tabulateButton.setOnAction(event -> {
            if (cancelBecauseNotSaved()) return;
            File selectedFile = fileChooser.showOpenDialog(getWindow());
            if (selectedFile != null) {
                try {
                    Function function = loader.loadFunction(selectedFile);
                    Optional<TabulatedFunctionParameters> parameters = parametersConnection.showAndWait();
                    parameters.ifPresent(tabulatedFunctionParameters -> {
                        try {
                            Main.tabFDoc.tabulateFunction(
                                    function, tabulatedFunctionParameters.leftBorderX,
                                    tabulatedFunctionParameters.rightBorderX, tabulatedFunctionParameters.pointCount
                            );
                            redraw();
                        } catch (IllegalArgumentException e) {
                            Error.errorProcession(e, "Tabulate error");
                        }
                    });
                } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                    e.printStackTrace();
                }

            }
        });
        Connect.connection(tabulateButton);
    }

    private void saveFileAsAction() {
            File saveFile = fileChooser.showSaveDialog(getWindow());
            Main.tabFDoc.saveFunctionAs(saveFile.getAbsolutePath());
    }

    private Stage getWindow() {
        return (Stage) adding.getScene().getWindow();
    }

    private boolean cancelBecauseNotSaved() {
        if (Main.tabFDoc.isModified()) {
            Optional<ButtonType> result = new Alert(
                    Alert.AlertType.CONFIRMATION,"Save function?",
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
