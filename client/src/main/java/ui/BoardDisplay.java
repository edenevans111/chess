package ui;

import chess.*;

import java.util.HashSet;

public interface BoardDisplay {
    public void displayBlackBoard(ChessGame game, HashSet<ChessPosition> validSquares);
    public void displayWhiteBoard(ChessGame game, HashSet<ChessPosition> validSquares);
    public void displayMessage(String message);
}
