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
        System.out.println("\n" + SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + bunchOfSpaces + "h" +
                fewerSpaces + "g" + fewerSpaces + "f" + fewerSpaces + "e" + fewerSpaces + "d" + fewerSpaces + "c" +
                fewerSpaces + "b" + fewerSpaces + "a");
        for (int i = 1; i < 9; i++) {
            StringBuilder row = new StringBuilder();
            row.append(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
            row.append(String.format(fewerSpaces + "%s" + fewerSpaces, i));
        }

    }

    @Override
    public void displayWhiteBoard(ChessGame game) {

        System.out.println("\n" + SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + bunchOfSpaces + "a" +
                fewerSpaces + "b" + fewerSpaces + "c" + fewerSpaces + "d" + fewerSpaces + "e" + fewerSpaces + "f" +
                fewerSpaces + "g" + fewerSpaces + "h" + "\n");
        for (int i = 8; i > 0; i--) {
            StringBuilder row = new StringBuilder();
            row.append(SET_BG_COLOR_LIGHT_GREY + "%s");
            boolean startWhite = i % 2 == 0;
            printRow(startWhite, i, game);
        }
    }

    private void printAllRows(ChessGame game){
        for (int i = 1; i < 9; i++) {
            boolean startWhite = i % 2 == 1;
            // need to account for white or black perspective
            // need to invert the rows depending
            printRow(startWhite, i, game);
        }
    }

    private void printRow(boolean startWhite, int rowNum, ChessGame game){
        System.out.print(bunchOfSpaces);
        for (int i = 1; i < 9; i++) {

            boolean isWhite = (i % 2 == 0) != startWhite;
            ChessPosition position = new ChessPosition(rowNum, i);
            // I just need to figure out how to get the isHighlighted bool
            // get a collection of the moves of the position that the player selects
            // if the rowNum == position.getRow && i == position.getColumn()
            // then isHighlighted = true, else: false
            printSquare(isWhite, false, game.getBoard().getPiece(position));
            System.out.print(fewerSpaces);
        }
        System.out.print("\n");
    }

    private void printSquare(boolean isWhite, boolean isHighlighted, ChessPiece piece){
        StringBuilder square = new StringBuilder();
        if(piece.getPieceType() == null){
            if(isWhite){
                square.append(SET_BG_COLOR_LIGHT_GREY);
            } else {
                square.append(SET_BG_COLOR_DARK_GREY);
            }
        }


        ChessPiece.PieceType pieceType = piece.getPieceType();
        if(pieceType == null){
            System.out.print("Hi");
        }
        ChessGame.TeamColor color = piece.getTeamColor();

        if(isHighlighted){
            square.append(SET_BG_COLOR_MAGENTA);
        }
        else if(isWhite){
            square.append(SET_BG_COLOR_LIGHT_GREY);
        } else {
            square.append(SET_TEXT_COLOR_DARK_GREY);
        }

        if(color == ChessGame.TeamColor.WHITE){
            switch(pieceType){
                case KING -> square.append(SET_TEXT_COLOR_WHITE + WHITE_KING);
                case QUEEN -> square.append(SET_TEXT_COLOR_WHITE + WHITE_QUEEN);
                case BISHOP -> square.append(SET_TEXT_COLOR_WHITE + WHITE_BISHOP);
                case ROOK -> square.append(SET_TEXT_COLOR_WHITE + WHITE_ROOK);
                case KNIGHT -> square.append(SET_TEXT_COLOR_WHITE + WHITE_KNIGHT);
                case PAWN -> square.append(SET_TEXT_COLOR_WHITE + WHITE_PAWN);
                case null -> square.append("  ");
            }
        } else {
            switch (pieceType) {
                case KING -> square.append(SET_TEXT_COLOR_BLACK + BLACK_KING);
                case QUEEN -> square.append(SET_TEXT_COLOR_BLACK + BLACK_QUEEN);
                case BISHOP -> square.append(SET_TEXT_COLOR_BLACK + BLACK_BISHOP);
                case ROOK -> square.append(SET_TEXT_COLOR_BLACK + BLACK_ROOK);
                case KNIGHT -> square.append(SET_TEXT_COLOR_BLACK + BLACK_KNIGHT);
                case PAWN -> square.append(SET_TEXT_COLOR_BLACK + BLACK_PAWN);
                // I will probably need to adjust this spacing to be the proper size later
                case null -> square.append("   ");
            }
        }
        System.out.print(square);
    }

}
