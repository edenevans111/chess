package dataaccess;

import model.dataaccess.UserData;


public class MemoryUserDAO implements UserDAO{
    @Override
    public void clear() throws DataAccessException{

    }

    @Override
    public void createUser(UserData u) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }
}
