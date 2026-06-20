package view;

import controller.GameController;
import game.engine.Board;
import game.engine.Game;
import game.engine.cards.*;
import game.engine.cells.*;
import game.engine.cells.Cell;
import game.engine.monsters.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GameView {

    private BorderPane root;
    private GridPane boardGrid;
    private Label statusLabel;
    private Label cardsLabel;
    private Button rollButton;
    private CheckBox powerUpCheckbox;

    // Dice display
    private Label diceLabel;

    private Label pName, pRole, pEnergy, pPos, pType;
    private Label pShield, pConfusion, pFrozen;

    private Label oName, oRole, oEnergy, oPos, oType;
    private Label oShield, oConfusion, oFrozen;

    private VBox cardHistoryBox;
    private VBox eventLogBox;

    private Stage stage;

    // ── THEME ─────────────────────────────────────────────────────────────────
    private static final String BG_ROOT      = "#f0f4ff";
    private static final String BG_PANEL     = "#ffffff";
    private static final String BG_TOP       = "#4361ee";
    private static final String BG_LOG       = "#f8f9ff";
    private static final String BORDER_PANEL = "#c0c8ff";
    private static final String TEXT_PRIMARY = "#1a1a2e";
    private static final String TEXT_SEC     = "#4361ee";
    private static final String TEXT_MUTED   = "#888aaa";
    private static final String RED          = "#ef233c";
    private static final String GREEN        = "#06d6a0";
    private static final String YELLOW       = "#ffd166";
    private static final String PURPLE       = "#9b5de5";
    private static final String BLUE         = "#4361ee";
    private static final String ORANGE       = "#f77f00";

    // Dice face characters
    private static final String[] DICE_FACES =
        {"", "⚀", "⚁", "⚂", "⚃", "⚄", "⚅"};

    public GameView(Stage stage, Game game) {
        this.stage = stage;

        root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: " + BG_ROOT + ";");

        // ── TOP ──────────────────────────────────────
        statusLabel = new Label("Roll the dice to start!");
        statusLabel.setStyle(
                "-fx-font-size: 20px; -fx-font-weight: bold;" +
                "-fx-text-fill: white;" +
                "-fx-background-color: " + BG_TOP + ";" +
                "-fx-padding: 10 28; -fx-background-radius: 22;");

        cardsLabel = new Label();
        cardsLabel.setStyle(
                "-fx-font-size: 15px; -fx-font-weight: bold;" +
                "-fx-text-fill: " + TEXT_SEC + ";");

        VBox top = new VBox(8);
        top.setAlignment(Pos.CENTER);
        top.setPadding(new Insets(12, 0, 12, 0));
        top.setStyle(
                "-fx-background-color: #e8edff;" +
                "-fx-border-color: #c0c8ff; -fx-border-width: 0 0 2 0;");
        top.getChildren().addAll(statusLabel, cardsLabel);
        root.setTop(top);

        // ── CENTER ────────────────────────────────────
        boardGrid = new GridPane();
        boardGrid.setAlignment(Pos.CENTER);
        boardGrid.setHgap(3);
        boardGrid.setVgap(3);
        boardGrid.setPadding(new Insets(10));
        boardGrid.setStyle("-fx-background-color: " + BG_ROOT + ";");
        root.setCenter(boardGrid);

        // ── SIDES ─────────────────────────────────────
        root.setLeft(createStatPanel("PLAYER", true));
        root.setRight(createStatPanel("OPPONENT", false));

        // ── BOTTOM ────────────────────────────────────
        powerUpCheckbox = new CheckBox("Use Powerup  (-500 energy)");
        powerUpCheckbox.setStyle(
                "-fx-font-size: 14px; -fx-font-weight: bold;" +
                "-fx-text-fill: " + TEXT_PRIMARY + ";");

        // Dice display label
        diceLabel = new Label("🎲");
        diceLabel.setStyle(
                "-fx-font-size: 52px;" +
                "-fx-text-fill: " + BLUE + ";" +
                "-fx-background-color: white;" +
                "-fx-border-color: " + BORDER_PANEL + ";" +
                "-fx-border-width: 2;" +
                "-fx-background-radius: 12; -fx-border-radius: 12;" +
                "-fx-padding: 6 16;");
        diceLabel.setMinWidth(100);
        diceLabel.setAlignment(Pos.CENTER);

        rollButton = new Button("ROLL DICE");
        rollButton.setPrefSize(160, 48);
        rollButton.setStyle(
                "-fx-font-size: 17px; -fx-font-weight: bold;" +
                "-fx-background-color: " + BLUE + ";" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 24; -fx-border-radius: 24;");

        HBox rollBox = new HBox(16, diceLabel, rollButton);
        rollBox.setAlignment(Pos.CENTER);

        // ── CARD HISTORY ──────────────────────────────
        Label cardTitle = new Label("🃏  Cards Drawn");
        cardTitle.setStyle(
                "-fx-font-size: 13px; -fx-font-weight: bold;" +
                "-fx-text-fill: " + ORANGE + ";");

        cardHistoryBox = new VBox(4);
        cardHistoryBox.setPadding(new Insets(5));
        cardHistoryBox.setStyle("-fx-background-color: " + BG_LOG + ";");

        ScrollPane cardScroll = new ScrollPane(cardHistoryBox);
        cardScroll.setFitToWidth(true);
        cardScroll.setPrefHeight(110);
        cardScroll.setStyle(
                "-fx-background: " + BG_LOG + ";" +
                "-fx-background-color: " + BG_LOG + ";");

        VBox cardSection = new VBox(4, cardTitle, cardScroll);
        cardSection.setPrefWidth(420);
        cardSection.setStyle(
                "-fx-background-color: " + BG_PANEL + ";" +
                "-fx-border-color: " + ORANGE + ";" +
                "-fx-border-width: 2; -fx-border-radius: 10;" +
                "-fx-background-radius: 10; -fx-padding: 8;");

        // ── EVENT LOG ─────────────────────────────────
        Label eventTitle = new Label("📋  Event Log");
        eventTitle.setStyle(
                "-fx-font-size: 13px; -fx-font-weight: bold;" +
                "-fx-text-fill: " + BLUE + ";");

        eventLogBox = new VBox(4);
        eventLogBox.setPadding(new Insets(5));
        eventLogBox.setStyle("-fx-background-color: " + BG_LOG + ";");

        ScrollPane eventScroll = new ScrollPane(eventLogBox);
        eventScroll.setFitToWidth(true);
        eventScroll.setPrefHeight(110);
        eventScroll.setStyle(
                "-fx-background: " + BG_LOG + ";" +
                "-fx-background-color: " + BG_LOG + ";");

        VBox eventSection = new VBox(4, eventTitle, eventScroll);
        eventSection.setPrefWidth(420);
        eventSection.setStyle(
                "-fx-background-color: " + BG_PANEL + ";" +
                "-fx-border-color: " + BLUE + ";" +
                "-fx-border-width: 2; -fx-border-radius: 10;" +
                "-fx-background-radius: 10; -fx-padding: 8;");

        HBox logs = new HBox(16, cardSection, eventSection);
        logs.setAlignment(Pos.CENTER);

        VBox controls = new VBox(10);
        controls.setAlignment(Pos.CENTER);
        controls.setPadding(new Insets(10, 0, 10, 0));
        controls.setStyle(
                "-fx-background-color: #e8edff;" +
                "-fx-border-color: #c0c8ff; -fx-border-width: 2 0 0 0;");
        controls.getChildren().addAll(powerUpCheckbox, rollBox, logs);
        root.setBottom(controls);

        updateBoard(game);

        Scene scene = new Scene(root, 1500, 980);
        stage.setScene(scene);

        new GameController(this, game);
    }

    // ── DICE DISPLAY ──────────────────────────────────────────────────────────

    public void showDiceRoll(int value) {
        if (value == 0) {
            // Frozen turn — show ice symbol instead
            diceLabel.setText("❄");
            diceLabel.setStyle(
                    "-fx-font-size: 52px;" +
                    "-fx-text-fill: #1a7abf;" +
                    "-fx-background-color: white;" +
                    "-fx-border-color: #1a7abf;" +
                    "-fx-border-width: 2;" +
                    "-fx-background-radius: 12; -fx-border-radius: 12;" +
                    "-fx-padding: 6 16;");
        } else if (value >= 1 && value <= 6) {
            diceLabel.setText(DICE_FACES[value]);
            diceLabel.setStyle(
                    "-fx-font-size: 52px;" +
                    "-fx-text-fill: " + BLUE + ";" +
                    "-fx-background-color: white;" +
                    "-fx-border-color: " + BLUE + ";" +
                    "-fx-border-width: 2;" +
                    "-fx-background-radius: 12; -fx-border-radius: 12;" +
                    "-fx-padding: 6 16;");
        }
    }

    // ── BOARD ─────────────────────────────────────────────────────────────────

    public void updateBoard(Game game) {
        boardGrid.getChildren().clear();

        Cell[][] cells = game.getBoard().getBoardCells();
        Monster current = game.getCurrent();

        for (int i = 0; i < 100; i++) {

            int rowArray = i / 10;
            int colArray = (rowArray % 2 == 0)
                    ? (i % 10)
                    : (9 - (i % 10));

            Cell cell = cells[rowArray][colArray];
            if (cell == null) continue;

            StackPane stack = new StackPane();

            VBox layout = new VBox(2);
            layout.setAlignment(Pos.TOP_CENTER);
            layout.setPrefSize(68, 68);
            layout.setPadding(new Insets(3));

            Label indexLabel = new Label("" + i);
            indexLabel.setStyle(
                    "-fx-font-weight: bold; -fx-font-size: 11px;" +
                    "-fx-text-fill: #555577;");

            Label typeLabel  = new Label();
            typeLabel.setStyle("-fx-font-size: 9px; -fx-font-weight: bold;");

            Label extraLabel = new Label();
            extraLabel.setStyle("-fx-font-size: 8px;");
            extraLabel.setWrapText(true);

            String bgColor, borderColor;

            if (cell instanceof DoorCell) {
                DoorCell d        = (DoorCell) cell;
                boolean activated = d.isActivated();
                boolean positive  = d.getEnergy() > 0;

                if (activated) {
                    bgColor = "#e0e0e0"; borderColor = "#aaaaaa";
                    typeLabel.setText("DOOR ✓");
                    typeLabel.setStyle(
                            "-fx-font-size: 9px; -fx-font-weight: bold;" +
                            "-fx-text-fill: #999999;");
                    extraLabel.setStyle(
                            "-fx-font-size: 8px; -fx-text-fill: #aaaaaa;");
                } else if (positive) {
                    bgColor = "#d4f7e0"; borderColor = "#06d6a0";
                    typeLabel.setText("DOOR ▲");
                    typeLabel.setStyle(
                            "-fx-font-size: 9px; -fx-font-weight: bold;" +
                            "-fx-text-fill: #028a5e;");
                    extraLabel.setStyle(
                            "-fx-font-size: 8px; -fx-text-fill: #028a5e;");
                } else {
                    bgColor = "#fde8ec"; borderColor = "#ef233c";
                    typeLabel.setText("DOOR ▼");
                    typeLabel.setStyle(
                            "-fx-font-size: 9px; -fx-font-weight: bold;" +
                            "-fx-text-fill: #c0172b;");
                    extraLabel.setStyle(
                            "-fx-font-size: 8px; -fx-text-fill: #c0172b;");
                }
                extraLabel.setText(
                        d.getRole() + " "
                        + (d.getEnergy() > 0 ? "+" : "")
                        + d.getEnergy());

            } else if (cell instanceof CardCell) {
                bgColor = "#fff8e1"; borderColor = "#ffd166";
                typeLabel.setText("🃏 CARD");
                typeLabel.setStyle(
                        "-fx-font-size: 9px; -fx-font-weight: bold;" +
                        "-fx-text-fill: #b8860b;");
                extraLabel.setStyle(
                        "-fx-font-size: 8px; -fx-text-fill: #b8860b;");

            } else if (cell instanceof ConveyorBelt) {
                ConveyorBelt cb = (ConveyorBelt) cell;
                bgColor = "#e0f7f4"; borderColor = "#06d6a0";
                typeLabel.setText("⏩ BELT");
                typeLabel.setStyle(
                        "-fx-font-size: 9px; -fx-font-weight: bold;" +
                        "-fx-text-fill: #028a5e;");
                extraLabel.setStyle(
                        "-fx-font-size: 8px; -fx-text-fill: #028a5e;");
                extraLabel.setText("+" + cb.getEffect());

            } else if (cell instanceof ContaminationSock) {
                ContaminationSock cs = (ContaminationSock) cell;
                bgColor = "#f3e8ff"; borderColor = "#9b5de5";
                typeLabel.setText("🧦 SOCK");
                typeLabel.setStyle(
                        "-fx-font-size: 9px; -fx-font-weight: bold;" +
                        "-fx-text-fill: #6a0dad;");
                extraLabel.setStyle(
                        "-fx-font-size: 8px; -fx-text-fill: #6a0dad;");
                extraLabel.setText("" + cs.getEffect());

            } else if (cell instanceof MonsterCell) {
                MonsterCell mc = (MonsterCell) cell;
                bgColor = "#ffe8f0"; borderColor = "#ef233c";
                typeLabel.setText("👾 " + mc.getCellMonster().getName());
                typeLabel.setStyle(
                        "-fx-font-size: 8px; -fx-font-weight: bold;" +
                        "-fx-text-fill: #c0172b;");
                extraLabel.setStyle(
                        "-fx-font-size: 8px; -fx-text-fill: #c0172b;");
                extraLabel.setText(mc.getCellMonster().getRole().toString());

            } else {
                bgColor = "#f8f9ff"; borderColor = "#dde1f5";
                typeLabel.setText("·");
                typeLabel.setStyle(
                        "-fx-font-size: 11px; -fx-text-fill: #ccccdd;");
                extraLabel.setStyle("-fx-font-size: 8px;");
            }

            layout.setStyle(
                    "-fx-background-color: " + bgColor + ";" +
                    "-fx-border-color: "      + borderColor + ";" +
                    "-fx-border-width: 1.5;" +
                    "-fx-background-radius: 5; -fx-border-radius: 5;");

            layout.getChildren().addAll(indexLabel, typeLabel, extraLabel);
            stack.getChildren().add(layout);

            // ── Monster circles ───────────────────────────────────────────
            HBox monsters = new HBox(2);
            monsters.setAlignment(Pos.BOTTOM_CENTER);
            StackPane.setAlignment(monsters, Pos.BOTTOM_CENTER);

            		if (game.getPlayer().getPosition() == i) {

            		    Circle c = new Circle(9);

            		    c.setFill(Color.web("#2563eb")); // blue

            		    if (game.getPlayer() == current) {

            		        c.setStroke(Color.GOLD);
            		        c.setStrokeWidth(3);

            		    } else {

            		        c.setStroke(Color.WHITE);
            		        c.setStrokeWidth(1.5);
            		    }

            		    monsters.getChildren().add(c);
            		}

            		if (game.getOpponent().getPosition() == i) {

            		    Circle c = new Circle(9);

            		    c.setFill(Color.web("#dc2626")); // red

            		    if (game.getOpponent() == current) {

            		        c.setStroke(Color.GOLD);
            		        c.setStrokeWidth(3);

            		    } else {

            		        c.setStroke(Color.WHITE);
            		        c.setStrokeWidth(1.5);
            		    }

            		    monsters.getChildren().add(c);
            		}
            		
            stack.getChildren().add(monsters);

            // ── Grid placement ────────────────────────────────────────────
            
            int visualRow = 9 - rowArray;
            int visualCol = colArray;

            boardGrid.add(stack, visualCol, visualRow);
        }

        cardsLabel.setText("Remaining Cards: " + Board.getCards().size());
        updateStatusEffects(game);
    }


    // ── STATUS EFFECTS ────────────────────────────────────────────────────────

    private void updateStatusEffects(Game game) {
        updateMonsterStatus(game.getPlayer(),
                pShield, pConfusion, pFrozen, pRole, pEnergy);
        updateMonsterStatus(game.getOpponent(),
                oShield, oConfusion, oFrozen, oRole, oEnergy);
    }

    public void refreshStatusEffects(Game game) {
        updateMonsterStatus(game.getPlayer(),
                pShield, pConfusion, pFrozen, pRole, pEnergy);
        updateMonsterStatus(game.getOpponent(),
                oShield, oConfusion, oFrozen, oRole, oEnergy);
    }

    private void updateMonsterStatus(Monster m,
            Label shieldLabel, Label confusionLabel, Label frozenLabel,
            Label roleLabel,   Label energyLabel) {

        shieldLabel.setText("🛡 Shield: " + (m.isShielded() ? "✔ Active" : "None"));
        shieldLabel.setStyle(m.isShielded()
                ? "-fx-text-fill: #028a5e; -fx-font-weight: bold; -fx-font-size: 13px;"
                : "-fx-text-fill: " + TEXT_MUTED + "; -fx-font-size: 13px;");

        boolean confused = m.isConfused();
        confusionLabel.setText("🌀 Confused: " + (confused
                ? m.getConfusionTurns() + " turn(s) left" : "No"));
        confusionLabel.setStyle(confused
                ? "-fx-text-fill: " + PURPLE
                  + "; -fx-font-weight: bold; -fx-font-size: 13px;"
                : "-fx-text-fill: " + TEXT_MUTED + "; -fx-font-size: 13px;");

        boolean roleSwapped = confused && m.getRole() != m.getOriginalRole();
        roleLabel.setText("Role: " + m.getRole()
                + (roleSwapped ? "  ⚠ (swapped!)" : ""));
        roleLabel.setStyle(roleSwapped
                ? "-fx-text-fill: " + PURPLE
                  + "; -fx-font-weight: bold; -fx-font-size: 13px;"
                : "-fx-text-fill: " + TEXT_PRIMARY + "; -fx-font-size: 13px;");

        frozenLabel.setText("❄ Frozen: "
                + (m.isFrozen() ? "YES — skip next turn!" : "No"));
        frozenLabel.setStyle(m.isFrozen()
                ? "-fx-text-fill: #1a7abf; -fx-font-weight: bold; -fx-font-size: 13px;"
                : "-fx-text-fill: " + TEXT_MUTED + "; -fx-font-size: 13px;");

        int e = m.getEnergy();
        String eColor = e >= 1000 ? "#b8860b"
                      : e >= 500  ? "#028a5e"
                      : e >= 200  ? ORANGE
                      : RED;
        energyLabel.setText("Energy: " + e);
        energyLabel.setStyle(
                "-fx-text-fill: " + eColor + ";" +
                "-fx-font-weight: bold; -fx-font-size: 14px;");
    }

    // ── LOG METHODS ───────────────────────────────────────────────────────────

    public void logCardDrawn(String monsterName, Card card) {
        VBox entry;

        if (card == null) {
            Label l = new Label(
                    "🃏 " + monsterName + " drew a card (deck reshuffled)");
            l.setWrapText(true);
            l.setStyle("-fx-font-size: 12px; -fx-text-fill: " + ORANGE + ";");
            entry = new VBox(l);
        } else {
            String lucky = card.isLucky() ? "✅ Lucky" : "❌ Unlucky";

            Label headerLabel = new Label(
                    "🃏 " + monsterName + " drew: "
                    + card.getName() + "  [" + lucky + "]");
            headerLabel.setWrapText(true);
            headerLabel.setStyle(
                    "-fx-font-size: 12px; -fx-font-weight: bold;" +
                    "-fx-text-fill: " + (card.isLucky() ? "#028a5e" : RED) + ";");

            Label descLabel = new Label("  " + card.getDescription());
            descLabel.setWrapText(true);
            descLabel.setStyle(
                    "-fx-font-size: 12px;" +
                    "-fx-text-fill: " + TEXT_PRIMARY + ";");

            Label effectLabel = new Label("  ⚡ " + getCardDetail(card));
            effectLabel.setWrapText(true);
            effectLabel.setStyle(
                    "-fx-font-size: 12px; -fx-font-weight: bold;" +
                    "-fx-text-fill: " + ORANGE + ";");

            entry = new VBox(3, headerLabel, descLabel, effectLabel);
            entry.setStyle(
                    "-fx-background-color: "
                    + (card.isLucky() ? "#f0fff8" : "#fff0f3") + ";" +
                    "-fx-border-color: "
                    + (card.isLucky() ? GREEN : RED) + ";" +
                    "-fx-border-width: 0 0 0 3;" +
                    "-fx-padding: 4 8;");
        }

        cardHistoryBox.getChildren().add(0, entry);
    }

    private String getCardDetail(Card card) {
        if (card instanceof SwapperCard)
            return "Swap positions if player is behind opponent";
        if (card instanceof ShieldCard)
            return "Activates shield — blocks next negative energy loss";
        if (card instanceof EnergyStealCard)
            return "Steal " + ((EnergyStealCard) card).getEnergy()
                    + " energy from opponent";
        if (card instanceof StartOverCard)
            return card.isLucky()
                    ? "Sends OPPONENT back to start"
                    : "Sends PLAYER back to start";
        if (card instanceof ConfusionCard)
            return "Both roles swapped for "
                    + ((ConfusionCard) card).getDuration() + " turn(s)";
        return "";
    }

    public void logEvent(String message) {
        addEventEntry(message, TEXT_SEC);
    }

    public void logEnergyChange(String monsterName, int delta, boolean shieldBlocked) {
        if (shieldBlocked) {
            addEventEntry(
                    "🛡 " + monsterName + "'s shield blocked the energy loss!",
                    "#028a5e");
        } else {
            String sign  = delta >= 0 ? "+" : "";
            String color = delta >= 0 ? "#028a5e" : RED;
            addEventEntry(
                    "⚡ " + monsterName + "  " + sign + delta + " energy",
                    color);
        }
    }

    public void logFreezeSkip(String monsterName) {
        addEventEntry(
                "❄  " + monsterName + " was FROZEN — turn skipped!",
                "#1a7abf");
        statusLabel.setText(
                "❄ " + monsterName + " was FROZEN and skipped their turn!");
        statusLabel.setStyle(
                "-fx-font-size: 20px; -fx-font-weight: bold;" +
                "-fx-text-fill: white;" +
                "-fx-background-color: #1a7abf;" +
                "-fx-padding: 10 28; -fx-background-radius: 22;");
    }

    private void addEventEntry(String text, String color) {
        Label l = new Label(text);
        l.setWrapText(true);
        l.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 12px;");
        eventLogBox.getChildren().add(0, l);
    }

    // ── STAT PANEL ────────────────────────────────────────────────────────────

    private VBox createStatPanel(String title, boolean isPlayer) {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(16));
        panel.setPrefWidth(230);
        panel.setStyle(
                "-fx-background-color: " + BG_PANEL + ";" +
                "-fx-border-color: "      + BORDER_PANEL + ";" +
                "-fx-border-width: 2;" +
                "-fx-background-radius: 12; -fx-border-radius: 12;");

        String accent = isPlayer ? BLUE : RED;

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
                "-fx-font-size: 18px; -fx-font-weight: bold;" +
                "-fx-text-fill: " + accent + ";");

        Label n = new Label("Name: --");
        Label r = new Label("Role: --");
        Label t = new Label("Type: --");
        Label e = new Label("Energy: --");
        Label p = new Label("Position: --");

        for (Label lbl : new Label[]{n, r, t, e, p})
            lbl.setStyle(
                    "-fx-text-fill: " + TEXT_PRIMARY + ";" +
                    "-fx-font-size: 14px;");

        Separator sep = new Separator();

        Label statusTitle = new Label("— Status Effects —");
        statusTitle.setStyle(
                "-fx-font-size: 13px; -fx-font-weight: bold;" +
                "-fx-text-fill: " + TEXT_MUTED + ";");

        Label shield    = new Label("🛡 Shield: None");
        Label confusion = new Label("🌀 Confused: No");
        Label frozen    = new Label("❄ Frozen: No");

        for (Label lbl : new Label[]{shield, confusion, frozen})
            lbl.setStyle("-fx-text-fill: " + TEXT_MUTED + "; -fx-font-size: 13px;");

        if (isPlayer) {
            pName = n; pRole = r; pType = t; pEnergy = e; pPos = p;
            pShield = shield; pConfusion = confusion; pFrozen = frozen;
        } else {
            oName = n; oRole = r; oType = t; oEnergy = e; oPos = p;
            oShield = shield; oConfusion = confusion; oFrozen = frozen;
        }

        panel.getChildren().addAll(
                titleLabel, n, r, t, e, p,
                sep, statusTitle, shield, confusion, frozen);

        return panel;
    }

    // ── INVALID ACTION POPUP ──────────────────────────────────────────────────

    public void showInvalidActionPopup(String reason) {
        Stage popup = new Stage();
        popup.setTitle("Invalid Action");
        popup.initOwner(stage);

        VBox layout = new VBox(16);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #fff8f8;");

        Label icon = new Label("⚠");
        icon.setStyle("-fx-font-size: 44px; -fx-text-fill: " + RED + ";");

        Label title = new Label("Invalid Action");
        title.setStyle(
                "-fx-font-size: 20px; -fx-font-weight: bold;" +
                "-fx-text-fill: " + RED + ";");

        Label msg = new Label(reason);
        msg.setWrapText(true);
        msg.setMaxWidth(320);
        msg.setAlignment(Pos.CENTER);
        msg.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: " + TEXT_PRIMARY + ";" +
                "-fx-text-alignment: center;");

        Button ok = new Button("OK");
        ok.setPrefSize(100, 36);
        ok.setStyle(
                "-fx-font-size: 14px; -fx-font-weight: bold;" +
                "-fx-background-color: " + BLUE + ";" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 18; -fx-border-radius: 18;");
        ok.setOnAction(e -> popup.close());

        layout.getChildren().addAll(icon, title, msg, ok);

        Scene scene = new Scene(layout, 400, 250);
        popup.setScene(scene);
        popup.setResizable(false);
        popup.showAndWait();
    }

    // ── WINNER SCREEN ─────────────────────────────────────────────────────────

    public void showWinnerView(Monster winner, Monster loser, Stage primaryStage) {
        Stage winStage = new Stage();
        winStage.setTitle("🏆 Game Over");
        winStage.initOwner(primaryStage);
        winStage.initModality(Modality.APPLICATION_MODAL);

        VBox layout = new VBox(22);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(50));
        layout.setStyle("-fx-background-color: " + BG_ROOT + ";");

        Label trophy = new Label("🏆");
        trophy.setStyle("-fx-font-size: 80px;");

        Label gameWon = new Label("GAME WON!");
        gameWon.setStyle(
                "-fx-font-size: 52px; -fx-font-weight: bold;" +
                "-fx-text-fill: " + BLUE + ";");

        Label winnerName = new Label(winner.getName());
        winnerName.setStyle(
                "-fx-font-size: 34px; -fx-font-weight: bold;" +
                "-fx-text-fill: " + RED + ";");

        Label winnerRole = new Label("Role: " + winner.getRole());
        winnerRole.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-text-fill: " + TEXT_SEC + ";");

        Label winnerEnergy = new Label("Final Energy: " + winner.getEnergy());
        winnerEnergy.setStyle(
                "-fx-font-size: 20px; -fx-font-weight: bold;" +
                "-fx-text-fill: #028a5e;");

        Separator sep = new Separator();
        sep.setMaxWidth(360);

        Label loserTitle = new Label("Runner-up: " + loser.getName());
        loserTitle.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-text-fill: " + TEXT_MUTED + ";");

        Label loserEnergy = new Label("Final Energy: " + loser.getEnergy());
        loserEnergy.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-text-fill: " + RED + ";");

        Button returnBtn = new Button("↩  Return to Main Menu");
        returnBtn.setPrefSize(240, 48);
        returnBtn.setStyle(
                "-fx-font-size: 15px; -fx-font-weight: bold;" +
                "-fx-background-color: " + BLUE + ";" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 24; -fx-border-radius: 24;");
        returnBtn.setOnAction(e -> {
            winStage.close();
            new view.MainMenuView(primaryStage);
        });

        Button exitBtn = new Button("✕  Exit");
        exitBtn.setPrefSize(130, 48);
        exitBtn.setStyle(
                "-fx-font-size: 15px; -fx-font-weight: bold;" +
                "-fx-background-color: " + RED + ";" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 24; -fx-border-radius: 24;");
        exitBtn.setOnAction(e -> System.exit(0));

        HBox buttons = new HBox(20, returnBtn, exitBtn);
        buttons.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(
                trophy, gameWon, winnerName, winnerRole, winnerEnergy,
                sep, loserTitle, loserEnergy, buttons);

        Scene scene = new Scene(layout, 700, 600);
        winStage.setScene(scene);
        winStage.setResizable(false);
        winStage.show();
    }

    // ── GETTERS ───────────────────────────────────────────────────────────────

    public Label getPName()              { return pName; }
    public Label getPRole()              { return pRole; }
    public Label getPEnergy()            { return pEnergy; }
    public Label getPPos()               { return pPos; }
    public Label getPType()              { return pType; }
    public Label getOName()              { return oName; }
    public Label getORole()              { return oRole; }
    public Label getOEnergy()            { return oEnergy; }
    public Label getOPos()               { return oPos; }
    public Label getOType()              { return oType; }
    public Label getStatusLabel()        { return statusLabel; }
    public Button getRollButton()        { return rollButton; }
    public CheckBox getPowerUpCheckbox() { return powerUpCheckbox; }
    public Stage getStage()              { return stage; }
}