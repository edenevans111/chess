package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.HashSet;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_BG_COLOR_LIGHT_GREY;

public class ChessBoardPrinter implements BoardDisplay{

    private final String bunchOfSpaces = EMPTY + EMPTY + EMPTY + EMPTY + EMPTY + EMPTY;
    private final String fewerSpaces = EMPTY + EMPTY + EMPTY;

    @Override
    public void displayBlackBoard(ChessGame game) {
        System.out.println("\n" + SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "    h " +
                " g " + " f " + " e " + " d " + " c " + " b " + " a " + "   " + RESET_BG_COLOR);
        for (int i = 1; i < 9; i++) {
            StringBuilder row = new StringBuilder();
            row.append(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + i + " ");
            System.out.print(row);
            boolean startWhite = i % 2 == 1;
            printRow(startWhite, i, game);
        }
        System.out.println(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "    h " +
                " g " + " f " + " e " + " d " + " c " + " b " + " a " + "   " + RESET_BG_COLOR);

    }

    @Override
    public void displayWhiteBoard(ChessGame game) {

        System.out.println("\n" + SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "    a " +
                " b " + " c " + " d " + " e " + " f " + " g " + " h " + "   " + RESET_BG_COLOR);
        for (int i = 8; i > 0; i--) {
            StringBuilder row = new StringBuilder();
            row.append(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + i + " ");
            System.out.print(row);
            boolean startWhite = i % 2 == 0;
            printRow(startWhite, i, game);
        }
        System.out.println(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "    a " +
                " b " + " c " + " d " + " e " + " f " + " g " + " h " + "   " + RESET_BG_COLOR);
    }

    private void printRow(boolean startWhite, int rowNum, ChessGame game){
        //System.out.print(bunchOfSpaces);
        for (int i = 1; i < 9; i++) {

            boolean isWhite = (i % 2 == 0) != startWhite;
            ChessPosition position = new ChessPosition(rowNum, i);
            // I just need to figure out how to get the isHighlighted bool
            // get a collection of the moves of the position that the player selects
            // if the rowNum == position.getRow && i == position.getColumn()
            // then isHighlighted = true, else: false
            printSquare(isWhite, false, game.getBoard().getPiece(position));
            //System.out.print(fewerSpaces);
        }
        // I will need to figure out how to change this so that it writes the row number
        System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + rowNum + " ");
        System.out.print(RESET_BG_COLOR + "\n");
    }

    private void printSquare(boolean isWhite, boolean isHighlighted, ChessPiece piece){
        StringBuilder square = new StringBuilder();

        if(isHighlighted){
            square.append(SET_BG_COLOR_MAGENTA);
        } else if (isWhite){
            square.append(SET_BG_COLOR_LIGHT_GREY);
        } else {
            square.append(SET_BG_COLOR_BLUE);
        }

        if(piece == null){
            square.append("   ");
        } else {
            ChessPiece.PieceType pieceType = piece.getPieceType();
            ChessGame.TeamColor color = piece.getTeamColor();

            if(color == ChessGame.TeamColor.WHITE){
                switch(pieceType){
                    case KING -> square.append(SET_TEXT_COLOR_WHITE + WHITE_KING);
                    case QUEEN -> square.append(SET_TEXT_COLOR_WHITE + WHITE_QUEEN);
                    case BISHOP -> square.append(SET_TEXT_COLOR_WHITE + WHITE_BISHOP);
                    case ROOK -> square.append(SET_TEXT_COLOR_WHITE + WHITE_ROOK);
                    case KNIGHT -> square.append(SET_TEXT_COLOR_WHITE + WHITE_KNIGHT);
                    case PAWN -> square.append(SET_TEXT_COLOR_WHITE + WHITE_PAWN);
                }
            } else {
                switch (pieceType) {
                    case KING -> square.append(SET_TEXT_COLOR_BLACK + BLACK_KING);
                    case QUEEN -> square.append(SET_TEXT_COLOR_BLACK + BLACK_QUEEN);
                    case BISHOP -> square.append(SET_TEXT_COLOR_BLACK + BLACK_BISHOP);
                    case ROOK -> square.append(SET_TEXT_COLOR_BLACK + BLACK_ROOK);
                    case KNIGHT -> square.append(SET_TEXT_COLOR_BLACK + BLACK_KNIGHT);
                    case PAWN -> square.append(SET_TEXT_COLOR_BLACK + BLACK_PAWN);
                }
            }
        }
        System.out.print(square);
    }

}
