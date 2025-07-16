package dataaccess;

import chess.ChessGame;
import model.dataaccess.GameData;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class MemoryGameDAO implements GameDAO{

    private int nextID = 1;
    private HashSet<GameData> games;

    private MemoryGameDAO(){

    }
    @Override
    public void clear() throws DataAccessException {
        games.clear();
    }
    @Override
    public void createGame(String whiteUsername, String blackUsername, String gameName) throws DataAccessException {
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(nextID,whiteUsername, blackUsername, gameName, game);
        games.add(gameData);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public HashSet<GameData> listGames() throws DataAccessException {
        return games;
    }

    @Override
    public void updateGame(int gameID) throws DataAccessException {

    }
}
