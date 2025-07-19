package chess.piecemovescalculator;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class Rook implements PieceMoveCalculator{

    private final int [][] directions = { {0, 1}, {1,0}, {0, -1}, {-1, 0} };

    @Override
    public Collection<ChessMove> move(ChessBoard squares, ChessPosition startPosition) {
        QueenRookBishop rook = new QueenRookBishop();
        return rook.movePiece(directions, squares, startPosition);
    }
}
