package chess.piecemovescalculator;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class Bishop extends Queen{

    private final int [][] directions = { {1, 1}, {-1, -1}, {1, -1}, {-1, 1} };

    public Collection<ChessMove> move(ChessBoard squares, ChessPosition startPosition) {
        QueenRookBishop bishop = new QueenRookBishop();
        return bishop.movePiece(directions, squares, startPosition);
    }
}
