package model;

/**
 * Simple main-based unit tests for the Scoreboard model.
 */
public final class ScoreboardTests {
    public static void main(String[] args) {
        ScoreboardTests tests = new ScoreboardTests();
        tests.runAll();
    }

    private void runAll() {
        try {
            testSetTeamNames();
            testScoringAndUndo();
            testClearGame();
            testValidation();
            System.out.println("All tests PASS");
        } catch (AssertionError ex) {
            System.out.println("FAIL: " + ex.getMessage());
        } catch (Throwable t) {
            System.out.println("FAIL (unexpected error): " + t.getMessage());
        }
    }

    private void testSetTeamNames() {
        Scoreboard sb = new Scoreboard();
        sb.setTeamNames("Aggies", "Gamecocks");
        assert "Aggies".equals(sb.getHomeName()) : "Home team should be updated";
        assert "Gamecocks".equals(sb.getAwayName()) : "Away team should be updated";
    }

    private void testScoringAndUndo() {
        Scoreboard sb = new Scoreboard();
        sb.setTeamNames("Home", "Away");
        sb.addPoints(Scoreboard.Team.HOME, 6, "Touchdown");
        sb.addPoints(Scoreboard.Team.AWAY, 3, "Field Goal");
        assert sb.getHomeScore() == 6 : "Home should have 6";
        assert sb.getAwayScore() == 3 : "Away should have 3";
        sb.undoLast();
        assert sb.getAwayScore() == 0 : "Undo should remove away score";
        sb.undoLast();
        assert sb.getHomeScore() == 0 : "Undo should remove home score";
    }

    private void testClearGame() {
        Scoreboard sb = new Scoreboard();
        sb.setTeamNames("Home", "Away");
        sb.addPoints(Scoreboard.Team.HOME, 2, "Safety");
        sb.addPoints(Scoreboard.Team.AWAY, 1, "PAT");
        sb.clearGame();
        assert sb.getHomeScore() == 0 : "Clear should reset home";
        assert sb.getAwayScore() == 0 : "Clear should reset away";
        assert !sb.hasHistory() : "Clear should wipe history";
    }

    private void testValidation() {
        Scoreboard sb = new Scoreboard();
        boolean threw;

        threw = false;
        try {
            sb.setTeamNames("", "Team");
        } catch (IllegalArgumentException ex) {
            threw = true;
        }
        assert threw : "Blank name should throw";

        threw = false;
        try {
            sb.addPoints(Scoreboard.Team.HOME, 3, "FG");
        } catch (IllegalStateException ex) {
            threw = true;
        }
        assert threw : "Cannot score before names set";

        sb.setTeamNames("Home", "Away");
        threw = false;
        try {
            sb.addPoints(Scoreboard.Team.AWAY, -1, "Bad");
        } catch (IllegalArgumentException ex) {
            threw = true;
        }
        assert threw : "Negative points should throw";

        threw = false;
        try {
            sb.undoLast();
        } catch (IllegalStateException ex) {
            threw = true;
        }
        assert threw : "Cannot undo without history";
    }
}
