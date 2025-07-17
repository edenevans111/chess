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
        String password = registerRequest.password();
        String email = registerRequest.email();
        if(username == null || password == null || email == null){
            throw new DataAccessException("Error: bad request");
        }
        try{
            userDAO.getUser(username);
            throw new DataAccessException("Error: already taken");
        } catch(DataAccessException e){
            UserData userData = new UserData(username, password, email);
            userDAO.createUser(userData);

            String authToken = UUID.randomUUID().toString();
            AuthData authData = new AuthData(authToken, username);
            authDAO.createAuth(authData);
            return new RegisterResponse(username, authToken);
        }
    }

    public LoginResponse login(LoginRequest loginRequest) throws DataAccessException{
        String username = loginRequest.username();
        String password = loginRequest.password();
        UserData userData = userDAO.getUser(username);
            if (userData.password().equals(password)) {
                String authToken = UUID.randomUUID().toString();
                AuthData authdata = new AuthData(authToken, username);
                authDAO.createAuth(authdata);
                return new LoginResponse(username, authToken);
            } else{
                throw new DataAccessException("Error: unauthorized");
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
