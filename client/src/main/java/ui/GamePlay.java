package ui;

import chess.ChessGame;
import model.GameData;
import websocket.commands.UserGameCommand;

public class GamePlay extends ChessClient{
    private boolean displayWhite;
    private GameData gameData;
    private String authToken;
    private String username;

    public GamePlay(String serverUrl) {
        super(serverUrl);
    }





}
