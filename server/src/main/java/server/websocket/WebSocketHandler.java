package server.websocket;

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
import websocket.messages.ServerMessage;

import javax.management.Notification;
import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final GameDAO gameDAO;
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameService gameService;

    public WebSocketHandler(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) throws DataAccessException {
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameService = new GameService(userDAO, authDAO, gameDAO);
    }

    
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        String authToken = command.getAuthToken();
        AuthData authData = authDAO.getAuth(authToken);
        String username = authData.username();

        switch(command.getCommandType()){
            case CONNECT -> connect(username, command.getGameID(), session);
            case MAKE_MOVE -> makeMove(command.getAuthToken());
            case LEAVE -> leaveGame(command.getAuthToken());
            case RESIGN -> resign(command.getAuthToken());
        }
    }

    public void connect(String authToken, int gameID, Session session) throws IOException {
        GameData gameData = null;
        try {
            gameData = gameDAO.getGame(gameID);
        } catch (DataAccessException e){
            String message = String.format("There is no game %d", gameID);
            // then need to sendError
        }

        connections.add(authToken, session);
        ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        connections.broadcast(authToken, notification.getServerMessageType());
        connections.broadcast(authToken, message.getServerMessageType());
    }

    public void makeMove(String authToken) throws IOException {
        // I think this one honestly needs to be substantially different...
        ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        ServerMessage loadGame = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        connections.broadcast(authToken, notification.getServerMessageType());
        connections.broadcast(authToken, loadGame.getServerMessageType());
    }

    public void leaveGame(String authToken) throws IOException {
        connections.remove(authToken);
        var message = String.format("%s has left the game", authToken);
        connections.broadcast(authToken, ServerMessage.ServerMessageType.NOTIFICATION);
    }

    public void resign(String authToken) throws IOException {
        connections.remove(authToken);
        // I am pretty sure I am actually supposed to remove everyone...?
        connections.broadcast(authToken, ServerMessage.ServerMessageType.NOTIFICATION);
    }

    public void sendError(String username, int gameID, String message){
        try {

        } catch (IOException e){
            sendError(username, gameID, message);
        }
    }
}
