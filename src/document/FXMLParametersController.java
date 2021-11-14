package document;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class FXMLParametersController implements Initializable {

    @FXML
    private TextField leftDomainTextField;

    @FXML
    private Spinner<Integer> pointsCountSpinner;

    @FXML
    private TextField rightDomainTextField;

    private TabulatedFunctionParameters tabulatedFunctionParameters;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory.IntegerSpinnerValueFactory pointsCountValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, Integer.MAX_VALUE);
        pointsCountSpinner.setValueFactory(pointsCountValueFactory);
    }

    boolean okAction() {
        double left = Double.parseDouble(leftDomainTextField.getText());
        double right = Double.parseDouble(rightDomainTextField.getText());
        int pointsCount = pointsCountSpinner.getValue();
        tabulatedFunctionParameters = new TabulatedFunctionParameters(left, right, pointsCount);
        return true;
    }

    void cancelAction() {
        tabulatedFunctionParameters = null;
    }

    public TabulatedFunctionParameters getParameters() {
        return tabulatedFunctionParameters;
    }
}
