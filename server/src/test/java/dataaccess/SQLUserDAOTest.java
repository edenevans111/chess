package dataaccess;

import model.UserData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

public class SQLUserDAOTest {

    SQLUserDAO userDAO = new SQLUserDAO();

    @BeforeEach
    void setUp() throws DataAccessException {
        userDAO.clear();
    }

    // I need a positive test for clear
    @Test
    void clearPositive() throws DataAccessException {
        UserData userData = new UserData("username1", "LegitGoodPassword", "email@email.com");
        userDAO.createUser(userData);
        userDAO.clear();
        assertTrue(userDAO.isEmpty());
    }

    // positive and negative test for create
    @Test
    void createUserPositive() throws DataAccessException {
        UserData userData = new UserData("username2", "LegitGoodPassword", "email@email.com");
        userDAO.createUser(userData);
        assertEquals("username2", userData.username());
    }

    @Test
    void createUserNegative() throws DataAccessException {
        assertTrue(userDAO.isEmpty());
    }

    // positive and negative test for get
    @Test
    void getUserPositive() throws DataAccessException {
        UserData user = new UserData("newUsername", "GreatPassword", "crazy@email.com");
        userDAO.createUser(user);
        UserData data1 = userDAO.getUser("newUsername");
        assertNotNull(data1.username());
    }

    @Test
    void getUserNegative() throws DataAccessException {
        UserData userData = new UserData("nextUsername", "IHateThis", "stupid@class.com");
        userDAO.createUser(userData);
        assertThrows(DataAccessException.class, () ->
                userDAO.getUser("BadUsername"));
    }

}
