package dataaccess;

import model.UserData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

public class SQLUserDAOTest {

    private static SQLUserDAO userDAO;

    @BeforeAll
    static void setUp() throws DataAccessException {
        userDAO = new SQLUserDAO();
        userDAO.clear();
    }

    @BeforeEach
    void clearBefore() throws DataAccessException {
        userDAO.clear();
    }

    // --- clear() ---

    @Test
    @DisplayName("clear() empties userData table")
    void clear_Positive() throws DataAccessException {
        var user = new UserData("testuser", "password123", "email@email.com");
        userDAO.createUser(user);
        userDAO.clear();
         assertTrue(userDAO.isEmpty());
    }


    @Test
    @DisplayName("createUser() inserts user with hashed password")
    void createUser_Positive() throws DataAccessException {
        var username = "newuser";
        var password = "mysecretpass";
        var email = "newuser@example.com";
        var user = new UserData(username, password, email);
        userDAO.createUser(user);

        UserData retrieved = userDAO.getUser(username);
        assertNotNull(retrieved);
        assertEquals(username, retrieved.username());
        assertEquals(email, retrieved.email());
        // Password should be hashed, so plain password should not equal stored hash
        assertNotEquals(password, retrieved.password());
        // Confirm hashed password matches original password using BCrypt
        assertTrue(org.mindrot.jbcrypt.BCrypt.checkpw(password, retrieved.password()));
    }

    @Test
    @DisplayName("createUser() throws DataAccessException on null username")
    void createUser_Negative_NullUsername() {
        var user = new UserData(null, "pass", "email@example.com");
        assertThrows(DataAccessException.class, () -> userDAO.createUser(user));
    }

    @Test
    @DisplayName("createUser() throws DataAccessException if username already exists")
    void createUser_Negative_DuplicateUsername() throws DataAccessException {
        String username = "duplicateUser";
        var user1 = new UserData(username, "pass1", "email1@example.com");
        userDAO.createUser(user1);

        var user2 = new UserData(username, "pass2", "email2@example.com");
        DataAccessException ex = assertThrows(DataAccessException.class, () -> userDAO.createUser(user2));
        assertTrue(ex.getMessage().toLowerCase().contains("error"));
    }

    // --- getUser() ---

    @Test
    @DisplayName("getUser() returns UserData for existing user")
    void getUser_Positive() throws DataAccessException {
        var user = new UserData("existingUser", "password", "user@example.com");
        userDAO.createUser(user);

        UserData retrieved = userDAO.getUser(user.username());
        assertNotNull(retrieved);
        assertEquals(user.username(), retrieved.username());
        assertEquals(user.email(), retrieved.email());
    }

    @Test
    @DisplayName("getUser() throws DataAccessException for non-existent user")
    void getUser_Negative_UserNotFound() {
        String fakeUsername = UUID.randomUUID().toString();
        DataAccessException ex = assertThrows(DataAccessException.class, () -> {
            userDAO.getUser(fakeUsername);
        });
        assertTrue(ex.getMessage().toLowerCase().contains("unauthorized"));
    }
}
