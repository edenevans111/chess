package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage{
    private final ChessGame game;
    private boolean displayWhite;

    public LoadGameMessage(ServerMessageType type, ChessGame game) {
        super(type);
        this.game = game;
    }

    public ChessGame getGame(){
        return game;
    }
}
