package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDAOTest {

    SQLAuthDAO authDAO = new SQLAuthDAO();

    @BeforeEach
    void setUp() throws DataAccessException {
        authDAO.clear();
    }

    // I need a positive test for clear
    @Test
    void clear() throws DataAccessException {
        AuthData authData = new AuthData("ValidAuthToken", "user1");
        authDAO.createAuth(authData);

        authDAO.clear();
        assertTrue(authDAO.isEmpty());
    }

    // positive and negative test for createAuth
    @Test
    void createAuthPositive() throws DataAccessException {
        AuthData authData = new AuthData("RealAuthToken", "LegitUser");
        authDAO.createAuth(authData);
        assertEquals("LegitUser", authData.username());
    }

    @Test
    void createAuthNegative() throws DataAccessException {

    }

    // positive and negative test for getAuth
    @Test
    void getAuthPositive() throws DataAccessException {
        AuthData authData = new AuthData("AnotherAuthToken", "LegitUser");
        authDAO.createAuth(authData);
        AuthData authData1 = authDAO.getAuth("AnotherAuthToken");
        assertEquals("LegitUser", authData1.username());
    }

    @Test
    void getAuthNegative() throws DataAccessException {
        // try to get an authToken that does not exist
    }

    // positive and negative test for deleteAuth
    @Test
    void deleteAuthPositive() throws DataAccessException {
        AuthData authData = new AuthData("TotallyRealAuthToken", "User1");
        authDAO.createAuth(authData);
        authDAO.deleteAuth("TotallyRealAuthToken");
        assertNull(authDAO.getAuth("TotallyRealAuthToken"));

    }

    @Test
    void deleteAuthNegative() throws DataAccessException {
        // try to delete an authData that does not exist
        assertThrows(DataAccessException.class, ()->
                authDAO.deleteAuth("FakeAuth"));
    }
}
