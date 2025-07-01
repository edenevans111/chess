package chess.PieceMovesCalculator;
import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;


public class Pawn implements PieceMoveCalculator{

    @Override
    public Collection<ChessMove> move(ChessBoard squares, ChessPosition startPosition) {
        Collection<ChessPiece.PieceType> promotionTypes = new ArrayList<>();
        promotionTypes.add(ChessPiece.PieceType.QUEEN);
        promotionTypes.add(ChessPiece.PieceType.BISHOP);
        promotionTypes.add(ChessPiece.PieceType.ROOK);
        promotionTypes.add(ChessPiece.PieceType.KNIGHT);

        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor teamColor = squares.getPiece(startPosition).getTeamColor();
        if (teamColor == ChessGame.TeamColor.WHITE){
            boolean promote = false;
            if(startPosition.getRow() == 2){
                int[][] directions = {{1, 0}, {2,0}};
                for(int[] direction : directions){
                    // I need to make sure the piece does not move if there is something in front of it
                    ChessPosition endPosition = new ChessPosition(startPosition.getRow()
                            + (direction[0]), startPosition.getColumn() + (direction[1]));
                    if (!endPosition.isOnBoard()) continue;
                    ChessPiece pieceAtEnd = squares.getPiece(endPosition);
                    if (pieceAtEnd != null) {
                        continue;
                    }
                    moves.add(new ChessMove(startPosition, endPosition));
                } // end of the for loop
            } // end of the row == 2 statement
            if (startPosition.getRow() != 2){
                int[][] directions = {{1, 0}};
                for(int[] direction : directions){
                    ChessPosition endPosition = new ChessPosition(startPosition.getRow()
                            + (direction[0]), startPosition.getColumn() + (direction[1]));
                    if (!endPosition.isOnBoard()) continue;
                    if(endPosition.getRow() == 8){promote = true;}
                    ChessPiece pieceAtEnd = squares.getPiece(endPosition);
                    if (pieceAtEnd != null) {
                        continue;
                    }
                    moves.add(new ChessMove(startPosition, endPosition));
                } // end of the for loop
            } // end of the row != 2 statement

            int[][] directions = {{1, 1}, {1, -1}};
            for(int[] direction : directions){
                ChessPosition endPosition = new ChessPosition(startPosition.getRow()
                        + (direction[0]), startPosition.getColumn() + (direction[1]));
                if (!endPosition.isOnBoard()) continue;
                if(endPosition.getRow() == 8){
                    promote = true;
                    for(ChessPiece.PieceType type : promotionTypes){
                        moves.add(new ChessMove(startPosition, endPosition, type));
                    }
                }
                
                ChessPiece pieceAtEnd = squares.getPiece(endPosition);
                if (pieceAtEnd != null) {
                    if (pieceAtEnd.getTeamColor() != teamColor) {
                        moves.add(new ChessMove(startPosition, endPosition));
                    }
                }
            }
            // I need to make a boolean for whether the pawn could promote

            /*
            // now we need to add the taking diagonally functionality
            ChessPiece possibleTakeRight = squares.getPiece(new ChessPosition(startPosition.getRow() + 1,
                    startPosition.getColumn() + 1));
            ChessPiece possibleTakeLeft = squares.getPiece(new ChessPosition(startPosition.getRow() + 1,
                    startPosition.getColumn() - 1));
            // to check if we can take to the right
            if(possibleTakeRight != null && possibleTakeRight.getTeamColor() != teamColor){
                ChessPosition endPosition = new ChessPosition(startPosition.getRow()
                        + 1, startPosition.getColumn() + 1);
                if (endPosition.isOnBoard()){
                    moves.add(new ChessMove(startPosition, endPosition));
                }
            }
            // and to see if we can take to the left
            if(possibleTakeLeft != null && possibleTakeLeft.getTeamColor() != teamColor){
                ChessPosition endPosition = new ChessPosition(startPosition.getRow()
                        + 1, startPosition.getColumn() - 1);
                if (endPosition.isOnBoard()){
                    moves.add(new ChessMove(startPosition, endPosition));
                }
            }*/
        }
        /*if(teamColor == ChessGame.TeamColor.WHITE && startPosition.getRow() == 2){
            int[][] directions = {{1,0}, {2, 0}};
            for (int[] direction : directions) {
                ChessPosition endPosition = new ChessPosition(startPosition.getRow()
                        + (direction[0]), startPosition.getColumn() + (direction[1]));
                if (!endPosition.isOnBoard()) continue;
                ChessPiece pieceAtEnd = squares.getPiece(endPosition);
                if (pieceAtEnd != null) {
                    if (pieceAtEnd.getTeamColor() != teamColor) {
                        moves.add(new ChessMove(startPosition, endPosition));
                    }
                    continue;
                }
                moves.add(new ChessMove(startPosition, endPosition));
            }
        }
        if (teamColor == ChessGame.TeamColor.BLACK && startPosition.getRow() == 7){
            int[][] directions = {{-1,0}, {-2, 0}};
            for (int[] direction : directions) {
                ChessPosition endPosition = new ChessPosition(startPosition.getRow()
                        + (direction[0]), startPosition.getColumn() + (direction[1]));
                if (!endPosition.isOnBoard()) continue;
                ChessPiece pieceAtEnd = squares.getPiece(endPosition);
                if (pieceAtEnd != null) {
                    if (pieceAtEnd.getTeamColor() != teamColor) {
                        moves.add(new ChessMove(startPosition, endPosition));
                    }
                    continue;
                }
                moves.add(new ChessMove(startPosition, endPosition));
            }
        } else if (teamColor == ChessGame.TeamColor.WHITE && startPosition.getRow() != 2) {
            int[][] directions = {{1,0}};
            for (int[] direction : directions) {
                ChessPosition endPosition = new ChessPosition(startPosition.getRow()
                        + (direction[0]), startPosition.getColumn() + (direction[1]));
                if (!endPosition.isOnBoard()) continue;
                ChessPiece pieceAtEnd = squares.getPiece(endPosition);
                if (pieceAtEnd != null) {
                    if (pieceAtEnd.getTeamColor() != teamColor) {
                        moves.add(new ChessMove(startPosition, endPosition));
                    }
                    continue;
                }
                moves.add(new ChessMove(startPosition, endPosition));
            }
        }
        else if (teamColor == ChessGame.TeamColor.BLACK && startPosition.getRow() != 7) {
            int[][] directions = {{-1,0}};
            for (int[] direction : directions) {
                ChessPosition endPosition = new ChessPosition(startPosition.getRow()
                        + (direction[0]), startPosition.getColumn() + (direction[1]));
                if (!endPosition.isOnBoard()) continue;
                ChessPiece pieceAtEnd = squares.getPiece(endPosition);
                if (pieceAtEnd != null) {
                    if (pieceAtEnd.getTeamColor() != teamColor) {
                        moves.add(new ChessMove(startPosition, endPosition));
                    }
                    continue;
                }
                moves.add(new ChessMove(startPosition, endPosition));
            }
        }
        else {
            int[][] directions = {{1, 0}};
            for (int[] direction : directions) {
                ChessPosition endPosition = new ChessPosition(startPosition.getRow()
                        + (direction[0]), startPosition.getColumn() + (direction[1]));
                if (!endPosition.isOnBoard()) {
                    continue;
                }
                ChessPiece pieceAtEnd = squares.getPiece(endPosition);
                if (pieceAtEnd != null) {
                    if (pieceAtEnd.getTeamColor() != teamColor) {
                        moves.add(new ChessMove(startPosition, endPosition));
                    }
                    continue;
                }
                moves.add(new ChessMove(startPosition, endPosition));
            }
        }*/
        return moves;
    }
}
