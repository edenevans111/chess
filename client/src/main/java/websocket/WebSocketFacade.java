package websocket;

import com.google.gson.Gson;
import com.sun.nio.sctp.NotificationHandler;
import serverfacade.ResponseException;
import ui.BoardDisplay;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.management.Notification;
import javax.swing.*;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

   private final Session session;
   private BoardDisplay boardDisplay;
   NotificationHandler notificationHandler;

   public WebSocketFacade(String url, BoardDisplay boardDisplay) throws ResponseException {
       try{
           url = url.replace("http", "ws");
           URI socketURI = new URI(url + "/ws");
           this.boardDisplay = boardDisplay;

           WebSocketContainer container = ContainerProvider.getWebSocketContainer();
           this.session  = container.connectToServer(this, socketURI);

           this.session.addMessageHandler(new MessageHandler.Whole<String>() {

               @Override
               public void onMessage(String s) {
                   ServerMessage serverMessage = new Gson().fromJson(s, ServerMessage.class);
                   var messageType = serverMessage.getServerMessageType();
                   switch(messageType){
                       case NOTIFICATION -> {
                           NotificationMessage notificationMessage = new Gson().fromJson(s, NotificationMessage.class);
                           // need to display the message using board display
                       }
                   }
                   Notification notification = new Gson().fromJson(s, Notification.class);
                   notificationHandler.notify();
               }
           });
       } catch (DeploymentException | IOException | URISyntaxException e){
           throw new ResponseException(e.getMessage());
       }

   }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
