package view; // Ensure this matches MainMenuView

import javafx.application.Application;
import javafx.stage.Stage;
// You don't need to import MainMenuView if they are in the same package

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        // If the error persists, try using the fully qualified name:
        // view.MainMenuView menu = new view.MainMenuView(stage);

        MainMenuView menu = new MainMenuView(stage); 

        stage.setTitle("DoorDasH");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}