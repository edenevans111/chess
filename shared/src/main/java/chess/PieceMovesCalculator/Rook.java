package chess.PieceMovesCalculator;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class Rook implements PieceMoveCalculator{
    @Override
    public Collection<ChessMove> move(ChessBoard squares, ChessPosition startPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor teamColor = squares.getPiece(startPosition).getTeamColor();
        int [][] directions = { {0, 1}, {1,0}, {0, -1}, {-1, 0} };
        for (int[] direction : directions) {
            for (int i = 1; i < 8; i++) {
                ChessPosition endPosition = new ChessPosition(startPosition.getRow()
                        + (i * direction[0]), startPosition.getColumn() + (i * direction[1]));
                if (!endPosition.isOnBoard()) {
                    continue;
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
