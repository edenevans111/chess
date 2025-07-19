package chess.piecemovescalculator;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class QueenRookBishop {

    public Collection<ChessMove> movePiece(int [][] directions, ChessBoard squares, ChessPosition startPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor teamColor = squares.getPiece(startPosition).getTeamColor();
        for (int[] direction : directions) {
            for (int i = 1; i < 8; i++) {
                ChessPosition endPosition = new ChessPosition(startPosition.getRow()
                        + (i * direction[0]), startPosition.getColumn() + (i * direction[1]));
                if (!endPosition.isOnBoard()) {
                    break;
                }
                ChessPiece pieceAtEnd = squares.getPiece(endPosition);
                if (pieceAtEnd != null) {
                    if (pieceAtEnd.getTeamColor() != teamColor) {
                        moves.add(new ChessMove(startPosition, endPosition));
                    }
                    break;
                }
                moves.add(new ChessMove(startPosition, endPosition));
            }
        }
        return moves;
    }
}
