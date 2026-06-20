package controller;

import game.engine.Game;
import game.engine.Role;
import javafx.stage.Stage;
import view.GameView;
import view.MainMenuView;

public class MainMenuController {

    private MainMenuView view;

    public MainMenuController(MainMenuView view, Stage stage) {

        this.view = view;

        view.getStartButton().setOnAction(e -> {

            try {

                Role role;

                if(view.isScarerSelected())
                    role = Role.SCARER;
                else
                    role = Role.LAUGHER;

                Game game = new Game(role);

                new GameView(stage, game);

            } catch(Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}