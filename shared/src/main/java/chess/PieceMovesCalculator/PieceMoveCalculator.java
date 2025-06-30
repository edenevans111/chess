package chess.PieceMovesCalculator;

import chess.*;

import java.util.Collection;

public interface PieceMoveCalculator  {

     Collection<ChessMove> move(ChessBoard squares, ChessPosition startPosition);

}