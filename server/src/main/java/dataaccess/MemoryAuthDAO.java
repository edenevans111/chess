package dataaccess;

import model.AuthData;

import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{

    private HashSet<AuthData> authDataSet;

    public MemoryAuthDAO(){
        this.authDataSet = new HashSet<>();
    }

    @Override
    public void clear() throws DataAccessException {
        authDataSet.clear();
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        AuthData authData1 = new AuthData(authData.authToken(), authData.username());
        authDataSet.add(authData1);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        for(AuthData authData : authDataSet){
            if(authData.authToken().equals(authToken)){
                return authData;
            }
        }
        // this should be the correct error message now
        throw new DataAccessException("Error: unauthorized");
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        Iterator<AuthData> iterator = authDataSet.iterator();
        while(iterator.hasNext()){
            AuthData authData = iterator.next();
            if(authData.authToken().equals(authToken)){
                iterator.remove();
                return;
            }
        }
        throw new DataAccessException("Error: unauthorized");
    }

    public boolean isEmpty() {
        return authDataSet.isEmpty();
    }
}
