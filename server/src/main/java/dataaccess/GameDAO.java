package dataaccess;

import model.dataaccess.GameData;

import java.util.Collection;
import java.util.HashSet;

public interface GameDAO {
    void clear() throws DataAccessException;

    void createGame(String whiteUsername, String blackUsername, String gameName) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    HashSet<GameData> listGames() throws DataAccessException;

    void updateGame(int gameID) throws DataAccessException;

}
