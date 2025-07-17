package service;

import dataaccess.*;
import request.ClearRequest;
import response.ClearResponse;

public class Service {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public Service(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public void setUserDAO(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    public void setAuthDAO(AuthDAO authDAO){
        this.authDAO = authDAO;
    }

    public void setGameDAO(GameDAO gameDAO){
        this.gameDAO = gameDAO;
    }

    public ClearResponse clearEverything() throws DataAccessException {
        authDAO.clear();
        userDAO.clear();
        gameDAO.clear();
        return new ClearResponse();
    }
}
