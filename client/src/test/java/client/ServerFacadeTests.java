package client;

import chess.ChessGame;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import request.*;
import response.CreateResponse;
import response.JoinResponse;
import response.LoginResponse;
import response.RegisterResponse;
import server.Server;
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
    public void registerPositive() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("username2", "password2", "email2@email.com");
        RegisterResponse response = facade.register(request);
        Assertions.assertNotNull(response);
    }

    @Test
    public void registerNegative() throws DataAccessException {
        RegisterRequest request = new RegisterRequest(null, null, null);
        Assertions.assertThrows(DataAccessException.class, () -> facade.register(request));
    }

    @Test
    public void loginPositive() throws DataAccessException {
        LoginRequest loginRequest = new LoginRequest("eden", "eden");
        LoginResponse loginResponse = facade.login(loginRequest);
        Assertions.assertNotNull(loginResponse.authToken());
    }

    @Test
    public void loginNegative() throws DataAccessException {
        LoginRequest request = new LoginRequest("badUsername", "terriblePassword");
        Assertions.assertThrows(DataAccessException.class, () -> facade.login(request));
    }

    @Test
    public void logoutPositive() throws DataAccessException {
        try {
            RegisterRequest registerReq = new RegisterRequest("eden", "eden", "eden@email.com");
            facade.register(registerReq);
        } catch (DataAccessException e) {
            throw new DataAccessException("Error: already taken");
        }
        LoginRequest loginReq = new LoginRequest("eden", "eden");
        LoginResponse loginResp = facade.login(loginReq);

        LogoutRequest logoutReq = new LogoutRequest();
        facade.logout(logoutReq);
        Assertions.assertTrue(true);
    }

    @Test
    public void logoutNegative() throws DataAccessException {
        LogoutRequest request = new LogoutRequest();
        Assertions.assertThrows(DataAccessException.class, () -> facade.logout(request));
    }

    @Test
    public void joinGamePositive() throws DataAccessException {
        LoginRequest request = new LoginRequest("eden", "eden");
        LoginResponse response = facade.login(request);

        CreateRequest createRequest = new CreateRequest("CoolestGame");
        CreateResponse createResponse = facade.createGame(createRequest);

        JoinRequest joinRequest = new JoinRequest(ChessGame.TeamColor.WHITE, createResponse.gameID());
        JoinResponse joinResponse = facade.join(joinRequest);
        Assertions.assertTrue(true);
    }

    @Test
    public void joinGameNegative() throws DataAccessException {
        LoginRequest request = new LoginRequest("eden", "eden");
        LoginResponse response = facade.login(request);

        JoinRequest joinRequest = new JoinRequest(ChessGame.TeamColor.BLACK, 6);
        Assertions.assertThrows(DataAccessException.class, () -> facade.join(joinRequest));
    }

    @Test
    public void createGamePositive() throws DataAccessException {
        LoginRequest loginRequest = new LoginRequest("eden", "eden");
        LoginResponse loginResponse = facade.login(loginRequest);

        CreateRequest createRequest = new CreateRequest("AnotherGame");
        CreateResponse createResponse = facade.createGame(createRequest);
        Assertions.assertNotNull(createResponse.gameID());
    }

    @Test
    public void createGameNegative() throws DataAccessException {
        // not logged in
    }

    @Test
    public void listGamesPositive() throws DataAccessException {

    }

    @Test
    public void listGamesNegative() throws DataAccessException {
        // also not logged in
    }
}
