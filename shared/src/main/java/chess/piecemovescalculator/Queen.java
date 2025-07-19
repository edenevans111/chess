package chess.piecemovescalculator;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class Queen implements PieceMoveCalculator{

    private final int [][] directions = { {1, 1}, {-1, -1}, {1, -1}, {-1, 1}, {1, 0}, {0, 1}, {0, -1}, {-1, 0} };

    @Override
    public Collection<ChessMove> move(ChessBoard squares, ChessPosition startPosition) {
        QueenRookBishop queen = new QueenRookBishop();
        return queen.movePiece(directions, squares, startPosition);
    }
}
