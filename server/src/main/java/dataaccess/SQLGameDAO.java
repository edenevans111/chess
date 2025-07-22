package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.List;

public class SQLGameDAO implements GameDAO{

    public SQLGameDAO() {
        try {
            configureDatabase();
        } catch (DataAccessException e) {
            System.out.println("SQLAuthDAO failed to make database");
        }

    }

    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public int createGame(String whiteUsername, String blackUsername, String gameName) throws DataAccessException {
        return 0;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {

    }

    private final String[] createStatements = {
            """
           CREATE TABLE IF NOT EXISTS gameData (
            gameID int NOT NULL,
            whiteUsername VARCHAR(255),
            blackUsername VARCHAR(255),
            gameName VARCHAR(255) NOT NULL,
            chessGame TEXT NOT NULL,
            PRIMARY KEY (gameID)
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
            throw new DataAccessException(String.format("unable to configure database: %s", e.getMessage()));
        }
    }

}
