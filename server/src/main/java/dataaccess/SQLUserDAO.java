package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

import static java.sql.Types.NULL;

public class SQLUserDAO implements UserDAO{

    public SQLUserDAO() {
        try {
            configureDatabase();
        } catch (DataAccessException e) {
            System.out.println("SQLAuthDAO failed to make database");
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

    private void configureDatabase() throws DataAccessException{
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()){
            for(var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)){
                    preparedStatement.executeUpdate();
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Error: unable to configure User database: %s", e.getMessage()));
        }
    }

    // I think I will need to find a better way to manage this function later on...
    private void executeUpdate(String statement, Object... parameters) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var ps = conn.prepareStatement(statement);
            for (int i = 0; i < parameters.length; i++) {
                Object param = parameters[i];
                if (param == null){
                    ps.setNull(i + 1, NULL);
                } else if (param instanceof String p) {
                    ps.setString(i + 1, p);
                }
                else if (param instanceof Integer p) {
                    ps.setInt(i + 1, p);
                } else {
                    throw new DataAccessException("Error: Wrong type entered: " + param.getClass());
                }
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException( String.format("Error: unable to update User database: %s, %s", statement, e.getMessage()));
        }
    }
}
