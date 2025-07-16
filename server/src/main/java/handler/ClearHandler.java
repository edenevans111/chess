package handler;

import com.google.gson.Gson;
import dataaccess.*;
import request.*;
import response.*;
import service.*;


public class ClearHandler {

    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public ClearHandler(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public String clearEverything() throws DataAccessException{
        var serializer = new Gson();

        Service clearService = new Service(userDAO, authDAO, gameDAO);
        ClearResponse clearResult = clearService.clearEverything(new ClearRequest());
        String result = serializer.toJson(clearResult);
        return result;
    }
}
