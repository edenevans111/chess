package websocket;

import com.google.gson.Gson;
import com.sun.nio.sctp.NotificationHandler;
import serverfacade.ResponseException;

import javax.management.Notification;
import javax.swing.*;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

   Session session;
   NotificationHandler notificationHandler;

   public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
       try{
           url = url.replace("http", "ws");
           URI socketURI = new URI(url + "/ws");
           this.notificationHandler = notificationHandler;

           WebSocketContainer container = ContainerProvider.getWebSocketContainer();
           this.session  = container.connectToServer(this, socketURI);

           this.session.addMessageHandler(new MessageHandler.Whole<String>() {

               @Override
               public void onMessage(String s) {
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
