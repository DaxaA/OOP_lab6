package document;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class FunctionParametresController implements Initializable {

    @FXML
    public Button cancelButton;

    @FXML
    private TextField leftDomainTextField;

    @FXML
    public Button okButton;

    @FXML
    private Spinner<Integer> pointsCountSpinner;

    @FXML
    private TextField rightDomainTextField;

    private SpinnerValueFactory.IntegerSpinnerValueFactory pointsCountValueFactory;

    private TabFParametres tabFParametres;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pointsCountValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, Integer.MAX_VALUE);
        pointsCountSpinner.setValueFactory(pointsCountValueFactory);
    }

    boolean okAction() {
        double left = Double.parseDouble(leftDomainTextField.getText());
        double right = Double.parseDouble(rightDomainTextField.getText());
        int pointsCount = pointsCountSpinner.getValue();
        tabFParametres = new TabFParametres(left, right, pointsCount);
        return true;
    }

    void cancelAction() {
        tabFParametres = null;
    }

    public TabFParametres getParameters() {
        return tabFParametres;
    }
}
