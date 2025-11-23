# Football Scoreboard Application - CSCE 314 Final Project

## App Purpose

This JavaFX application implements a football scoreboard that allows users to:
- Set team names for home and away teams
- Add points for different scoring plays (+6 TD, +3 FG, +2 Safety/2PT Conversion, +1 PAT)
- Undo the last scoring action
- Clear the game and reset scores

## MVC Design

**Model** (`model/Scoreboard.java`):
- Contains all game logic and data
- Stores team names, scores, and scoring history
- Validates input (team names must be set before scoring, points must be positive)
- Has no JavaFX imports or UI dependencies

**View** (`view/scoreboard.fxml` and `view/scoreboard.css`):
- FXML file defines the UI layout (created with SceneBuilder)
- CSS file provides styling for buttons, text fields, and other UI elements
- Contains no business logic

**Controller** (`controller/ScoreboardController.java`):
- Connects the view to the model
- Handles button clicks and user input
- Calls model methods to update game state
- Updates the view after each change
- Displays error messages when needed

## How to Build and Run

**Compilation:**

Must be done within src\app

```bash
javac --module-path "C:\path\to\javafx-sdk\lib" --add-modules javafx.controls,javafx.fxml -d out src\model\*.java src\controller\*.java src\app\*.java

```

**Running:**
```bash
java --module-path "C:\path\to\javafx-sdk\lib" --add-modules javafx.controls,javafx.fxml -cp "out;src" app.App
```

Replace `C:\path\to\javafx-sdk\lib` with your actual JavaFX SDK path.

## Model API

**Main methods:**

- `void setTeamNames(String home, String away)` - Sets team names (throws exception if blank)
- `void addPoints(Team team, int points, String description)` - Adds points to a team
- `void undoLast()` - Removes the last scoring action
- `void clearGame()` - Resets scores to 0, keeps team names
- `String getHomeName()` / `String getAwayName()` - Get team names
- `int getHomeScore()` / `int getAwayScore()` - Get current scores
- `Optional<String> getLastActionDescription()` - Get description of last action
- `boolean hasHistory()` - Check if undo is available

**Running Model Tests:**
```bash
javac -d out src/model/Scoreboard.java src/model/ScoreboardTests.java
java -cp out model.ScoreboardTests
```

![alt text](image.png)

## Known Limitations

- Scoreboard labels (team names and scores) are styled programmatically in the controller using `setStyle()` instead of CSS because inline FXML styles were not rendering reliably. 
- No persistence - game state is lost when app closes
- No game clock or quarter tracking
- Scores can go negative if undo is used repeatedly