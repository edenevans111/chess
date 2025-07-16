package dataaccess;

import model.dataaccess.AuthData;

import java.util.HashSet;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{

    private HashSet<AuthData> authDataSet;

    public MemoryAuthDAO(){
        authDataSet = new HashSet<>();
    }

    public static String generateToken(){
        return UUID.randomUUID().toString();
    }

    @Override
    public void clear() throws DataAccessException {
        authDataSet.clear();
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        // create a new AuthData Object with all the new information
        AuthData authData1 = new AuthData(authData.authToken(), authData.username());
        authDataSet.add(authData1);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        for(AuthData authData : authDataSet){
            if(authData.authToken() == authToken){
                return authData;
            }
        }
        throw new DataAccessException("Authorization Code does not exist");
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        for(AuthData authData : authDataSet){
            if(authData.authToken() == authToken){
                authDataSet.remove(authData);
            }
        }
        throw new DataAccessException("Did not find authToken");
    }
}
