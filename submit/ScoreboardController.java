package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import model.Scoreboard;

public class ScoreboardController {
    @FXML
    private TextField homeNameField;

    @FXML
    private TextField awayNameField;

    @FXML
    private Label homeNameLabel;

    @FXML
    private Label awayNameLabel;

    @FXML
    private Label homeScoreLabel;

    @FXML
    private Label awayScoreLabel;

    @FXML
    private Label lastActionLabel;

    @FXML
    private RadioButton homeTeamRadio;

    @FXML
    private RadioButton awayTeamRadio;

    @FXML
    private ToggleGroup teamToggleGroup;

    @FXML
    private Button undoButton;

    @FXML
    private Button clearButton;

    @FXML
    private VBox scoreboardPanel;

    private Scoreboard scoreboard;

    @FXML
    private void initialize() {
        scoreboard = new Scoreboard();
        homeTeamRadio.setUserData(Scoreboard.Team.HOME);
        awayTeamRadio.setUserData(Scoreboard.Team.AWAY);
        homeTeamRadio.setSelected(true);
        updateView();
    }

    @FXML
    private void handleSetTeamNames() {
        try {
            scoreboard.setTeamNames(homeNameField.getText(), awayNameField.getText());
            updateView();
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    @FXML
    private void handleAddTouchdown() {
        handleScore(6, "Touchdown");
    }

    @FXML
    private void handleAddFieldGoal() {
        handleScore(3, "Field Goal");
    }

    @FXML
    private void handleAddTwoPoint() {
        handleScore(2, "Two Points");
    }

    @FXML
    private void handleAddExtraPoint() {
        handleScore(1, "Extra Point");
    }

    @FXML
    private void handleUndo() {
        try {
            scoreboard.undoLast();
            updateView();
        } catch (IllegalStateException ex) {
            showError(ex.getMessage());
        }
    }

    @FXML
    private void handleClear() {
        scoreboard.clearGame();
        updateView();
    }

    private void handleScore(int points, String label) {
        Scoreboard.Team team = getSelectedTeam();
        if (team == null) {
            showError("Select a team before scoring.");
            return;
        }
        try {
            scoreboard.addPoints(team, points, label);
            updateView();
        } catch (IllegalStateException | IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    private Scoreboard.Team getSelectedTeam() {
        Object data = teamToggleGroup.getSelectedToggle() == null ? null : teamToggleGroup.getSelectedToggle().getUserData();
        return data instanceof Scoreboard.Team ? (Scoreboard.Team) data : null;
    }

    private void updateView() {
        homeNameLabel.setText(scoreboard.getHomeName());
        awayNameLabel.setText(scoreboard.getAwayName());
        homeScoreLabel.setText(Integer.toString(scoreboard.getHomeScore()));
        awayScoreLabel.setText(Integer.toString(scoreboard.getAwayScore()));
        
        // Apply styles programmatically (this is more reliable than FXML inline or CSS)
        homeNameLabel.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 28px; -fx-font-weight: bold;");
        awayNameLabel.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 28px; -fx-font-weight: bold;");
        homeScoreLabel.setStyle("-fx-text-fill: white; -fx-font-size: 72px; -fx-font-weight: bold;");
        awayScoreLabel.setStyle("-fx-text-fill: white; -fx-font-size: 72px; -fx-font-weight: bold;");
        
        homeTeamRadio.setText(scoreboard.getHomeName());
        awayTeamRadio.setText(scoreboard.getAwayName());
        lastActionLabel.setText(scoreboard.getLastActionDescription().orElse("Ready for kickoff"));
        undoButton.setDisable(!scoreboard.hasHistory());
        clearButton.setDisable(scoreboard.getHomeScore() == 0 && scoreboard.getAwayScore() == 0 && !scoreboard.hasHistory());
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Scoreboard Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}