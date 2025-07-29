package ui;

import dataaccess.DataAccessException;
import request.LogoutRequest;
import request.RegisterRequest;
import response.LoginResponse;
import response.RegisterResponse;
import server.*;
import request.LoginRequest;

import java.util.Arrays;
import java.util.Collection;

public class ChessClient {
    // handles all the logic for the Repl parameters
    // validation of the input
    private String serverUrl;
    private ServerFacade serverFacade;
    private boolean isLoggedIn = false;

    // I need to ask about how to make the server.ServerFacade object
    // I cannot figure out how to make it correctly for whatever reason

    public ChessClient(String serverUrl){
        this.serverUrl = serverUrl;
        this.serverFacade = new server.ServerFacade(serverUrl);
    }

    public String eval(String line) throws DataAccessException {
        String [] args = line.toLowerCase().split(" ");
        String command = (args.length > 0) ? args[0] : "help";
        var parameters = Arrays.copyOfRange(args, 1, args.length);
        return(switch(command){
            case "login" -> login(parameters);
            case "register" -> register(parameters);
            case "logout" -> logout();
            default -> help();
        });
    }

    public String help(){

        StringBuilder helpString = new StringBuilder();

        if(!isLoggedIn){
            helpString.append("Register: supply username, password, email to create an account\n");
            helpString.append("Login: username and password to login to account\n");
            helpString.append("Quit: exit the program\n");
        } else {
            helpString.append("Logout\n");
            helpString.append("Create: supply name for a new chess game \n");
            helpString.append("List: get a list of all the current games\n");
            helpString.append("Play: request to play a certain game using the game ID\n");
            helpString.append("Observe: indicate a game you would wish to watch\n");
        }

        return helpString.toString();
    }

    public String login(String [] args) throws DataAccessException {
        String username = args[0];
        String password = args[1];
        StringBuilder loginString = new StringBuilder();
        if(password.isBlank()){
            loginString.append("No password was given");
        } else if (username.isBlank()){
            loginString.append("No username was given");
        } else {
            LoginRequest request = new LoginRequest(username, password);
            LoginResponse response = serverFacade.login(request);
            loginString.append("Congratulations, you are logged in as " + response.username());
            isLoggedIn = true;
        }
        return loginString.toString();
    }

    public String register(String [] args) throws DataAccessException {
        if(args.length < 3){
            System.out.println("Not enough information was given");
        }
        String username = args[0];
        String password = args[1];
        String email = args[2];
        StringBuilder registerString = new StringBuilder();

        if(username.isBlank()){
            registerString.append("No username given");
        } else if (password.isBlank()){
            registerString.append("No password was given");
        } else if (email.isBlank()){
            registerString.append("No email was given");
        } else {
            RegisterRequest request = new RegisterRequest(username, password, email);
            RegisterResponse response = serverFacade.register(request);
            registerString.append("Welcome! You are now signed in as " + response.username());
            isLoggedIn = true;
        }
        return registerString.toString();
    }

    private String logout() throws DataAccessException {
        StringBuilder logoutString = new StringBuilder();

        LogoutRequest request = new LogoutRequest();
        serverFacade.logout(request);
        logoutString.append("You are now logged out");

        return logoutString.toString();
    }



}
