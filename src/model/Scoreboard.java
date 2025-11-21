package model;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.Optional;

/**
 * Model representing an American football scoreboard.
 * Contains all validation, scoring logic, and undo history without any UI dependencies.
 * Kept text-only so it can be shared without bundling build outputs or media.
 */
public final class Scoreboard {
    public enum Team {
        HOME,
        AWAY
    }

    private String homeName = "Home";
    private String awayName = "Away";
    private boolean namesSet;
    private int homeScore;
    private int awayScore;
    private final Deque<ScoreChange> history = new ArrayDeque<>();
    private String lastActionDescription;

    public void setTeamNames(String home, String away) {
        String trimmedHome = trimOrNull(home);
        String trimmedAway = trimOrNull(away);
        if (trimmedHome == null || trimmedAway == null || trimmedHome.isEmpty() || trimmedAway.isEmpty()) {
            throw new IllegalArgumentException("Team names must not be blank.");
        }
        homeName = trimmedHome;
        awayName = trimmedAway;
        namesSet = true;
        lastActionDescription = String.format("Team names set to %s vs %s", homeName, awayName);
    }

    public void addPoints(Team team, int points, String description) {
        validateTeamNamesSet();
        if (points <= 0) {
            throw new IllegalArgumentException("Points must be positive.");
        }
        Objects.requireNonNull(team, "Team cannot be null");
        String safeDescription = description == null ? "" : description.trim();

        ScoreChange change = new ScoreChange(team, points, safeDescription.isEmpty() ? "Score" : safeDescription);
        applyChange(change);
        history.push(change);
        lastActionDescription = formatChange(change);
    }

    public void undoLast() {
        if (history.isEmpty()) {
            throw new IllegalStateException("No actions to undo.");
        }
        ScoreChange change = history.pop();
        reverseChange(change);
        lastActionDescription = "Undid " + formatChange(change);
    }

    public void clearGame() {
        homeScore = 0;
        awayScore = 0;
        history.clear();
        lastActionDescription = "Game reset";
    }

    public String getHomeName() {
        return homeName;
    }

    public String getAwayName() {
        return awayName;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public Optional<String> getLastActionDescription() {
        return Optional.ofNullable(lastActionDescription);
    }

    public boolean hasHistory() {
        return !history.isEmpty();
    }

    private void applyChange(ScoreChange change) {
        if (change.team == Team.HOME) {
            homeScore += change.points;
        } else {
            awayScore += change.points;
        }
    }

    private void reverseChange(ScoreChange change) {
        if (change.team == Team.HOME) {
            homeScore -= change.points;
        } else {
            awayScore -= change.points;
        }
        if (homeScore < 0) {
            homeScore = 0;
        }
        if (awayScore < 0) {
            awayScore = 0;
        }
    }

    private void validateTeamNamesSet() {
        if (!namesSet || homeName == null || homeName.isBlank() || awayName == null || awayName.isBlank()) {
            throw new IllegalStateException("Set team names before scoring.");
        }
    }

    private String formatChange(ScoreChange change) {
        String teamName = change.team == Team.HOME ? homeName : awayName;
        return teamName + " +" + change.points + (change.description.isEmpty() ? "" : " (" + change.description + ")");
    }

    private String trimOrNull(String value) {
        return value == null ? null : value.trim();
    }

    private static final class ScoreChange {
        private final Team team;
        private final int points;
        private final String description;

        private ScoreChange(Team team, int points, String description) {
            this.team = team;
            this.points = points;
            this.description = description;
        }
    }
}
