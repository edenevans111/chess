package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static java.sql.Types.NULL;

public class SQLGameDAO implements GameDAO{

    private final Gson gson = new Gson();

    public SQLGameDAO() {
        try {
            configureDatabase();
        } catch (DataAccessException e) {
            System.out.println("Error: SQLAuthDAO failed to make database");
        }

    }

    @Override
    public void clear() throws DataAccessException {
        String statement = "TRUNCATE TABLE gameData";
        executeUpdate(statement);
    }

    // I might need to change this so that it just takes in the gameData object instead of all the random stuff...
    @Override
    public int createGame(String whiteUsername, String blackUsername, String gameName) throws DataAccessException {
        String statement = "INSERT INTO gameData (whiteUsername, blackUsername, gameName, chessGame) VALUES (?, ?, ?, ?)";
        ChessGame chessGame = new ChessGame();
        String gameJson = gson.toJson(chessGame);

        try(var conn = DatabaseManager.getConnection();
                var ps = conn.prepareStatement(statement,
                        new String[]{"gameID"})){
                ps.setString(1, whiteUsername);
                ps.setString(2, blackUsername);
                ps.setString(3, gameName);
                ps.setString(4, gameJson);

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    return rs.getInt(1);
                } else {
                    throw new DataAccessException("Error: Did not get generated gameID");
                }
            }
        } catch (SQLException ex) {
            // here is where the creation is having a problem
            throw new DataAccessException("Error: unable to create game " + ex.getMessage());
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        String statement = "SELECT * FROM gameData WHERE gameID=?";
        try(var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(statement)){
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()){
                    if (rs.next()){
                        return readGame(rs);
                    } else {
                        throw new DataAccessException("Error: bad request");
                    }
                }
            } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
       String statement = "SELECT * FROM gameData";
       Collection<GameData> games = new ArrayList<>();
       try (var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(statement);
            var rs = ps.executeQuery()){
           while(rs.next()){
               games.add(readGame(rs));
           }
       } catch (SQLException e) {
           throw new DataAccessException("Error: "+e.getMessage());
       }
        return games;
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        String statement = "UPDATE gameData SET whiteUsername=?, blackUsername=?, gameName=?, " +
                "chessGame=? WHERE gameID=?";

        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)){
            String gameJson = gson.toJson(gameData.game());
            ps.setString(1, gameData.whiteUsername());
            ps.setString(2, gameData.blackUsername());
            ps.setString(3, gameData.gameName());
            ps.setString(4, gameJson);
            ps.setInt(5, gameData.gameID());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected <= 0){
                throw new DataAccessException("Error: game not found");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }

    private final String[] createStatements = {
            """
           CREATE TABLE IF NOT EXISTS gameData (
            gameID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
            whiteUsername VARCHAR(255),
            blackUsername VARCHAR(255),
            gameName VARCHAR(255) NOT NULL,
            chessGame TEXT NOT NULL
            )"""
    };

    private void configureDatabase() throws DataAccessException{
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()){
            for(var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)){
                    preparedStatement.executeUpdate();
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Error: " + e.getMessage()));
        }
    }

    private void executeUpdate(String statement, Object... parameters) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var ps = conn.prepareStatement(statement);
            for (var i = 0; i < parameters.length; i++) {
                Object param = parameters[i];
                if (param == null){
                    ps.setNull(i + 1, NULL);
                } else if (param instanceof String p) {
                    ps.setString(i + 1, p);
                }
                else if (param instanceof Integer p) {
                    ps.setInt(i + 1, p);
                } else {
                    throw new DataAccessException("Error: Wrong type has been entered: " + param.getClass());
                }
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException( String.format("Error: unable to update Game database: %s, %s", statement, e.getMessage()));
        }
    }

    private GameData readGame(ResultSet rs) throws SQLException{
        int gameID = rs.getInt("gameID");
        String whiteUsername = rs.getString("whiteUsername");
        String blackUsername = rs.getString("blackUsername");
        String gameName = rs.getString("gameName");
        String gameJson = rs.getString("chessGame");
        ChessGame gameObj = gson.fromJson(gameJson, ChessGame.class);
        return new GameData(gameID, whiteUsername, blackUsername, gameName, gameObj);
    }
}
