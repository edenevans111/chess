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

    private final Gson gson = new Gson();

    @BeforeEach

    /*void setUpGame() throws DataAccessException, SQLException {
        // I need to createDatabase(in the DatabaseManager)
        gameDAO.configureDatabase(createStatements);
        // create a new chessGame and convert it to a Json
        ChessGame game = new ChessGame();
        String gameJson = gson.toJson(game);
        // create a gameData with random information (make sure everything GameData needs is there
        GameData gameData = new GameData(1, "whiteUsername", "blackUsername",
                "BestChessGame", game);

        // get a connection
        try(var conn = DatabaseManager.getConnection()){
            // if the table already exists, delete it
            try {
                var cleanSlate = conn.prepareStatement("DROP TABLE IF EXISTS testGameData");
                cleanSlate.executeUpdate();
            } catch (SQLException e){
                throw new DataAccessException(e.getMessage());
            }
            try (var connection = DatabaseManager.getConnection()){
                for(var statement : createStatements) {
                    try (var preparedStatement = connection.prepareStatement(statement)){
                        preparedStatement.executeUpdate();
                    }
                }
            } catch (Exception e) {
                throw new DataAccessException(String.format("Error: unable to configure User database: %s", e.getMessage()));
            }
        }

        String statement = "INSERT INTO testGameData (whiteUsername, blackUsername, gameName, chessGame) VALUES (?, ?, ?, ?)";

        try(var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(statement,
                    new String[]{"gameID"})){
            ps.setString(1, gameData.whiteUsername());
            ps.setString(2, gameData.blackUsername());
            ps.setString(3, gameData.gameName());
            ps.setString(4, gameJson);

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    rs.getInt(1);
                } else {
                    throw new DataAccessException("Error: Did not get generated gameID");
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error: unable to create game " + ex.getMessage());
        }

    }*/

    @Test
    void clearPositive() throws DataAccessException{
        // create new SQLGameDAO object
        SQLGameDAO gameDAO1 = new SQLGameDAO();
        gameDAO1.clear();
        // clear it
        assertTrue(gameDAO1.isEmpty("testGameData"));
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
