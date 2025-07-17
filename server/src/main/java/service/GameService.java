package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.dataaccess.AuthData;
import model.dataaccess.GameData;
import request.*;
import response.*;

import java.util.HashMap;
import java.util.HashSet;

public class GameService {

    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public GameService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) throws DataAccessException {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ListResponse listOfGames(ListRequest listRequest, String authToken) throws DataAccessException {
        HashMap<Integer, GameData> games = gameDAO.listGames();
        // I need to check the authToken before I can do anything else
        if(authDAO.getAuth(authToken) == null){
            throw new DataAccessException("Error: unauthorized");
        }
        return new ListResponse(games);
    }

    public CreateResponse createGame(CreateRequest createRequest, String authToken) throws DataAccessException {
        if(authDAO.getAuth(authToken) == null || authToken.isBlank()){
            throw new DataAccessException("Error: unauthorized");
        }
        if(createRequest == null || createRequest.gameName() == null){
            throw new DataAccessException("Error: bad request");
        }
        int gameID = gameDAO.createGame(null, null, createRequest.gameName());
        return new CreateResponse(gameID, createRequest.gameName());
    }

    public JoinResponse joinGame(JoinRequest joinRequest, String authToken) throws DataAccessException {
        int gameID = joinRequest.gameID();
        ChessGame.TeamColor playerColor = joinRequest.playerColor();
        GameData game = gameDAO.getGame(gameID);
        if(playerColor == null){
            throw new DataAccessException("Error: bad request");
        }
        if(game == null){
            throw new DataAccessException("Error: bad request");
        }
        AuthData auth = authDAO.getAuth(authToken);
        if(auth == null){
            throw new DataAccessException("Error: unauthorized");
        }
        if(game.whiteUsername() != null && playerColor == ChessGame.TeamColor.WHITE){
            throw new DataAccessException("Error: already taken");
        }
        else if(game.blackUsername() != null && playerColor == ChessGame.TeamColor.BLACK){
            throw new DataAccessException("Error: already taken");
        }
        String username = auth.username();
        String newWhite = (playerColor == ChessGame.TeamColor.WHITE) ? username : game.whiteUsername();
        String newBlack = (playerColor == ChessGame.TeamColor.BLACK) ? username : game.blackUsername();
        GameData updated = new GameData(game.gameID(), newWhite, newBlack, game.gameName(), game.game());
        gameDAO.updateGame(updated);
        return new JoinResponse(gameID);
    }
}
