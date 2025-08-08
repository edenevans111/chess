package websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import com.sun.nio.sctp.NotificationHandler;
import serverfacade.ResponseException;
import ui.BoardDisplay;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
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
   private final BoardDisplay boardDisplay;
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
                           boardDisplay.displayMessage(notificationMessage.toString());
                       }
                       case ERROR -> {
                           ErrorMessage errorMessage = new Gson().fromJson(s, ErrorMessage.class);
                           boardDisplay.displayMessage(errorMessage.toString());
                       }
                       case LOAD_GAME -> {
                           handleLoadGame(s);
                       }
                   }
               }
           });
       } catch (DeploymentException | IOException | URISyntaxException e){
           throw new ResponseException(e.getMessage());
       }

   }

   private void handleLoadGame(String s){
       LoadGameMessage loadGameMessage = new Gson().fromJson(s, LoadGameMessage.class);
       if(loadGameMessage.shouldDisplayWhite()){
           boardDisplay.displayWhiteBoard(loadGameMessage.getGame());
       } else {
           boardDisplay.displayBlackBoard(loadGameMessage.getGame());
       }
   }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void leaveGame(String username, String authToken, int gameID){
       try {
           UserGameCommand userGameCommand = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
           String json = new Gson().toJson(userGameCommand);
           this.session.getBasicRemote().sendText(json);
       } catch (Exception e) {
           throw new RuntimeException("Unable to leave game");
       }
    }

    public void makeMove(String username, String authToken, int gameID, ChessMove move){
       try {
           MakeMoveCommand makeMoveCommand = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, move);
           String json = new Gson().toJson(makeMoveCommand);
           // does this need to connect to the webSocketHandler?
           this.session.getBasicRemote().sendText(json);
       } catch (Exception e){
           throw new RuntimeException("Unable to make the move");
       }
    }

    public void joinGame(String username, String authToken, int gameID){
       try{
           UserGameCommand userGameCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
           String json = new Gson().toJson(userGameCommand);
           this.session.getBasicRemote().sendText(json);
       } catch (Exception e) {
           throw new RuntimeException("unable to join the game");
       }
    }

}
