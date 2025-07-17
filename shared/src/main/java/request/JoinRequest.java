package request;

import chess.ChessGame;
import chess.ChessPiece;

public record JoinRequest(ChessGame.TeamColor playerColor, int gameID) {
}
