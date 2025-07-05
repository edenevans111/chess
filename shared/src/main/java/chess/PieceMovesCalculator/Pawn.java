package chess.PieceMovesCalculator;
import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;


public class Pawn implements PieceMoveCalculator {

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
            if (startPosition.getRow() == 2) {
                int[][] directions = {{1, 0}, {2, 0}};
                for (int[] direction : directions) {
                    // I need to make sure the piece does not move if there is something in front of it
                    ChessPosition endPosition = new ChessPosition(startPosition.getRow()
                            + (direction[0]), startPosition.getColumn());
                    if (!endPosition.isOnBoard()) continue;
                    ChessPiece pieceAtEnd = squares.getPiece(endPosition);
                    if (pieceAtEnd != null) {
                        continue;
                    }
                    moves.add(new ChessMove(startPosition, endPosition));
                } // end of the for loop
            } // end of the row == 2 statement
            if (startPosition.getRow() != 2) {
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
            if (startPosition.getRow() == 7) {
                int[][] directions = {{-1, 0}, {-2, 0}};
                for (int[] direction : directions) {
                    ChessPosition endPosition = new ChessPosition(startPosition.getRow()
                            + (direction[0]), startPosition.getColumn());
                    if (!endPosition.isOnBoard()) continue;
                    ChessPiece pieceAtEnd = squares.getPiece(endPosition);
                    if (pieceAtEnd != null) {
                        break;
                    }
                    moves.add(new ChessMove(startPosition, endPosition));
                } // end of the for loop
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