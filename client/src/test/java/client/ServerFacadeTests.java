package client;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
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
        facade = new ServerFacade("http://localhost:8080");
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
        Assertions.assertThrows(DataAccessException.class, () -> new LoginRequest("badUsername", "terriblePassword"));
    }

    @Test
    public void logoutPositive() throws DataAccessException {
        LogoutRequest request = new LogoutRequest();
    }

    @Test
    public void logoutNegative() throws DataAccessException {

    }

    @Test
    public void joinGamePositive() throws DataAccessException {

    }

    @Test
    public void joinGameNegative() throws DataAccessException {

    }

    @Test
    public void createGamePositive() throws DataAccessException {

    }

    @Test
    public void createGameNegative() throws DataAccessException {

    }

    @Test
    public void listGamesPositive() throws DataAccessException {

    }

    @Test
    public void listGamesNegative() throws DataAccessException {

    }


}
