package dataaccess;

import com.google.gson.Gson;
import model.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLAuthDAO implements AuthDAO{


    public SQLAuthDAO() {
        try {
            configureDatabase();
        } catch (DataAccessException e) {
            System.out.println("SQLAuthDAO failed to make database");
        }

    }

    @Override
    public void clear() throws DataAccessException {
        String statement = "TRUNCATE TABLE authData";
        executeUpdate(statement);
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO authData (authToken, username) VALUES (?, ?)";
        var id = executeUpdate(statement, authData.authToken(), authData.username());
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        // this is a problem for some reason that I can't quite figure out
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT * FROM authData WHERE authToken=?";
            try(var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try(var rs = ps.executeQuery()) {
                    if(rs.next()){
                        return readAuth(rs);
                    }
                }
            }

        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM authData WHERE authToken=?";
        executeUpdate(statement, authToken);
    }

    private int executeUpdate(String statement, Object... parameters) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < parameters.length; i++) {
                    var param = parameters[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param instanceof AuthDAO p) ps.setString(i + 1, p.toString());
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException( String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
           CREATE TABLE IF NOT EXISTS authData (
            authToken VARCHAR(255) NOT NULL,
            username VARCHAR(255) NOT NULL,
            PRIMARY KEY (authToken)
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
            throw new DataAccessException(String.format("unable to configure database: %s", e.getMessage()));
        }
    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        var authToken = rs.getString("authToken");
        var username = rs.getString("username");
        return new AuthData(authToken, username);
    }
}
