package dataaccess;

import model.dataaccess.UserData;

import java.util.HashSet;


public class MemoryUserDAO implements UserDAO{

    private HashSet<UserData> userDataSet;

    public MemoryUserDAO(){
        userDataSet = new HashSet<>();
    }

    @Override
    public void clear() throws DataAccessException{
        userDataSet.clear();
    }

    @Override
    public void createUser(UserData u) throws DataAccessException {
        UserData user = new UserData(u.username(), u.password(), u.email());
        userDataSet.add(user);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        for(UserData user : userDataSet){
            if(user.username().equals(username)){
                return user;
            }
        }
        throw new DataAccessException("Did not find username");
    }
}
