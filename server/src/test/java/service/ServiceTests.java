package service;
import chess.InvalidMoveException;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.dataaccess.AuthData;
import model.dataaccess.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ServiceTests {

    @Test
    void clear() throws InvalidMoveException, DataAccessException {
        // create all the DAOs
        MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();

        // add data into the DAOs
        UserData user = new UserData("username1", "password2", "emmail3@gmail.com");
        memoryUserDAO.createUser(user);
        String authToken = "thisIsAnAuthToken";
        AuthData authData = new AuthData(authToken, "username2");
        memoryAuthDAO.createAuth(authData);
        memoryGameDAO.createGame("whiteUsername1",
                "blackUsername1", "BestChessGame");

        Service service = new Service(memoryUserDAO, memoryAuthDAO, memoryGameDAO);
        service.clearEverything();

        assertTrue(memoryUserDAO.isEmpty());
        assertTrue(memoryAuthDAO.isEmpty());
        assertTrue(memoryGameDAO.listGames().isEmpty());

    }
}
