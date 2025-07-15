package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class Service {
    public void clearEverything() throws DataAccessException {
        GameDAO gameDAO = null; // eventually these won't be null...I don't know exactly how that will go down though
        UserDAO userDAO = null;
        AuthDAO authDAO = null;
        authDAO.clear();
        userDAO.clear();
        gameDAO.clear();
    }

}
