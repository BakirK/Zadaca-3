package ba.unsa.etf.rs.zadaca3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        LibraryDAO model = LibraryDAO.getInstance();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainform.fxml"));
        loader.setController(new MainController(model));
        Parent root = loader.load();
        primaryStage.setTitle("Library");
        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
