package service;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.dataaccess.AuthData;
import model.dataaccess.GameData;
import model.dataaccess.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.CreateRequest;
import request.JoinRequest;
import request.ListRequest;
import response.CreateResponse;
import response.JoinResponse;
import response.ListResponse;
import response.LoginResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameServiceTests {

    private GameService gameService;

    @BeforeEach
    void makeGame() throws InvalidMoveException, DataAccessException{
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        UserData firstUser = new UserData("username1", "password1", "email1@email.com");
        UserData secondUser = new UserData("username2", "password2", "email2@email.com");
        userDAO.createUser(firstUser);
        userDAO.createUser(secondUser);
        AuthData authData = new AuthData("thisIsAnAuth", "username1");
        AuthData authData1 = new AuthData("thisIsAnAuth2", "username2");
        authDAO.createAuth(authData);
        authDAO.createAuth(authData1);

        gameDAO.createGame("whiteUsername", "blackUsername", "CoolestGame");
        gameDAO.createGame(null, null, "OtherGame");

        gameService = new GameService(userDAO, authDAO, gameDAO);
    }

    @Test
    void getGamesGood() throws DataAccessException {
        Collection<GameData> gameInfo = new ArrayList<>();
        GameData game = new GameData(1,"whiteUsername", "blackUsername",
                "CoolestGame", new ChessGame());
        gameInfo.add(game);

        ListRequest listRequest = new ListRequest();
        ListResponse listResponse = gameService.listOfGames(listRequest, "thisIsAnAuth");

        assertTrue(gameService.listOfGames(listRequest, "thisIsAnAuth") instanceof ListResponse);
    }

    @Test
    void getGamesBad() throws DataAccessException {
        Collection<GameData> gameInfo = new ArrayList<>();
        GameData game = new GameData(1,"whiteUsername", "blackUsername",
                "CoolestGame", new ChessGame());
        gameInfo.add(game);

        ListRequest listRequest = new ListRequest();
        assertThrows(DataAccessException.class, () -> gameService.listOfGames(listRequest, " "));
    }

    @Test
    void createGameGood() throws DataAccessException {
        CreateRequest createRequest = new CreateRequest("NewGame");
        // CreateResponse createResponse = gameService.createGame(createRequest, "thisIsAnAuth");
        assertTrue(gameService.createGame(createRequest, "thisIsAnAuth") instanceof CreateResponse);
    }

    @Test
    void createGameBad() throws DataAccessException {
        CreateRequest createRequest = new CreateRequest("AnotherGame");
        assertThrows(DataAccessException.class, () -> gameService.createGame(createRequest, " "));
    }

    @Test
    void joinGameGood() throws DataAccessException {
        // create a valid way for someone to join a game
        JoinRequest joinRequest = new JoinRequest(ChessGame.TeamColor.WHITE,2);
        assertTrue(gameService.joinGame(joinRequest, "thisIsAnAuth") instanceof JoinResponse);
    }

    @Test
    void joinGameBad() throws DataAccessException {
        JoinRequest joinRequest = new JoinRequest(ChessGame.TeamColor.WHITE,1);
        assertThrows(DataAccessException.class, () -> gameService.joinGame(joinRequest, " "));
    }
}
