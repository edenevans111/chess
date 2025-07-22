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
        QueenRookBishop king = new QueenRookBishop();
        return king.kingKnightMethod(directions, squares, startPosition, teamColor);
    }
}
