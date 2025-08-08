package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import model.GameData;
import request.*;
import response.*;
import serverfacade.ResponseException;
import serverfacade.ServerFacade;
import websocket.WebSocketFacade;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

public class ChessClient {
    // handles all the logic for the Repl parameters
    // validation of the input
    private String serverUrl;
    private ServerFacade serverFacade;
    private boolean isLoggedIn = false;
    private boolean inGameplay = false;
    private boolean isObserver = false;
    private boolean displayWhite = true;
    private GameData gameData = null;
    private String authToken = null;
    private String username = null;
    private WebSocketFacade wsf;

    public ChessClient(String serverUrl) {
        this.serverUrl = serverUrl;
        this.serverFacade = new ServerFacade(serverUrl);
        BoardDisplay boardDisplay = new ChessBoardPrinter();
        try {
            this.wsf = new WebSocketFacade(serverUrl, boardDisplay);
        } catch (ResponseException e) {
            throw new RuntimeException("Unable to make the WebSocketFacade");
        }
    }

    public String eval(String line) throws ResponseException {
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
            case "leave" -> leave();
            case "redraw" -> redrawChessBoard();
            case "move" -> makeMove(parameters);
            default -> help();
        });
    }

    public String help(){

        StringBuilder helpString = new StringBuilder();

        if(!isLoggedIn){
            helpString.append("Register: supply username, password, email to create an account\n");
            helpString.append("Login: username and password to login to account\n");
            helpString.append("Quit: exit the program\n");
            helpString.append("Help: commands that can be given\n");
        } else if (!inGameplay && !isObserver){
            helpString.append("Logout\n");
            helpString.append("Create: supply name for a new chess game (name must be one word) \n");
            helpString.append("List: get a list of all the current games\n");
            helpString.append("Join: request to play a certain game using the game ID\n");
            helpString.append("Observe: indicate a game you would wish to watch\n");
            helpString.append("Help: commands that can be given\n");
        } else if (inGameplay){
            helpString.append("Redraw Chess Board: redraws the chess board\n");
            helpString.append("Leave: leave the game without ending it\n");
            helpString.append("Move: indicate the start position and the desired end position\n");
            helpString.append("Resign: forfeit the game, the other player wins\n");
            helpString.append("Highlight: highlights all the possible moves\n");
            helpString.append("Help: list of all the possible actions\n");
        } else {
            helpString.append("Leave: leave the game\n");
            helpString.append("Help: list of all the possible actions\n");
        }
        return helpString.toString();
    }

    public String login(String [] args) throws ResponseException {
        StringBuilder loginString = new StringBuilder();
        if(args.length != 2){
            loginString.append("Error, wrong information given: need username and password");
        } else {
            String username = args[0];
            String password = args[1];

            if(password.isBlank()){
                loginString.append("No password was given");
            } else if (username.isBlank()){
                loginString.append("No username was given");
            } else {
                try{
                    LoginRequest request = new LoginRequest(username, password);
                    LoginResponse response = serverFacade.login(request);
                    loginString.append("Congratulations, you are logged in as " + response.username());
                    isLoggedIn = true;
                    this.authToken = response.authToken();
                    this.username = response.username();
                } catch (ResponseException e){
                    loginString.append("Invalid Login given");
                }

            }
        }

        return loginString.toString();
    }

    public String register(String [] args) throws ResponseException {
        StringBuilder registerString = new StringBuilder();
        if(args.length != 3){
            registerString.append("Wrong information given");
        } else {
            String username = args[0];
            String password = args[1];
            String email = args[2];

            if(username.isBlank()){
                registerString.append("No username given");
            } else if (password.isBlank()){
                registerString.append("No password was given");
            } else if (email.isBlank()){
                registerString.append("No email was given");
            } else {
                try{
                    RegisterRequest request = new RegisterRequest(username, password, email);
                    RegisterResponse response = serverFacade.register(request);
                    registerString.append("Welcome! You are now signed in as " + response.username());
                    isLoggedIn = true;
                    this.authToken = response.authToken();
                    this.username = response.username();
                } catch (ResponseException e){
                    registerString.append("Unable to register");
                }
            }
        }
        return registerString.toString();
    }

    private String logout() throws ResponseException {
        StringBuilder logoutString = new StringBuilder();

        LogoutRequest request = new LogoutRequest();
        serverFacade.logout(request);
        logoutString.append("You are now logged out");
        isLoggedIn = false;
        return logoutString.toString();
    }

    private String createGame(String [] args) throws ResponseException {
        StringBuilder createString = new StringBuilder();

        if(args.length < 1){
            createString.append("Need to supply a game name (name must be one word) ");
        } else {
            try{
                String gameName = args[0];
                CreateRequest request = new CreateRequest(gameName);
                CreateResponse response = serverFacade.createGame(request);
                createString.append("You have successfully created game: " + gameName);
            } catch (ResponseException e){
                createString.append("Unable to create game");
            }
        }
        return createString.toString();
    }

    private String listGames() throws ResponseException {
        StringBuilder listString = new StringBuilder();
        ListRequest request = new ListRequest();
        ListResponse response = serverFacade.listGames(request);
        for(GameData game : response.games()){
            listString.append(game.gameID() + " : " + game.gameName() + "White Player: "
                    + game.whiteUsername() + "Black Player: " + game.blackUsername() + "\n");
        }
        return listString.toString();
    }

    private String joinGame(String [] args) throws ResponseException {
        StringBuilder joinString = new StringBuilder();
        if(inGameplay || isObserver){
            joinString.append("You are already part of another game");
            return joinString.toString();
        }
        if(args.length < 2){
            joinString.append("Not enough information provided");
        } else {
            ChessGame.TeamColor teamColor;
            if(args[0].toLowerCase().equals("white")){
                teamColor = ChessGame.TeamColor.WHITE;
            } else {
                teamColor = ChessGame.TeamColor.BLACK;
            }
            try{
                int gameID = Integer.parseInt(args[1]);
                JoinRequest request = new JoinRequest(teamColor, gameID);
                JoinResponse response = serverFacade.join(request);
                joinString.append("Now joining game " + gameID);

                ChessGame game = null;
                ListRequest listRequest = new ListRequest();
                ListResponse listResponse = serverFacade.listGames(listRequest);
                Collection<GameData> games = listResponse.games();
                for (GameData gameData : games){
                    if(gameData.gameID() == gameID){
                        game = gameData.game();
                        this.gameData = gameData;
                    }
                }
                BoardDisplay display = new ChessBoardPrinter();
                if(teamColor == ChessGame.TeamColor.WHITE){
                    display.displayWhiteBoard(game);
                    displayWhite = true;
                } else{
                    display.displayBlackBoard(game);
                    displayWhite = false;
                }
            } catch (ResponseException e){
                joinString.append("Unable to join game");
            } catch (NumberFormatException e){
                joinString.append("Need to give number");
            }
        }
        wsf.joinGame(username, authToken, gameData.gameID());
        inGameplay = true;
        return joinString.toString();
    }

    private String observeGame(String [] args) throws ResponseException {
        StringBuilder observeString = new StringBuilder();
        if(inGameplay || isObserver){
            observeString.append("You are already part of another game");
            return observeString.toString();
        }
        if(args.length != 1){
            observeString.append("Need to specify a game");
        } else {
            try{
                int gameID = Integer.parseInt(args[0]);
                ListRequest listRequest = new ListRequest();
                ListResponse listResponse = serverFacade.listGames(listRequest);
                Collection<GameData> games = listResponse.games();
                boolean hasGame = false;
                ChessGame actualGame = null;
                for(GameData game : games){
                    if (game.gameID() == gameID){
                        hasGame = true;
                        actualGame = game.game();
                        break;
                    }
                }
                if(hasGame){
                    observeString.append("You are now joining game: " + gameID);
                    BoardDisplay boardDisplay = new ChessBoardPrinter();
                    boardDisplay.displayWhiteBoard(actualGame);
                    isObserver = true;
                } else {
                    observeString.append("Game was not found");
                }
            } catch (NumberFormatException e){
                observeString.append("need to give a number");
            }
        }
        wsf.joinGame(username, authToken, gameData.gameID());
        isObserver = true;
        return observeString.toString();
    }

    public String leave() throws ResponseException {
        if(!isObserver && !inGameplay){
            throw new RuntimeException("You need to be in a game to leave");
        }
        try {
            wsf.leaveGame(username, authToken, gameData.gameID());
            String response = String.format("Now leaving game %d", gameData.gameID());
            isObserver = false;
            inGameplay = false;
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Unable to leave game");
        }
    }


    public String redrawChessBoard() throws ResponseException {
        BoardDisplay boardDisplay = new ChessBoardPrinter();
        if(!isLoggedIn){
            throw new ResponseException("You need to be signed in to redraw board");
        }
        if (!inGameplay && !isObserver){
            throw new ResponseException("You need to join a game to redraw chess board");
        }
        if(displayWhite){
            boardDisplay.displayWhiteBoard(gameData.game());
        } else {
            boardDisplay.displayBlackBoard(gameData.game());
        }
        return String.format("Redrew game %d", gameData.gameID());
    }

    public String makeMove(String [] args){
        StringBuilder moveString = new StringBuilder();
        if(!inGameplay){
            moveString.append("You must be a player to make a move");
            return moveString.toString();
        }
        if(args.length != 2){
            moveString.append("Need a start and end position");
            return moveString.toString();
        }
        try {
            ChessPosition startPosition = stringToPosition(args[0].toString());
            ChessPosition endPosition = stringToPosition(args[1].toString());
            ChessMove move = new ChessMove(startPosition, endPosition);
            moveString.append("Moving piece from " + args[0].toString() + " to " + args[1].toString());
            wsf.makeMove(username, authToken, gameData.gameID(), move);
        } catch (Exception e){
            moveString.append("Invalid position given");
        }
        return moveString.toString();
    }

    private ChessPosition stringToPosition(String stringPosition) throws Exception {
        char[] validLetter = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' };
        char first = Character.toLowerCase(stringPosition.charAt(0));
        char second = Character.toLowerCase(stringPosition.charAt(1));
        int row = Character.getNumericValue(second);
        int counter = 0;
        int column = 0;
        for (char c : validLetter){
            counter++;
            if(c == first){
                column = counter;
            }
        }
        if(column == 0){
            throw new Exception("column letter needs to be between a-h");
        }
        if (row > 8 || row < 1){
            throw new Exception("row needs to be between 1-8");
        }
        return new ChessPosition(row, column);
    }

    public String resign(){
        StringBuilder resignString = new StringBuilder();
        System.out.print("Are you sure you want to resign? (yes or no): ");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();
        if(answer.equals("yes")){

        }
        return resignString.toString();
    }

}
