package dataaccess;

import model.dataaccess.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public interface GameDAO {
    void clear() throws DataAccessException;

    int createGame(String whiteUsername, String blackUsername, String gameName) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    Collection<GameData> listGames() throws DataAccessException;

    void updateGame(GameData gameData) throws DataAccessException;

}
