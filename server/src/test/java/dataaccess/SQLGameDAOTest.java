package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import com.sun.source.tree.AssertTree;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SQLGameDAOTest {

    SQLGameDAO gameDAO = new SQLGameDAO();

    @BeforeEach
    void setUp() throws DataAccessException {
        gameDAO.clear();
    }

    private final Gson gson = new Gson();

    UserData userData1 = new UserData("username1", "password1", "email1@email.com");
    UserData userData2 = new UserData("username2", "password2", "email2@email.com");



    //@BeforeEach
    // I might just need to delete everything before each of the tests to make sure that
    // they each have a blank slate
    // maybe I need to also have some userData...



    @Test
    void clearPositive() throws DataAccessException{
        gameDAO.createGame("whiteUsername", "blackUsername", "BestGameEver");
        gameDAO.clear();
        assertTrue(gameDAO.isEmpty());
    }

    // positive and negative tests for listGames
    @Test
    void listGamesPositive() throws DataAccessException {
        gameDAO.createGame("username1", "username2", "Game1");
        gameDAO.createGame("username3", "username4", "Game2");
        Collection<GameData> games = gameDAO.listGames();
        assertNotNull(games);
        // need to make sure that in the table,

    }

    @Test
    void listGamesNegative() throws DataAccessException {
        Collection<GameData> games = gameDAO.listGames();
        assertNotNull(games);
    }

    // positive and negative tests for createGame
    @Test
    void createGamePositive() throws DataAccessException {
        int gameID = gameDAO.createGame("whiteUser", "blackUser", "BestestGame");
        assertTrue(gameID > 0);
        GameData gameData = gameDAO.getGame(gameID);
        assertEquals("whiteUser", gameData.whiteUsername());
        assertNotNull(gameData.game());
    }

    @Test
    void createGameNegative() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> {
            gameDAO.createGame("whiteUser", "blackUser", null);
        });
    }

    // positive and negative tests for getGame
    @Test
    void getGamePositive() throws DataAccessException {
        int gameID = gameDAO.createGame("whiteUsername", "blackUsername", "JustAnotherGame");
        GameData gameData = gameDAO.getGame(gameID);
        assertNotNull(gameData);
    }

    @Test
    void getGameNegative() throws DataAccessException {
        int gameID = gameDAO.createGame("whiteUsername", "blackUsername", "JustAnotherGame");
        assertThrows(DataAccessException.class, () ->{
            gameDAO.getGame(2);
        });
    }

    // positive and negative for updateGame
    @Test
    void updateGamePositive() throws DataAccessException {
        int gameID = gameDAO.createGame("whiteUsername", "blackUsername", "JustAnotherGame");
        GameData gameData = gameDAO.getGame(gameID);

        GameData updated = new GameData(gameData.gameID(), "newWhite", "blackUsername",
                "JustAnotherGame", gameData.game());
        gameDAO.updateGame(updated);

        GameData updatedGame = gameDAO.getGame(gameID);
        assertEquals("newWhite", updatedGame.whiteUsername());
    }

    @Test
    void updateGameNegative() throws DataAccessException {
        ChessGame game = new ChessGame();
        GameData fakeNews = new GameData(5, "whiteUSer", "blackUser", "ErrorGame", game);
        assertThrows(DataAccessException.class, () ->{
            gameDAO.updateGame(fakeNews);
        });
    }
}
