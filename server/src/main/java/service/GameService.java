package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.dataaccess.GameData;
import request.*;
import response.*;

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
        HashSet<GameData> games = gameDAO.listGames();
        // I need to check the authToken before I can do anything else
        if(authDAO.getAuth(authToken) == null){
            throw new DataAccessException("Error: unauthorized");
        }
        return new ListResponse(games);
    }

    public CreateResponse createGame(CreateRequest createRequest, String authToken) throws DataAccessException {
        if(authDAO.getAuth(authToken) == null){
            throw new DataAccessException("Error: unauthorized");
        }
        gameDAO.createGame(null, null, createRequest.gameName());
        return new CreateResponse(createRequest.gameName());
    }

    public JoinResponse joinGame(JoinRequest joinRequest, String authToken) throws DataAccessException {
        // here I will need to somehow update the game information with the usernames...
        int gameID = joinRequest.gameID();
        ChessGame.TeamColor playerColor = joinRequest.playerColor();
        // now I need to check that it is an actual game
        if(gameDAO.getGame(gameID) == null){
            throw new DataAccessException("Error: bad request");
        }
        // now I need to check that the authToken is good...
        if(authDAO.getAuth(authToken) == null){
            throw new DataAccessException("Error: unauthorized");
        }
        // now I need to make sure that the color is the right one...
        if(gameDAO.getGame(gameID).whiteUsername() != null && playerColor == ChessGame.TeamColor.WHITE){
            throw new DataAccessException("Error: already taken");
        }
        else if(gameDAO.getGame(gameID).blackUsername() != null && playerColor == ChessGame.TeamColor.BLACK){
            throw new DataAccessException("Error: already taken");
        }
        // if it passes through all of those things, then we need to update the GameData with the new username for
        // the proper color
        if(playerColor == ChessGame.TeamColor.BLACK){
            GameData gameData = new GameData(gameID, gameDAO.getGame(gameID).whiteUsername(),
                    authDAO.getAuth(authToken).username(), gameDAO.getGame(gameID).gameName(), gameDAO.getGame(gameID).game());
            gameDAO.updateGame(gameData);
        } else {
            GameData gameData = new GameData(gameID, authDAO.getAuth(authToken).username(),
                    gameDAO.getGame(gameID).blackUsername(), gameDAO.getGame(gameID).gameName(), gameDAO.getGame(gameID).game());
            gameDAO.updateGame(gameData);
        }
        return new JoinResponse(gameID);
    }

}
