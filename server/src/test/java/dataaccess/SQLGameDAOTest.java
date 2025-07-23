package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SQLGameDAOTest {

    private static SQLGameDAO gameDAO;

    @BeforeAll
    static void setUp() throws DataAccessException {
        gameDAO = new SQLGameDAO();
        gameDAO.clear();
    }

    @BeforeEach
    void clearBefore() throws DataAccessException {
        gameDAO.clear();
    }

    // ----- clear() -----
    @Test
    @DisplayName("clear() empties the gameData table")
    void clear_Positive() throws DataAccessException {
        int gameID = gameDAO.createGame("white", "black", "game1");
        assertTrue(gameID > 0);

        gameDAO.clear();

        Collection<GameData> games = gameDAO.listGames();
        assertTrue(games.isEmpty(), "Expected empty game list after clear");
    }

    @Test
    @DisplayName("clear() throws DataAccessException on bad DB connection")
    void clear_Negative_InvalidConnection() {
        // This test needs DB connection mocking or an invalid config
        // Placeholder showing you expect DataAccessException
        assertThrows(DataAccessException.class, () -> {
            // Simulate bad connection by manipulating DatabaseManager (not shown here)
            // e.g. DatabaseManager.setConnectionUrl("invalid-url");
            gameDAO.clear();
        });
    }

    // ----- createGame() -----
    @Test
    @DisplayName("createGame() inserts a new game successfully")
    void createGame_Positive() throws DataAccessException {
        int gameID = gameDAO.createGame("Alice", "Bob", "Test Game");
        assertTrue(gameID > 0);

        GameData game = gameDAO.getGame(gameID);
        assertEquals("Alice", game.whiteUsername());
        assertEquals("Bob", game.blackUsername());
        assertEquals("Test Game", game.gameName());
        assertNotNull(game.game());
    }

    @Test
    @DisplayName("createGame() throws DataAccessException given invalid data")
    void createGame_Negative_InvalidData() {
        // Null usernames or gameName should cause failure (depends on DB constraint)
        assertThrows(DataAccessException.class, () -> {
            gameDAO.createGame(null, null, null);
        });
    }

    // ----- getGame() -----
    @Test
    @DisplayName("getGame() returns existing game data")
    void getGame_Positive() throws DataAccessException {
        int gameID = gameDAO.createGame("Carol", "Dave", "Carol vs Dave");
        GameData game = gameDAO.getGame(gameID);
        assertNotNull(game);
        assertEquals(gameID, game.gameID());
    }

    @Test
    @DisplayName("getGame() throws DataAccessException for non-existent gameID")
    void getGame_Negative_InvalidID() {
        int fakeGameID = 999999;
        DataAccessException ex = assertThrows(DataAccessException.class, () -> {
            gameDAO.getGame(fakeGameID);
        });
        assertTrue(ex.getMessage().toLowerCase().contains("bad request"));
    }

    // ----- listGames() -----
    @Test
    @DisplayName("listGames() returns all inserted games")
    void listGames_Positive() throws DataAccessException {
        int gameID1 = gameDAO.createGame("Eve", "Frank", "Game 1");
        int gameID2 = gameDAO.createGame("Grace", "Heidi", "Game 2");

        Collection<GameData> games = gameDAO.listGames();
        assertNotNull(games);
        assertTrue(games.size() >= 2);
        assertTrue(games.stream().anyMatch(g -> g.gameID() == gameID1));
        assertTrue(games.stream().anyMatch(g -> g.gameID() == gameID2));
    }

    @Test
    @DisplayName("listGames() throws DataAccessException on bad DB connection")
    void listGames_Negative_InvalidConnection() {
        // Placeholder - would require mocking or misconfiguring DB connection
        assertThrows(DataAccessException.class, () -> {
            gameDAO.listGames();
        });
    }

    // ----- updateGame() -----
    @Test
    @DisplayName("updateGame() updates an existing game successfully")
    void updateGame_Positive() throws DataAccessException {
        int gameID = gameDAO.createGame("Ivan", "Judy", "Old Game Name");

        GameData game = gameDAO.getGame(gameID);

        GameData updatedGame = new GameData(
                game.gameID(),
                "IvanUpdated",
                "JudyUpdated",
                "New Game Name",
                game.game()
        );

        gameDAO.updateGame(updatedGame);

        GameData fetched = gameDAO.getGame(gameID);

        assertEquals("IvanUpdated", fetched.whiteUsername());
        assertEquals("JudyUpdated", fetched.blackUsername());
        assertEquals("New Game Name", fetched.gameName());
    }

    @Test
    @DisplayName("updateGame() throws DataAccessException if gameID not found")
    void updateGame_Negative_InvalidGameID() {
        GameData fakeGame = new GameData(
                999999,
                "Nobody",
                "Nobody",
                "No Game",
                new ChessGame()
        );
        DataAccessException ex = assertThrows(DataAccessException.class, () -> {
            gameDAO.updateGame(fakeGame);
        });
        assertTrue(ex.getMessage().toLowerCase().contains("game not found"));
    }
}
