package dataaccess;

import chess.ChessGame;
import model.dataaccess.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class MemoryGameDAO implements GameDAO{

    private int nextID = 1;
    private HashMap<Integer, GameData> games;

    public MemoryGameDAO(){
        this.games = new HashMap<>();
    }

    @Override
    public void clear() throws DataAccessException {
        games.clear();
        nextID = 1;
    }

    @Override
    public int createGame(String whiteUsername, String blackUsername, String gameName) throws DataAccessException {
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(nextID,null, null, gameName, game);
        games.put(nextID, gameData);
        int toReturn = nextID;
        nextID++;
        return toReturn;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        GameData game = games.get(gameID);
        if(game == null){
            throw new DataAccessException("Error: bad request");
        }
        return game;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        Collection<GameData> games = this.games.values();
        return games;
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        int gameID = gameData.gameID();
        games.put(gameID, gameData);
    }
}
