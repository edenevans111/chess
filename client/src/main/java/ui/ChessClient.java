package ui;

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

    public void help(){
        System.out.print("Register: supply username, password, email\n");
        System.out.print("Login: username and password\n");
        System.out.print("Quit: exit the program\n");
    }

    public String login(String [] args){
        String username = args[1];
        String password = args[2];
        StringBuilder loginString = new StringBuilder();
        if(password.isBlank()){
            loginString.append("No password was given");
        } else if (username.isBlank()){
            loginString.append("No username was given");
        }
        LoginRequest request = new LoginRequest(username, password);
        // now I need to call the server.ServerFacade and pass the request in
        // now I need to make a LoginRequest and call the server.ServerFacade

        return loginString.toString();
    }


}
