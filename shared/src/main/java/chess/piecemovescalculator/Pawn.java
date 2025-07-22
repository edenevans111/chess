package chess.piecemovescalculator;
import chess.*;

import java.util.ArrayList;
import java.util.Collection;


public class Pawn implements PieceMoveCalculator {

    Collection<ChessMove> pawnMoves(int[][] directions, ChessBoard squares, ChessPosition startPosition, int atEnd){
        Collection<ChessMove> moves = new ArrayList<>();
        for (int[] direction : directions) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow()
                    + (direction[0]), startPosition.getColumn() + direction[1]);
            if (!endPosition.isOnBoard()) continue;
            ChessPiece pieceAtEnd = squares.getPiece(endPosition);
            if (pieceAtEnd != null) {
                break;
            }
            moves.add(new ChessMove(startPosition, endPosition));
        }
        return moves;
    }

    @Override
    public Collection<ChessMove> move(ChessBoard squares, ChessPosition startPosition) {
        Collection<ChessPiece.PieceType> promotionTypes = new ArrayList<>();
        promotionTypes.add(ChessPiece.PieceType.QUEEN);
        promotionTypes.add(ChessPiece.PieceType.BISHOP);
        promotionTypes.add(ChessPiece.PieceType.ROOK);
        promotionTypes.add(ChessPiece.PieceType.KNIGHT);

        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor teamColor = squares.getPiece(startPosition).getTeamColor();
        if (teamColor == ChessGame.TeamColor.WHITE) {
            int atEnd = 8;
            if (startPosition.getRow() == 2) {
                int[][] directions = {{1, 0}, {2, 0}};
                moves = pawnMoves(directions, squares, startPosition, atEnd);
            }
            else {
                int[][] directions = {{1, 0}};
                for (int[] direction : directions) {
                    ChessPosition endPosition = new ChessPosition(startPosition.getRow()
                            + direction[0], startPosition.getColumn());
                    if (!endPosition.isOnBoard()) break;
                    ChessPiece pieceAtEnd = squares.getPiece(endPosition);
                    if (pieceAtEnd != null) {
                        continue;
                    }
                    if (endPosition.getRow() == 8) {
                        for (ChessPiece.PieceType type : promotionTypes) {
                            moves.add(new ChessMove(startPosition, endPosition, type));
                        }
                    } else {
                        moves.add(new ChessMove(startPosition, endPosition));
                    }
                } // end of the for loop
            } // end of the row != 2 statement

            int[][] directions = {{1, 1}, {1, -1}};
            for (int[] direction : directions) {
                ChessPosition endPosition = new ChessPosition(startPosition.getRow()
                        + (direction[0]), startPosition.getColumn() + (direction[1]));
                if (!endPosition.isOnBoard()) continue;
                ChessPiece pieceAtEnd = squares.getPiece(endPosition);
                if (pieceAtEnd != null) {
                    if (pieceAtEnd.getTeamColor() != teamColor) {
                        if (endPosition.getRow() == 8) {
                            for (ChessPiece.PieceType type : promotionTypes) {
                                moves.add(new ChessMove(startPosition, endPosition, type));
                            }
                        } else{
                            moves.add(new ChessMove(startPosition, endPosition));
                        }
                    }
                }
            }
        }
        // this is for the Black pawns
        if (teamColor == ChessGame.TeamColor.BLACK) {
            int atEnd = 1;
            if (startPosition.getRow() == 7) {
                int[][] directions = {{-1, 0}, {-2, 0}};
                moves = pawnMoves(directions, squares, startPosition, atEnd); // end of the for loop
            } // end of the row == 2 statement
            if (startPosition.getRow() != 7) {
                int[][] directions = {{-1, 0}};
                for (int[] direction : directions) {
                    ChessPosition endPosition = new ChessPosition(startPosition.getRow()
                            + direction[0], startPosition.getColumn());
                    if (!endPosition.isOnBoard()) break;
                    ChessPiece pieceAtEnd = squares.getPiece(endPosition);
                    if (pieceAtEnd != null) {
                        continue;
                    }
                    if (endPosition.getRow() == 1) {
                        for (ChessPiece.PieceType type : promotionTypes) {
                            moves.add(new ChessMove(startPosition, endPosition, type));
                        }
                    } else {
                        moves.add(new ChessMove(startPosition, endPosition));
                    }
                } // end of the for loop
            } // end of the row != 2 statement

            int[][] directions = {{-1, 1}, {-1, -1}};
            for (int[] direction : directions) {
                ChessPosition endPosition = new ChessPosition(startPosition.getRow()
                        + (direction[0]), startPosition.getColumn() + (direction[1]));
                if (!endPosition.isOnBoard()) continue;
                ChessPiece pieceAtEnd = squares.getPiece(endPosition);
                if (pieceAtEnd != null) {
                    if (pieceAtEnd.getTeamColor() != teamColor) {
                        if (endPosition.getRow() == 1) {
                            for (ChessPiece.PieceType type : promotionTypes) {
                                moves.add(new ChessMove(startPosition, endPosition, type));
                            }
                        } else{
                            moves.add(new ChessMove(startPosition, endPosition));
                        }
                    }
                }
            }
        }

        return moves;
    }
}