package dataaccess;

import chess.ChessGame;
import com.sun.source.tree.AssertTree;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SQLGameDAOTest {

    private static SQLGameDAO gameDAO;

    private final String[] createStatements = {
            """
           CREATE TABLE IF NOT EXISTS testGameData (
            gameID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
            whiteUsername VARCHAR(255),
            blackUsername VARCHAR(255),
            gameName VARCHAR(255) NOT NULL,
            chessGame TEXT NOT NULL
            )"""
    };

    @BeforeEach

    void setUpGame() throws DataAccessException{
        // I need to createDatabase(in the DatabaseManager)
        gameDAO.configureDatabase(createStatements);
        // create a new chessGame and convert it to a Json
        // create a gameData with random information (make sure everything GameData needs is there

        // get a connection
        // if the table already exists, delete it
        // then make a table and insert in the gameData

    }

    @Test
    void clearPositive() throws DataAccessException{
        // create new SQLGameDAO object
        // clear it
        // assertTrue(GameDAO.isEmpty("testGameData"));
    }

    // positive and negative tests for listGames
    @Test
    void listGamesPositive(){

    }

    @Test
    void listGamesNegative(){

    }

    // positive and negative tests for createGame
    @Test
    void createGamePositive(){

    }

    @Test
    void createGameNegative(){

    }

    // positive and negative tests for getGame
    @Test
    void getGamePositive(){

    }

    @Test
    void getGameNegative(){

    }

    // positive and negative for updateGame
    @Test
    void updateGamePositive(){

    }

    @Test
    void updateGameNegative(){

    }
}
