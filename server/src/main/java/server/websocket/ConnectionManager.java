package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import javax.management.Notification;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String authToken, Session session){
        var connection = new Connection(authToken, session);
        connections.put(authToken, connection);
    }

    public void remove(String authToken){
        connections.remove(authToken);
    }

    public void broadcast(String excludeAuthToken, ServerMessage.ServerMessageType notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for(var c : connections.values()){
            if(c.session.isOpen()){
                if(!c.authToken.equals(excludeAuthToken)){
                    c.send(notification.toString());
                }
            } else {
                removeList.add(c);
            }
        }
        for(var c : removeList){
            connections.remove(c.authToken);
        }
    }
}
