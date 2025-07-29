package ui;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.GameData;
import request.*;
import response.*;
import serverfacade.ServerFacade;

import java.util.Arrays;

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
        this.serverFacade = new ServerFacade(serverUrl);
    }

    public String eval(String line) throws DataAccessException {
        String [] args = line.toLowerCase().split(" ");
        String command = (args.length > 0) ? args[0] : "help";
        var parameters = Arrays.copyOfRange(args, 1, args.length);
        return(switch(command){
            case "login" -> login(parameters);
            case "register" -> register(parameters);
            case "logout" -> logout();
            case "create" -> createGame(parameters);
            case "list" -> listGames();
            case "join" -> joinGame(parameters);
            case "observe" -> observeGame(parameters);
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

    private String createGame(String [] args) throws DataAccessException {
        StringBuilder createString = new StringBuilder();

        if(args.length < 1){
            createString.append("Need to supply a game name");
        }
        String gameName = args[0];
        CreateRequest request = new CreateRequest(gameName);
        CreateResponse response = serverFacade.createGame(request);
        createString.append("You have successfully created game: " + gameName);
        return createString.toString();
    }

    private String listGames() throws DataAccessException {
        StringBuilder listString = new StringBuilder();
        ListRequest request = new ListRequest();
        ListResponse response = serverFacade.listGames(request);
        for(GameData game : response.games()){
            listString.append(game.gameID() + " : " + game.gameName() + "\n");
        }
        return listString.toString();
    }

    private String joinGame(String [] args) throws DataAccessException {
        StringBuilder joinString = new StringBuilder();

        if(args.length < 2){
            joinString.append("Not enough information provided");
        } else {
            ChessGame.TeamColor teamColor;
            if(args[0].toLowerCase().equals("white")){
                teamColor = ChessGame.TeamColor.WHITE;
                BoardDisplay display = new ChessBoardPrinter();
                ChessGame game = new ChessGame();
                display.displayWhiteBoard(game);
            } else {
                teamColor = ChessGame.TeamColor.BLACK;
                BoardDisplay display = new ChessBoardPrinter();
                ChessGame game = new ChessGame();
                display.displayBlackBoard(game);
            }
            int gameID = Integer.parseInt(args[1]);
            JoinRequest request = new JoinRequest(teamColor, gameID);
            JoinResponse response = serverFacade.join(request);
            joinString.append("Now joining game" + gameID);
        }
        return joinString.toString();
    }

    private String observeGame(String [] args) throws DataAccessException {
        StringBuilder observeString = new StringBuilder();

        if(args.length < 1){
            observeString.append("Need to specify a game");
        } else {
            int gameID = Integer.parseInt(args[0]);
            JoinRequest request = new JoinRequest(null, gameID);
            JoinResponse response = serverFacade.join(request);
            observeString.append("You are now joining game: " + gameID);
            BoardDisplay boardDisplay = new ChessBoardPrinter();
            ChessGame game = new ChessGame();
            boardDisplay.displayWhiteBoard(game);
        }

        return observeString.toString();
    }

}
