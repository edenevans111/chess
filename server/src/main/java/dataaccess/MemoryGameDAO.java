package dataaccess;

import model.dataaccess.GameData;

import java.util.Collection;
import java.util.List;

public class MemoryGameDAO implements GameDAO{
    @Override
    public void clear() throws DataAccessException {
        
    }

    @Override
    public void createGame() throws DataAccessException {

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
    public void updateGame(int gameID) throws DataAccessException {

    }
}
