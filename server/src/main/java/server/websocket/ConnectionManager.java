package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import javax.management.Notification;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, HashMap<String, Connection>> connections = new ConcurrentHashMap<>();

    public void add(int gameID, String username, Session session){
        var connection = new Connection(username, session);
        if (connections.containsKey(gameID)){
            HashMap<String, Connection> existingConnections = connections.get(gameID);
            existingConnections.put(username, connection);
        } else {
            HashMap<String, Connection> connectionHashMap = new HashMap<>();
            connectionHashMap.put(username, connection);
            connections.put(gameID, connectionHashMap);
        }
    }

    public void remove(int gameID, String username){
        HashMap<String, Connection> existingConnections = connections.get(gameID);
        existingConnections.remove(username);
        if(existingConnections.isEmpty()){
            connections.remove(gameID);
        }
    }

    public void broadcast(int gameID, String excludeUsername, ServerMessage serverMessage) throws IOException {
        HashMap<String, Connection> relevantConnections = connections.get(gameID);
        var removeList = new ArrayList<Connection>();
        for(var c : relevantConnections.values()){
            if(c.session.isOpen()){
                // I might need to add a check here for in case the username is null...
                if(!c.username.equals(excludeUsername)){
                    String json = new Gson().toJson(serverMessage);
                    c.send(json);
                }
            } else {
                removeList.add(c);
            }
        }
        for(var c : removeList){
            relevantConnections.remove(c.username);
        }
    }

    public void singleMessage(int gameID, String excludeUsername, ServerMessage message) throws IOException {
        HashMap<String, Connection> relevantConnections = connections.get(gameID);
        var removeList = new ArrayList<Connection>();
        for (var c : relevantConnections.values()) {
            if (c.session.isOpen()) {
                // I might need to add a check here for in case the username is null...
                if (c.username.equals(excludeUsername)) {
                    String json = new Gson().toJson(message);
                    c.send(json);
                }
            } else {
                removeList.add(c);
            }
        }
        for (var c : removeList) {
            relevantConnections.remove(c.username);
        }
    }

    public void singleErrorMessage(Session session, ServerMessage message) throws IOException {
        if(session.isOpen()){
            String json = new Gson().toJson(message);
            session.getRemote().sendString(json);
        }
    }
}
