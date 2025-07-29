package ui;

import dataaccess.DataAccessException;
import request.RegisterRequest;
import response.LoginResponse;
import response.RegisterResponse;
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
        System.out.print("Register: supply username, password, email to create an account\n");
        System.out.print("Login: username and password to login to account\n");
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
        } else {
            LoginRequest request = new LoginRequest(username, password);
            LoginResponse response = serverFacade.login(request);
            loginString.append("Congratulations, you are logged in as " + response.username());
        }
        return loginString.toString();
    }

    public String register(String [] args) throws DataAccessException {
        String username = args[1];
        String password = args[2];
        String email = args[3];
        StringBuilder registerString = new StringBuilder();

        if(username.isBlank()){
            registerString.append("No username given");
        } else if (password.isBlank()){
            registerString.append("No password was given");
        } else if (email.isBlank()){
            registerString.append("No email was given");
        } else {
            // I might want to change this to a try and catch thing later on, so that I can better handle the errors
            RegisterRequest request = new RegisterRequest(username, password, email);
            RegisterResponse response = serverFacade.register(request);
            registerString.append("Welcome! You are now signed in as " + response.username());
        }
        return registerString.toString();
    }
    




}
