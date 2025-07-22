package chess.piecemovescalculator;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class King implements PieceMoveCalculator{
    @Override
    public Collection<ChessMove> move(ChessBoard squares, ChessPosition startPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor teamColor = squares.getPiece(startPosition).getTeamColor();
        int [][] directions = { {1, 1}, {-1, -1}, {1, -1}, {-1, 1}, {1, 0}, {0, 1}, {0, -1}, {-1, 0} };
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
        return moves;
    }
}
