package dataaccess;

public interface UserDAO {
    void clear() throws DataAccessException;

    Object registerUser(String username, String password, String email) throws DataAccessException;

    Object loginUser(String username, String password) throws DataAccessException;
}
