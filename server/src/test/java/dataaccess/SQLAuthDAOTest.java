package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDAOTest {

    private static SQLAuthDAO authDAO;

    // Setup before all tests (clean DB)
    @BeforeAll
    static void setUp() throws DataAccessException {
        authDAO = new SQLAuthDAO();
        authDAO.clear();
    }

    // Clear the table before each test for isolation
    @BeforeEach
    void clearTable() throws DataAccessException {
        authDAO.clear();
    }

    // ---- Positive test for clear() ----
    @Test
    @DisplayName("clear() removes all rows without error")
    void clear_RemovesAllRows() throws DataAccessException {
        var authToken = UUID.randomUUID().toString();
        var username = "user1";
        authDAO.createAuth(new AuthData(authToken, username));

        authDAO.clear();

        // After clear, getAuth should return null
        assertNull(authDAO.getAuth(authToken));
    }

    // ---- Positive test for createAuth() and getAuth() ----
    @Test
    @DisplayName("createAuth() inserts and getAuth() retrieves the data")
    void createAndGetAuth_Success() throws DataAccessException {
        var authToken = UUID.randomUUID().toString();
        var username = "user2";

        AuthData authData = new AuthData(authToken, username);
        authDAO.createAuth(authData);

        AuthData retrieved = authDAO.getAuth(authToken);
        assertNotNull(retrieved);
        assertEquals(authToken, retrieved.authToken());
        assertEquals(username, retrieved.username());
    }

    // ---- Negative test for getAuth() with missing authToken ----
    @Test
    @DisplayName("getAuth() returns null for nonexistent authToken")
    void getAuth_NonexistentToken_ReturnsNull() throws DataAccessException {
        String fakeToken = UUID.randomUUID().toString();
        AuthData result = authDAO.getAuth(fakeToken);
        assertNull(result);
    }

    // ---- Positive test for deleteAuth() ----
    @Test
    @DisplayName("deleteAuth() deletes existing auth record")
    void deleteAuth_Success() throws DataAccessException {
        var authToken = UUID.randomUUID().toString();
        var username = "user3";

        authDAO.createAuth(new AuthData(authToken, username));
        // Should delete without error
        authDAO.deleteAuth(authToken);

        // Confirm deletion
        assertNull(authDAO.getAuth(authToken));
    }

    // ---- Negative test for deleteAuth() non-existent authToken -> throws DataAccessException ----
    @Test
    @DisplayName("deleteAuth() throws DataAccessException for invalid authToken")
    void deleteAuth_NonexistentToken_Throws() {
        String fakeToken = UUID.randomUUID().toString();

        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            authDAO.deleteAuth(fakeToken);
        });

        assertTrue(exception.getMessage().contains("Error: unauthorized"));
    }

    // ---- Negative test for createAuth() with null/invalid parameters (optional) ----
    // Note: You may want to test how it behaves with nulls or malformed input.
    // Depending on your DAO and DB constraints, this may throw SQLException wrapped in DataAccessException.

    @Test
    @DisplayName("createAuth() null authToken throws DataAccessException")
    void createAuth_NullAuthToken_Throws() {
        AuthData invalid = new AuthData(null, "someuser");
        assertThrows(DataAccessException.class, () -> authDAO.createAuth(invalid));
    }

    @Test
    @DisplayName("createAuth() null username throws DataAccessException")
    void createAuth_NullUsername_Throws() {
        AuthData invalid = new AuthData("token", null);
        assertThrows(DataAccessException.class, () -> authDAO.createAuth(invalid));
    }
}
