package ui;

import static ui.EscapeSequences.*;
import chess.ChessGame;
import chess.ChessPosition;

import java.util.HashSet;

public class Repl implements BoardDisplay{

    private final String bunchOfSpaces = " ";


    @Override
    public void displayBlackBoard(ChessGame game, HashSet<ChessPosition> validSquares) {
        System.out.println("\n" + SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
    }

    @Override
    public void displayWhiteBoard(ChessGame game, HashSet<ChessPosition> validSquares) {

    }

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }
}
