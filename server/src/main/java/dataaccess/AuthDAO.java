package dataaccess;


import model.dataaccess.AuthData;

// for this I think, once it has all the methods that will be needed, I will need to create
// a AuthMemoryDataAccess class that implements this interface in Memory, then later we'll add a SQLDataAccess class
public interface AuthDAO {
    void clear() throws DataAccessException;
    void createAuth(AuthData authData) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
}
