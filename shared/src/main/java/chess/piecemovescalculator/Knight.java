package chess.piecemovescalculator;
import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class Knight implements PieceMoveCalculator{
    @Override
    public Collection<ChessMove> move(ChessBoard squares, ChessPosition startPosition){
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor teamColor = squares.getPiece(startPosition).getTeamColor();
        int[][] directions = {{2, 1}, {1, 2}, {-2, 1}, {1, -2},
                {2, -1}, {-1, 2}, {-2, -1}, {-1, -2}};
        QueenRookBishop knight = new QueenRookBishop();
        return knight.kingKnightMethod(directions, squares, startPosition, teamColor);
    }
}
