package service;

import dataaccess.*;
import model.dataaccess.*;
import request.*;
import response.*;

import java.util.UUID;

public class UserService {

    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public RegisterResponse register(RegisterRequest registerRequest) throws DataAccessException {
        String username = registerRequest.username();
        if(userDAO.getUser(username) == null){
            String password = registerRequest.password();
            UserData userData = new UserData(username, password, registerRequest.email());
            userDAO.createUser(userData);
            // now we need to give them an authToken
            String authToken = UUID.randomUUID().toString();
            // then create authData and add them to the authDAO
            AuthData authData = new AuthData(authToken, username);
            authDAO.createAuth(authData);
            // create RegisterResult with the username and authToken
            return new RegisterResponse(username, authToken);
        } else{
            throw new DataAccessException("Error: already taken");
        }
    }

    public LoginResponse login(LoginRequest loginRequest) throws DataAccessException{
        String username = loginRequest.username();
        String password = loginRequest.password();
        UserData userData = userDAO.getUser(username);
        if(userData.username() != null) {
            if (userData.password() == password) {
                String authToken = UUID.randomUUID().toString();
                AuthData authdata = new AuthData(authToken, username);
                authDAO.createAuth(authdata);
                return new LoginResponse(username, authToken);
            } else {
                throw new DataAccessException("wrong password");
            }
        } else{
            throw new DataAccessException("username not found");
        }
    }

    public void logout(LogoutRequest logoutRequest, String authToken) throws DataAccessException{
        AuthData authData = authDAO.getAuth(authToken);
        if(authData.authToken() != null){
            authDAO.deleteAuth(authToken);
        } else {
            throw new DataAccessException("authToken is not valid");
        }
    }


}
