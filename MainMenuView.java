package view;

import controller.MainMenuController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainMenuView {

    private VBox root;
    private RadioButton scarer;
    private RadioButton laugher;
    private Button startButton;

    public MainMenuView(Stage stage) {

        root = new VBox(22);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(35));
        root.setStyle("-fx-background-color: #f0f4ff;");

        // ── TITLE ─────────────────────────────────────
        Label title = new Label("DoorDasH");
        title.setFont(new Font(52));
        title.setTextFill(Color.web("#4361ee"));
        title.setStyle(
                "-fx-font-weight: bold;" +
                "-fx-effect: dropshadow(gaussian, #4361ee55, 10, 0.4, 0, 2);");

        Label subtitle = new Label("Scare vs Laugh Touchdown");
        subtitle.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-text-fill: #888aaa;" +
                "-fx-font-style: italic;");

        // ── ROLE SELECTION ────────────────────────────
        VBox roleBox = new VBox(14);
        roleBox.setAlignment(Pos.CENTER);
        roleBox.setPadding(new Insets(20));
        roleBox.setMaxWidth(420);
        roleBox.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 14;" +
                "-fx-border-radius: 14;" +
                "-fx-border-color: #c0c8ff;" +
                "-fx-border-width: 2;");

        Label choose = new Label("Choose Your Side");
        choose.setStyle(
                "-fx-font-size: 22px;" +
                "-fx-text-fill: #1a1a2e;" +
                "-fx-font-weight: bold;");

        scarer = new RadioButton("⚡  SCARER");
        scarer.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-text-fill: #ef233c;" +
                "-fx-font-weight: bold;");

        laugher = new RadioButton("😄  LAUGHER");
        laugher.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-text-fill: #4361ee;" +
                "-fx-font-weight: bold;");

        ToggleGroup group = new ToggleGroup();
        scarer.setToggleGroup(group);
        laugher.setToggleGroup(group);

        roleBox.getChildren().addAll(choose, scarer, laugher);

        // ── INSTRUCTIONS ──────────────────────────────
        Label introduction = new Label(
                "INTRODUCTION\n\n" +
                "DooR DasH: Scare vs Laugh Touchdown is a competitive\n" +
                "board game inspired by the world of Monsters Inc.\n\n" +
                "For generations, Monstropolis was powered by screams.\n" +
                "But laughter was discovered to produce far more energy.\n" +
                "Now Scarers and Laughers compete on the Floor to prove\n" +
                "whose method is superior.\n\n" +
                "Navigate through a dangerous 100-cell factory floor filled with:\n\n" +
                "• Energy Doors\n" +
                "• Conveyor Belts\n" +
                "• Contamination Socks\n" +
                "• Monster Cells\n" +
                "• Random Cards\n\n" +
                "HOW TO PLAY\n\n" +
                "1. Choose your role: Scarer or Laugher\n\n" +
                "2. Roll the dice to move across the board\n\n" +
                "3. Land on special cells to gain or lose energy\n\n" +
                "4. Use powerups strategically (costs 500 energy)\n\n" +
                "5. Reach Boo's Door (Cell 99) with at least 1000 energy to win\n\n" +
                "SPECIAL RULES\n\n" +
                "• If the destination cell is occupied, the move is invalid\n\n" +
                "• Card cells trigger random effects\n\n" +
                "• Conveyor belts move monsters forward\n\n" +
                "• Socks move monsters backward\n\n" +
                "\"We scare because we care.\"\n" +
                "— Monsters Inc."
        );

        introduction.setWrapText(true);
        introduction.setMaxWidth(700);
        introduction.setStyle(
                "-fx-background-color: white;" +
                "-fx-padding: 22;" +
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #1a1a2e;" +
                "-fx-border-color: #c0c8ff;" +
                "-fx-border-width: 2;" +
                "-fx-background-radius: 12;" +
                "-fx-border-radius: 12;");

        ScrollPane introScroll = new ScrollPane(introduction);
        introScroll.setFitToWidth(true);
        introScroll.setPrefViewportHeight(260);
        introScroll.setMaxWidth(760);
        introScroll.setStyle(
                "-fx-background: transparent;" +
                "-fx-background-color: transparent;");

        // ── START BUTTON ──────────────────────────────
        startButton = new Button("START GAME");
        startButton.setPrefSize(240, 55);
        startButton.setDisable(true);
        startButton.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-color: #4361ee;" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 28;" +
                "-fx-cursor: hand;");

        group.selectedToggleProperty().addListener((a, b, c) ->
                startButton.setDisable(false));

        startButton.setOnMouseEntered(e ->
                startButton.setStyle(
                        "-fx-font-size: 20px; -fx-font-weight: bold;" +
                        "-fx-background-color: #3451d1;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 28; -fx-cursor: hand;"));

        startButton.setOnMouseExited(e ->
                startButton.setStyle(
                        "-fx-font-size: 20px; -fx-font-weight: bold;" +
                        "-fx-background-color: #4361ee;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 28; -fx-cursor: hand;"));

        // ── ROOT ──────────────────────────────────────
        root.getChildren().addAll(
                title, subtitle, roleBox, introScroll, startButton);

        Scene scene = new Scene(root, 1000, 850);
        stage.setScene(scene);

        new MainMenuController(this, stage);
    }

    public Button getStartButton() { return startButton; }
    public boolean isScarerSelected() { return scarer.isSelected(); }
}