package chess.PieceMovesCalculator;
import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Pawn implements PieceMoveCalculator{
    @Override
    public Collection<ChessMove> move(ChessBoard squares, ChessPosition startPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        // need to write code for the fact that pawns take diagonally
        // need to write code that for the first move they can move forward one or two spaces
            // write check for what row the pawn is on
        // and then just the normal code that they can move forward one spot
        ChessGame.TeamColor teamColor = squares.getPiece(startPosition).getTeamColor();
        int [][] directions = { {1, 0} };
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
        return moves;
    }
}
