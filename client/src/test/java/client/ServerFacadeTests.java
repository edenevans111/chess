package client;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import request.LogoutRequest;
import request.RegisterRequest;
import response.RegisterResponse;
import server.Server;
import serverfacade.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:8080");
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }


    @Test
    public void registerPositive() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("username1", "password1", "email1@email.com");
        RegisterResponse response = facade.register(request);
        Assertions.assertNotNull(response);
    }

    @Test
    public void registerNegative() throws DataAccessException {

    }

    @Test
    public void loginPositive() throws DataAccessException {

    }

    @Test
    public void loginNegative() throws DataAccessException {

    }

    @Test
    public void logoutPositive() throws DataAccessException {
        ServerFacade facade = new ServerFacade("http://localhost:8080");
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
