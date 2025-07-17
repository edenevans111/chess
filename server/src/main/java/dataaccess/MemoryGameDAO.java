package dataaccess;

import chess.ChessGame;
import model.dataaccess.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MemoryGameDAO implements GameDAO{

    private int nextID = 1;
    private HashSet<GameData> games;

    public MemoryGameDAO(){
        this.games = new HashSet<>();
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
        nextID++;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        for(GameData game : games){
            if(game.gameID() == gameID){
                return game;
            }
        }
        throw new DataAccessException("No game with ID " + gameID);
    }

    @Override
    public HashSet<GameData> listGames() throws DataAccessException {
        return games;
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        int gameID = gameData.gameID();
        games.remove(getGame(gameID));
        games.add(gameData);
    }
}
