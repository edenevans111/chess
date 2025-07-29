import chess.*;
import ui.BoardDisplay;
import ui.ChessBoardPrinter;

import java.util.HashSet;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.EMPTY;

public class Main {

    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        BoardDisplay display = new ChessBoardPrinter();
        ChessGame game = new ChessGame();
        display.displayWhiteBoard(game);
        display.displayBlackBoard(game);
    }
}