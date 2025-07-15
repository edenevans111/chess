package dataaccess;

import model.dataaccess.GameData;

public interface GameDAO {
    void clear() throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;
}
