package websocket;

import chess.ChessGame;

public interface NotificationHandler {
    public void loadGameHandler(ChessGame game);
}
