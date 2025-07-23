package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import response.LoginResponse;
import response.RegisterResponse;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserSQLServiceTests {

    private UserService userService;

    @BeforeEach
    void makeStuff() throws DataAccessException {
        MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();

        UserData user = new UserData("username1", "password2", "emmail3@gmail.com");
        memoryUserDAO.createUser(user);
        userService = new UserService(memoryUserDAO, memoryAuthDAO, memoryGameDAO);
    }

    @Test
    void registerBad() throws DataAccessException{
        RegisterRequest registerRequest = new RegisterRequest(null, "password3", "compsci4@email.com");
        assertThrows(DataAccessException.class, () -> userService.register(registerRequest));

    }

    @Test
    void registerGood() throws DataAccessException{
        // user does everything correctly
        RegisterRequest registerRequest = new RegisterRequest("newUsername",
                "GoodPassword", "greatemail@email.com");
        assertTrue(userService.register(registerRequest) instanceof RegisterResponse);
    }

    @Test
    void loginGood() throws DataAccessException {
        LoginRequest loginRequest = new LoginRequest("username1", "password2");
        assertTrue(userService.login(loginRequest) instanceof LoginResponse);
    }

    @Test
    void loginBad() throws DataAccessException {
        LoginRequest loginRequest = new LoginRequest("username", "password2");
        assertThrows(DataAccessException.class, () -> userService.login(loginRequest));
    }

    @Test
    void logoutGood() throws DataAccessException {
        LoginRequest loginRequest = new LoginRequest("username1", "password2");
        LoginResponse loginResponse = userService.login(loginRequest);

        LogoutRequest logoutRequest = new LogoutRequest();
        String authToken = loginResponse.authToken();
        userService.logout(logoutRequest, authToken);

    }

    @Test
    void logoutBad() throws DataAccessException {
        LoginRequest loginRequest = new LoginRequest("username1", "password2");
        LoginResponse loginResponse = userService.login(loginRequest);

        LogoutRequest logoutRequest = new LogoutRequest();
        String authToken = "";

        assertThrows(DataAccessException.class, () -> userService.logout(logoutRequest, authToken));
    }
}
