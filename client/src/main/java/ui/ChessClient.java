package ui;

import dataaccess.DataAccessException;
import response.LoginResponse;
import server.*;
import request.LoginRequest;

public class ChessClient {
    // handles all the logic for the Repl parameters
    // validation of the input
    private String serverUrl;
    private ServerFacade serverFacade;

    // I need to ask about how to make the server.ServerFacade object
    // I cannot figure out how to make it correctly for whatever reason

    public ChessClient(String serverUrl){
        this.serverUrl = serverUrl;
        this.serverFacade = new server.ServerFacade(serverUrl);
    }

    public void preloginHelp(){
        System.out.print("Register: supply username, password, email\n");
        System.out.print("Login: username and password\n");
        System.out.print("Quit: exit the program\n");
    }

    public String login(String [] args) throws DataAccessException {
        String username = args[1];
        String password = args[2];
        StringBuilder loginString = new StringBuilder();
        if(password.isBlank()){
            loginString.append("No password was given");
        } else if (username.isBlank()){
            loginString.append("No username was given");
        }
        LoginRequest request = new LoginRequest(username, password);
        LoginResponse response = serverFacade.login(request);
        loginString.append("Congratulations, you are logged in as " + response.username());
        return loginString.toString();
    }

    //




}
