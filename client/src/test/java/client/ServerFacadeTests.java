package client;

import chess.ChessGame;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import request.*;
import response.*;
import server.Server;
import serverfacade.ResponseException;
import serverfacade.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        // this is an issue
        facade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerPositive() throws ResponseException {
        RegisterRequest request = new RegisterRequest("username3", "password2", "email2@email.com");
        RegisterResponse response = facade.register(request);
        Assertions.assertNotNull(response);
    }

    @Test
    public void registerNegative() throws ResponseException {
        RegisterRequest request = new RegisterRequest(null, null, null);
        Assertions.assertThrows(DataAccessException.class, () -> facade.register(request));
    }

    @Test
    public void loginPositive() throws ResponseException {
        LoginRequest loginRequest = new LoginRequest("eden", "eden");
        LoginResponse loginResponse = facade.login(loginRequest);
        Assertions.assertNotNull(loginResponse.authToken());
    }

    @Test
    public void loginNegative() throws ResponseException {
        LoginRequest request = new LoginRequest("badUsername", "terriblePassword");
        Assertions.assertThrows(DataAccessException.class, () -> facade.login(request));
    }

    @Test
    public void logoutPositive() throws ResponseException {
        try {
            RegisterRequest registerReq = new RegisterRequest("eden", "eden", "eden@email.com");
            facade.register(registerReq);
        } catch (ResponseException e) {
            throw new ResponseException("Error: already taken");
        }
        LoginRequest loginReq = new LoginRequest("eden", "eden");
        LoginResponse loginResp = facade.login(loginReq);

        LogoutRequest logoutReq = new LogoutRequest();
        facade.logout(logoutReq);
        Assertions.assertTrue(true);
    }

    @Test
    public void logoutNegative() throws ResponseException {
        LogoutRequest request = new LogoutRequest();
        Assertions.assertThrows(DataAccessException.class, () -> facade.logout(request));
    }

    @Test
    public void joinGamePositive() throws ResponseException {
        LoginRequest request = new LoginRequest("eden", "eden");
        LoginResponse response = facade.login(request);

        CreateRequest createRequest = new CreateRequest("CoolestGame");
        CreateResponse createResponse = facade.createGame(createRequest);

        JoinRequest joinRequest = new JoinRequest(ChessGame.TeamColor.WHITE, createResponse.gameID());
        JoinResponse joinResponse = facade.join(joinRequest);
        Assertions.assertTrue(true);
    }

    @Test
    public void joinGameNegative() throws ResponseException {
        LoginRequest request = new LoginRequest("eden", "eden");
        LoginResponse response = facade.login(request);

        JoinRequest joinRequest = new JoinRequest(ChessGame.TeamColor.BLACK, 6);
        Assertions.assertThrows(DataAccessException.class, () -> facade.join(joinRequest));
    }

    @Test
    public void createGamePositive() throws ResponseException {
        LoginRequest loginRequest = new LoginRequest("eden", "eden");
        LoginResponse loginResponse = facade.login(loginRequest);

        CreateRequest createRequest = new CreateRequest("AnotherGame");
        CreateResponse createResponse = facade.createGame(createRequest);
        Assertions.assertNotNull(createResponse.gameID());
    }

    @Test
    public void createGameNegative() throws ResponseException {
        CreateRequest createRequest = new CreateRequest("DifferentGame");
        Assertions.assertThrows(DataAccessException.class, () -> facade.createGame(createRequest));
    }

    @Test
    public void listGamesPositive() throws ResponseException {
        LoginRequest loginRequest = new LoginRequest("username2", "password2");
        LoginResponse loginResponse = facade.login(loginRequest);

        ListRequest listRequest = new ListRequest();
        ListResponse listResponse = facade.listGames(listRequest);
        Assertions.assertNotNull(listResponse);
    }

    @Test
    public void listGamesNegative() throws ResponseException {
        ListRequest listRequest = new ListRequest();
        Assertions.assertThrows(DataAccessException.class, () -> facade.listGames(listRequest));
    }
}
