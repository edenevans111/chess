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
        RegisterRequest request = new RegisterRequest("username4", "password2", "email2@email.com");
        RegisterResponse response = facade.register(request);
        Assertions.assertNotNull(response);
    }

    @Test
    public void registerNegative() throws ResponseException {
        RegisterRequest request = new RegisterRequest(null, null, null);
        Assertions.assertThrows(ResponseException.class, () -> facade.register(request));
    }

    @Test
    public void loginPositive() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("NewPerson", "eden", "email");
        RegisterResponse registerResponse = facade.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("NewPerson", "eden");
        LoginResponse loginResponse = facade.login(loginRequest);
        Assertions.assertNotNull(loginResponse.authToken());
    }

    @Test
    public void loginNegative() throws ResponseException {
        LoginRequest request = new LoginRequest("badUsername", "terriblePassword");
        Assertions.assertThrows(ResponseException.class, () -> facade.login(request));
    }

    @Test
    public void logoutPositive() throws ResponseException {
        try {
            RegisterRequest registerReq = new RegisterRequest("newestEden", "eden", "eden@email.com");
            facade.register(registerReq);
        } catch (ResponseException e) {
            throw new ResponseException("Error: already taken");
        }
        LoginRequest loginReq = new LoginRequest("newestEden", "eden");
        LoginResponse loginResp = facade.login(loginReq);

        LogoutRequest logoutReq = new LogoutRequest();
        facade.logout(logoutReq);
        Assertions.assertTrue(true);
    }

    @Test
    public void logoutNegative() throws ResponseException {
        LogoutRequest request = new LogoutRequest();
        Assertions.assertThrows(ResponseException.class, () -> facade.logout(request));
    }

    @Test
    public void joinGamePositive() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("AnotherNewbie", "password", "email");
        RegisterResponse registerResponse = facade.register(registerRequest);

        CreateRequest createRequest = new CreateRequest("CoolestGame");
        CreateResponse createResponse = facade.createGame(createRequest);

        JoinRequest joinRequest = new JoinRequest(ChessGame.TeamColor.WHITE, createResponse.gameID());
        JoinResponse joinResponse = facade.join(joinRequest);
        Assertions.assertTrue(true);
    }

    @Test
    public void joinGameNegative() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("newUser", "password", "email");
        RegisterResponse registerResponse = facade.register(registerRequest);

        JoinRequest joinRequest = new JoinRequest(ChessGame.TeamColor.BLACK, 6);
        Assertions.assertThrows(ResponseException.class, () -> facade.join(joinRequest));
    }

    @Test
    public void createGamePositive() throws ResponseException {
        RegisterRequest request = new RegisterRequest("username53", "password2", "email2@email.com");
        RegisterResponse response = facade.register(request);

        CreateRequest createRequest = new CreateRequest("AnotherGame");
        CreateResponse createResponse = facade.createGame(createRequest);
        Assertions.assertNotNull(createResponse.gameID());
        LogoutRequest logoutRequest = new LogoutRequest();
        facade.logout(logoutRequest);
    }

    @Test
    public void createGameNegative() throws ResponseException {
        CreateRequest createRequest = new CreateRequest("BadGame");
        Assertions.assertThrows(ResponseException.class, () -> facade.createGame(createRequest));
    }

    @Test
    public void listGamesPositive() throws ResponseException {
        RegisterRequest request = new RegisterRequest("username9", "password2", "email2@email.com");
        RegisterResponse response = facade.register(request);

        ListRequest listRequest = new ListRequest();
        ListResponse listResponse = facade.listGames(listRequest);
        Assertions.assertNotNull(listResponse);
        LogoutRequest logoutRequest = new LogoutRequest();
        facade.logout(logoutRequest);
    }

    @Test
    public void listGamesNegative() throws ResponseException {
        ListRequest listRequest = new ListRequest();
        Assertions.assertThrows(ResponseException.class, () -> facade.listGames(listRequest));
    }
}
