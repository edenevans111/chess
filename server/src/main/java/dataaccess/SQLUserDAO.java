package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

import static java.sql.Types.NULL;

public class SQLUserDAO extends SQLDatabase implements UserDAO{

    private final String tableName = "userData";

    public SQLUserDAO() {
        try {
            configureDatabase(createStatements);
        } catch (DataAccessException e) {
            System.out.println("Error: SQLUserDAO failed to make database");
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
            throw new DataAccessException(e.getMessage());
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
}
