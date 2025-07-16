package dataaccess;

import model.dataaccess.AuthData;

public class MemoryAuthDAO implements AuthDAO{

    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public void createAuth() throws DataAccessException {

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }
}
