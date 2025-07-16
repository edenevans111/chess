package dataaccess;

import model.dataaccess.GameData;

import java.util.Collection;
import java.util.HashSet;

public interface GameDAO {
    void clear() throws DataAccessException;

    void createGame(String whiteUsername, String blackUsername, String gameName) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    // I am not sure if I will need to fix this later, since I don't know if that's technically correct for when we
    // need the SQL stuff...
    HashSet<GameData> listGames() throws DataAccessException;

    void updateGame(GameData gameData) throws DataAccessException;

}
