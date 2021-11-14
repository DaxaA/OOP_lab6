package document;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static TabulatedFunctionDoc tabFDoc;

    @Override
    public void start(Stage stage) throws Exception {
        tabFDoc = new TabulatedFunctionDoc();
        tabFDoc.newFunction(0,10,5);
        tabFDoc.saveFunctionAs("tabFDoc");
        FXMLLoader loader = Connect.createFxmlLoader(getClass(), "FXMLMainForm");
        Parent root = loader.load();
        FXMLMainFormController ctrl = loader.getController();
        tabFDoc.registerRedrawFunctionController(ctrl);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}