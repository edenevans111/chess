package service;

import dataaccess.*;
import model.dataaccess.UserData;

public class Service {
    public void clearEverything() throws DataAccessException {
        GameDAO gameDAO = null; // eventually these won't be null...I don't know exactly how that will go down though
        UserDAO userDAO = null;
        AuthDAO authDAO = null;
        authDAO.clear();
        userDAO.clear();
        gameDAO.clear();
    }

    // I think I want to implement like the register function here...
    // I am not at all sure how to do that with the stuff that's supposed to come back from the handler

}
