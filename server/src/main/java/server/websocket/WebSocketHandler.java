package server.websocket;

import chess.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.management.Notification;
import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final GameDAO gameDAO = new SQLGameDAO();
    private final UserDAO userDAO = new SQLUserDAO();
    private final AuthDAO authDAO = new SQLAuthDAO();

    public WebSocketHandler() {

    }

    
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        String username;
        String authToken = command.getAuthToken();
        AuthData authData = authDAO.getAuth(authToken);
        username = authData.username();
        MakeMoveCommand command1 = null;
        if (command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE){
            command1 = new Gson().fromJson(message, MakeMoveCommand.class);
        }
        switch(command.getCommandType()){
            case CONNECT -> {
                connect(username, command.getGameID(), session);
                break;
            }
            case MAKE_MOVE -> {
                try{
                    makeMove(username, command1.getGameID(), command1.getMove());
                } catch (DataAccessException | InvalidMoveException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            case LEAVE -> {
                leaveGame(username, command.getGameID());
                break;
            }
            case RESIGN -> {
                resign(username, command.getGameID());
                break;
            }
        }
        System.out.print("End of onMessage function");
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
                connections.broadcast(gameID, null, loadGameMessage);
            } catch (Exception e){
                sendError(username, gameID, e.getMessage());
            }
        }
    }

    public void makeMove(String username, int gameID, ChessMove move) throws IOException, DataAccessException, InvalidMoveException {
        
        GameData gameData = gameDAO.getGame(gameID);
        ChessGame game = gameData.game();
        if (game.getIsOver()){
            String message = String.format("%d is over, you cannot make any moves", gameID);
            sendError(username, gameID, message);
            return;
        }
        Collection<ChessMove> validMoves = game.validMoves(move.getStartPosition());
        boolean isValid = false;
        for(ChessMove potentialMove : validMoves){
            if (potentialMove.getEndPosition() == move.getEndPosition()) {
                isValid = true;
                break;
            }
        }
        if(isValid){
            game.makeMove(move);
        }
        // now need to determine if the White or Black board should be displayed...
        boolean displayWhite = true;
        boolean isStalemate = false;
        boolean isCheck = false;
        boolean isCheckmate = false;
        if(gameData.blackUsername() != null && gameData.blackUsername().equals(username)){
            displayWhite = false;
            isStalemate = game.isInStalemate(ChessGame.TeamColor.BLACK);
            isCheck = game.isInCheck(ChessGame.TeamColor.BLACK);
            isCheckmate = game.isInCheckmate(ChessGame.TeamColor.BLACK);
        } else {
            isStalemate = game.isInStalemate(ChessGame.TeamColor.WHITE);
            isCheck = game.isInCheck(ChessGame.TeamColor.WHITE);
            isCheckmate = game.isInCheckmate(ChessGame.TeamColor.WHITE);
        }
        LoadGameMessage loadGameMessage =
                new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game, displayWhite);
        String notification = String.format("%s moved from %s to %s", username, move.getStartPosition(), move.getEndPosition());
        NotificationMessage notificationMessage =
                new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, notification);
        if(isCheck || isCheckmate || isStalemate){
            String message;
            if(isStalemate){
                message = "The game is in a Stalemate";
            } else if (isCheck){
                message = String.format("%s is in Check", username);

            } else {
                message = String.format("%s is in Checkmate", username);
            }
            NotificationMessage notificationMessage1 =
                    new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast(gameID, username, notificationMessage1);
        }
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
        ChessGame chessGame = gameData.game();
        chessGame.setIsOverTrue();
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
