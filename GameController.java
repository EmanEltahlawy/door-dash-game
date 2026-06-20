package controller;

import game.engine.Board;
import game.engine.Game;
import game.engine.cards.Card;
import game.engine.exceptions.InvalidMoveException;
import game.engine.exceptions.OutOfEnergyException;
import game.engine.monsters.Monster;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import view.GameView;

public class GameController {

    private GameView view;
    private Game model;

    public GameController(GameView view, Game model) {

        this.view  = view;
        this.model = model;

        updateUI("Welcome! " + model.getCurrent().getName() + "'s turn.");

        this.view.getRollButton().setOnAction(e -> handleTurn());

        Scene scene = view.getStage().getScene();

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.W) {
                model.getPlayer().setPosition(99);
                view.logEvent("CHEAT → Player moved to cell 99");
                updateUI("CHEAT: Player moved to cell 99");
            } else if (e.getCode() == KeyCode.E) {
                Monster player = model.getPlayer();
                player.setEnergy(player.getEnergy() + 1000);
                view.logEnergyChange(player.getName(), 1000, false);
                view.logEvent("CHEAT → +1000 energy");
                updateUI("CHEAT: Energy increased");
            }
             else if(e.getCode() == KeyCode.X)
                System.exit(0);

        });
    }

    private void handleTurn() {

        Monster current     = model.getCurrent();
        Monster opponent    = current == model.getPlayer()
                              ? model.getOpponent()
                              : model.getPlayer();

        int  oldPos         = current.getPosition();
        int  oldEnergy      = current.getEnergy();
        int  oldOppEnergy   = opponent.getEnergy();
        int  cardsBefore    = Board.getCards().size();
        Card drawnCard      = cardsBefore > 0 ? Board.getCards().get(0) : null;
        boolean wasFrozen   = current.isFrozen();

        try {

            // ── POWERUP ──────────────────────────────────────────────────────
            if (view.getPowerUpCheckbox().isSelected()) {
                try {
                    model.usePowerup();
                    view.logEvent("⚡ " + current.getName()
                            + " used powerup (-500 energy)");
                } catch (OutOfEnergyException ex) {
                    view.showInvalidActionPopup(
                            "Not enough energy to use powerup!\n"
                            + "Required: 500\nCurrent: " + current.getEnergy());
                    view.getPowerUpCheckbox().setSelected(false);
                    return;
                }
            }

            // ── FROZEN: skip turn before rolling ─────────────────────────────
            if (wasFrozen) {
                model.playTurn();                    // unfreezes + switches turn
                view.showDiceRoll(0);                // clear dice (no roll happened)
                view.logFreezeSkip(current.getName());
                updateUI(current.getName() + " was frozen and skipped the turn!");
                return;
            }

            // ── PLAY TURN ────────────────────────────────────────────────────
            model.playTurn();
  

         // ── INFER ACTUAL DICE ROLL from position delta ───────────────────
            int newPos   = current.getPosition();
            int rawDelta = Math.abs(newPos - oldPos);

            String monsterType = current.getClass().getSimpleName();
            int inferredDice;
            int speedMultiplier;

            switch (monsterType) {
                case "Dasher":
                    speedMultiplier = 2;
                    inferredDice = rawDelta / speedMultiplier;
                    break;
                case "MultiTasker":
                    speedMultiplier = 1;
                    inferredDice = rawDelta * 2;
                    break;
                default:
                    speedMultiplier = 1;
                    inferredDice = rawDelta;
                    break;
            }

            inferredDice = Math.max(1, Math.min(6, inferredDice));

            view.showDiceRoll(inferredDice);

            if (monsterType.equals("Dasher")) {
                view.logEvent("🎲 " + current.getName()
                        + " rolled " + inferredDice
                        + " → moved " + (inferredDice * 2)
                        + " steps (×2 Dasher)");
            } else if (monsterType.equals("MultiTasker")) {
                view.logEvent("🎲 " + current.getName()
                        + " rolled " + inferredDice
                        + " → moved " + (inferredDice / 2)
                        + " steps (÷2 MultiTasker)");
            } else {
                view.logEvent("🎲 " + current.getName() + " rolled " + inferredDice);
            }

            // ── POSITION CHANGE ───────────────────────────────────────────────
            if (newPos != oldPos) {
                view.logEvent("📍 " + current.getName()
                        + " moved from " + oldPos + " → " + newPos);
            }

            // ── CARD DRAW ─────────────────────────────────────────────────────
            int     cardsAfter = Board.getCards().size();
            boolean drewCard   = cardsAfter < cardsBefore || cardsBefore == 0;
            if (drewCard) {
                view.logCardDrawn(current.getName(), drawnCard);
            }

            // ── ENERGY CHANGES ────────────────────────────────────────────────
            int deltaCurrent  = current.getEnergy()  - oldEnergy;
            int deltaOpponent = opponent.getEnergy() - oldOppEnergy;

            if (deltaCurrent  != 0) view.logEnergyChange(current.getName(),  deltaCurrent,  false);
            if (deltaOpponent != 0) view.logEnergyChange(opponent.getName(), deltaOpponent, false);
            

            // ── UPDATE UI ─────────────────────────────────────────────────────
            updateUI("Now it's " + model.getCurrent().getName() + "'s turn.");

        } catch (InvalidMoveException ex) {
            view.logEvent("⛔ Invalid move: " + ex.getMessage());
            view.showInvalidActionPopup(ex.getMessage() + "\n\nRoll again.");
            updateUI(current.getName() + " must retry the turn.");

        } catch (Exception ex) {
            ex.printStackTrace();
            view.showInvalidActionPopup("Unexpected error:\n" + ex.getMessage());
            view.logEvent("⛔ Error: " + ex.getMessage());

        } finally {
            view.getPowerUpCheckbox().setSelected(false);
        }
    }

    private void updateUI(String statusMessage) {

        view.getPName()  .setText("Name: "     + model.getPlayer().getName());
        view.getPRole()  .setText("Role: "     + model.getPlayer().getRole());
        view.getPType()  .setText("Type: "     + model.getPlayer().getClass().getSimpleName());
        view.getPEnergy().setText("Energy: "   + model.getPlayer().getEnergy());
        view.getPPos()   .setText("Position: " + model.getPlayer().getPosition());

        view.getOName()  .setText("Name: "     + model.getOpponent().getName());
        view.getORole()  .setText("Role: "     + model.getOpponent().getRole());
        view.getOType()  .setText("Type: "     + model.getOpponent().getClass().getSimpleName());
        view.getOEnergy().setText("Energy: "   + model.getOpponent().getEnergy());
        view.getOPos()   .setText("Position: " + model.getOpponent().getPosition());

        view.getStatusLabel().setText(statusMessage);
        view.updateBoard(model);
        view.refreshStatusEffects(model);

        Monster winner = model.getWinner();
        if (winner != null) {
            Monster loser = winner == model.getPlayer()
                            ? model.getOpponent() : model.getPlayer();
            view.getRollButton().setDisable(true);
            view.getPowerUpCheckbox().setDisable(true);
            view.showWinnerView(winner, loser, view.getStage());
        }
    }
}