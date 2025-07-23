package dataaccess;

import java.sql.SQLException;

import static java.sql.Types.NULL;

public class SQLDatabase {


    void executeUpdate(String statement, Object... parameters) throws DataAccessException {
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

    void configureDatabase(String[] createStatements) throws DataAccessException{
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

    public boolean isEmpty(String tableName) throws DataAccessException{
        String statement = "SELECT COUNT(*) FROM " + tableName;
        try (var conn = DatabaseManager.getConnection()){
            var ps = conn.prepareStatement(statement);
            var rs = ps.executeQuery();
            if (rs.next()){
                int count = rs.getInt(1);
                return count == 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return false;
    }

}
