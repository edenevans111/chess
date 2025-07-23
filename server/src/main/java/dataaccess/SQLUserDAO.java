package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

import static java.sql.Types.NULL;

public class SQLUserDAO extends SQLDatabase implements UserDAO{

    public SQLUserDAO() {
        try {
            configureDatabase(createStatements);
        } catch (DataAccessException e) {
            System.out.println("SQLUserDAO failed to make database");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String statement = "TRUNCATE TABLE userData";
        executeUpdate(statement);
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        var statement = "INSERT INTO userData (username, password, email) VALUES (?, ?, ?)";
        // I think here I need to encrypt the password so that it is better protected...
        String encryptedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
        executeUpdate(statement, userData.username(), encryptedPassword, userData.email());
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        String statement = "SELECT * FROM userData WHERE username=?";
        try (var conn = DatabaseManager.getConnection()) {
            var ps = conn.prepareStatement(statement);
            ps.setString(1, username);
            try (var rs = ps.executeQuery()){
                if(rs.next()){
                    return readUser(rs);
                }
            }
            throw new DataAccessException("Error: unauthorized");
        } catch (Exception e) {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var password = rs.getString("password");
        String email = rs.getString("email");
        return new UserData(username, password, email);
    }

    private final String[] createStatements = {
            """
           CREATE TABLE IF NOT EXISTS userData (
            username VARCHAR(255) NOT NULL PRIMARY KEY,
            password VARCHAR(255) NOT NULL,
            email VARCHAR(255) NOT NULL
            )"""
    };

    public boolean isEmpty() throws DataAccessException{
        return true;
    }
}
