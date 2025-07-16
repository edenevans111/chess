package dataaccess;

import model.dataaccess.GameData;

import java.util.Collection;

public interface GameDAO {
    void clear() throws DataAccessException;

    void createGame() throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    Collection<GameData> listGames() throws DataAccessException;

    void updateGame(int gameID) throws DataAccessException;

}
