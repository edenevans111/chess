package server.websocket;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.management.Notification;
import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final GameDAO gameDAO;
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameService gameService;

    public WebSocketHandler(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO){
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        try {
            this.gameService = new GameService(userDAO, authDAO, gameDAO);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        String authToken = command.getAuthToken();
        AuthData authData = authDAO.getAuth(authToken);
        String username = authData.username();

        switch(command.getCommandType()){
            case CONNECT -> connect(username, command.getGameID(), session);
            case MAKE_MOVE -> makeMove(username, command.getGameID());
            case LEAVE -> leaveGame(username, command.getGameID());
            case RESIGN -> resign(username, command.getGameID());
        }
    }

    public void connect(String username, int gameID, Session session) throws IOException {
        GameData gameData = null;
        try {
            gameData = gameDAO.getGame(gameID);
        } catch (DataAccessException e){
            String message = String.format("There is no game %d", gameID);
            sendError(username, gameID, message);
            return;
        }
        if (gameData != null){
            try {
                boolean displayWhite = true;
                // check that black username is not null
                if (gameData.blackUsername() != null){
                    if (gameData.blackUsername().equals(username)){
                        displayWhite = false;
                    }
                }
                connections.add(gameID, username, session);
                String message;
                if(gameData.whiteUsername() != null && gameData.whiteUsername().equals(username)){
                    message = String.format("%s has joined game %d as the WHITE player", username, gameID);
                } else if (!displayWhite){
                    message = String.format("%s has joined game %d as the BLACK player", username, gameID);
                } else {
                    message = String.format("%s has joined game %d as an OBSERVER", username, gameID);
                }
                NotificationMessage notificationMessage =
                        new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                connections.broadcast(gameID, username, notificationMessage);
                ChessGame chessGame = gameData.game();
                LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, chessGame, displayWhite);
                connections.broadcast(gameID, username, loadGameMessage);
            } catch (Exception e){
                sendError(username, gameID, e.getMessage());
            }
        }
    }

    public void makeMove(String username, int gameID) throws IOException, DataAccessException {
        GameData gameData = gameDAO.getGame(gameID);
        ChessGame game = gameData.game();
        // validate the move (see if it's in the possibleMoves)
        // Game is updated with the move
        // LoadGameMessage with the new move to all the clients of the game
        // NotificationMessage with the move is sent to everyone
        // if(isInCheck || isInCheckMate || isStalement) sends NotificationMessage to everyone
    }

    public void leaveGame(String username, int gameID) throws IOException, DataAccessException {
        GameData gameData = gameDAO.getGame(gameID);
        String message;
        if (gameData.blackUsername() != null && gameData.blackUsername().equals(username)){
            message = String.format("%s has left the game. They were the BLACK player", username);
        } else if (gameData.whiteUsername() != null && gameData.whiteUsername().equals(username)){
            message = String.format("%s has left the game. They were the WHITE player", username);
        } else {
            message = String.format("%s has left the game. They were an OBSERVER", username);
        }
        NotificationMessage notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(gameID, username, notification);
        connections.remove(gameID, username);
    }

    public void resign(String username, int gameID) throws IOException, DataAccessException {
        GameData gameData = gameDAO.getGame(gameID);
        // need to mark the game as over
        // Game is updated in the database
        // Server sends a message to all the clients that the game is over
        String message;
        if(gameData.whiteUsername().equals(username)){
            message = String.format("%s has resigned. Black Player: %s wins", username, gameData.blackUsername());
        } else if (gameData.blackUsername().equals(username)){
            message = String.format("%s has resigned. Black Player: %s wins", username, gameData.whiteUsername());
        } else {
            message = "Not a player, cannot resign from game";
            sendError(username, gameID, message);
        }
        NotificationMessage notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(gameID, username, notificationMessage);
        // then I somehow need to close the connection for all the players
    }

    public void sendError(String username, int gameID, String message){
        try {
            ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, message);
            connections.singleMessage(gameID, username, errorMessage);
        } catch (IOException e) {
            sendError(username, gameID, e.getMessage());
        }
    }
}
