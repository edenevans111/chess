package server.websocket;

import com.google.gson.Gson;
import dataaccess.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.management.Notification;
import javax.swing.*;
import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch(command.getCommandType()){
            // depending on what type it is switch
            case CONNECT -> connect(command.getAuthToken(), session);
            case MAKE_MOVE -> makeMove(command.getAuthToken());
            case LEAVE -> leaveGame(command.getAuthToken());
            case RESIGN -> resign(command.getAuthToken());
        }
    }

    public void connect(String authToken, Session session) throws IOException {
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
}
