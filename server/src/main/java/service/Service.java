package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;

public class Service {
    public void clearEverything() throws DataAccessException {
        GameDAO gameDAO = null;
        gameDAO.clear();
        // do the other DAOs
    }

}
