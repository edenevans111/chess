package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage{
    private final ChessGame game;
    private boolean displayWhite;

    public LoadGameMessage(ServerMessageType type, ChessGame game, boolean displayWhite) {
        super(type);
        this.game = game;
        this.displayWhite = displayWhite;
    }

    public ChessGame getGame(){
        return game;
    }

    public boolean shouldDisplayWhite() {
        return displayWhite;
    }
}
