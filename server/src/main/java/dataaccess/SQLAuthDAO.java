package dataaccess;

import model.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLAuthDAO extends SQLDatabase implements AuthDAO{

    private final String tableName = "authData";

    public SQLAuthDAO() {
        try {
            configureDatabase(createStatements);
        } catch (DataAccessException e) {
            // maybe this needs to throw an error?
            // but if I have it throw an error, then other problem pop up...
            System.out.println("Error: failed to make database");
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
        executeUpdate(statement, authData.authToken(), authData.username());
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
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
            throw new DataAccessException("Error: "+e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        AuthData authData = getAuth(authToken);
        if(authData == null){
            throw new DataAccessException("Error: unauthorized");
        }
        var statement = "DELETE FROM authData WHERE authToken=?";
        executeUpdate(statement, authToken);
    }

    private final String[] createStatements = {
            """
           CREATE TABLE IF NOT EXISTS authData (
            authToken VARCHAR(255) NOT NULL,
            username VARCHAR(255) NOT NULL,
            PRIMARY KEY (authToken)
            )"""
    };

    private AuthData readAuth(ResultSet rs) throws SQLException {
        var authToken = rs.getString("authToken");
        var username = rs.getString("username");
        return new AuthData(authToken, username);
    }


    public boolean isEmpty() throws DataAccessException {
        return true;
    }
}
