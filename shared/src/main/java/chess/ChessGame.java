package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard squares;

    public ChessGame() {
        ChessBoard squares = new ChessBoard();
        squares.resetBoard();
        this.squares = squares;
        this.teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> actuallyValid = new ArrayList<>();

        ChessGame.TeamColor teamColor = squares.getPiece(startPosition).getTeamColor();
        // need to get all the potential moves of the piece
        Collection<ChessMove> potentiallyValid = squares.getPiece(startPosition).pieceMoves(squares, startPosition);
        // for each of the moves, check to see if they would result in a Check
        for(ChessMove potentialMove : potentiallyValid){
            ChessBoard potentialBoard = squares.makeDuplicate();
            potentialBoard.makeMove(potentialMove);
            ChessGame potentialGame = new ChessGame();
            potentialGame.setBoard(potentialBoard);
            if(!potentialGame.isInCheck(teamColor)){
                actuallyValid.add(potentialMove);
            }
        }
        return actuallyValid;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        // throw new InvalidMoveException("Reason it is not valid")
        // check to see if there is something at the startPosition of the move
        if(move.getStartPosition() == null){
            throw new InvalidMoveException("There is nothing at that position");
        }
        // then make sure that the piece at the startPosition is teamColor
        ChessPosition startPosition = move.getStartPosition();
        if(squares.getPiece(startPosition).getTeamColor() != teamTurn){
            throw new InvalidMoveException("Not your turn");
        }
        // if it is the right color: get all of the potentialValid moves it can make
        Collection<ChessMove> potentialMoves = validMoves(startPosition);
        if(potentialMoves.contains(move)){
            squares.makeMove(move);
            if(teamTurn == TeamColor.WHITE){
                teamTurn = TeamColor.BLACK;
            } else{
                teamTurn = TeamColor.WHITE;
            }
        }
        // check if the move is in the valid moves
            // if it is valid, make the move
            // set the new teamColor
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // get the opponent's color:
        TeamColor opponentColor;
        if(teamColor == TeamColor.WHITE){
            opponentColor = TeamColor.BLACK;
        } else {
            opponentColor = TeamColor.WHITE;
        }
        // check if any of the opponent pieces would get the king
            // get the position of the teamColor KING
            // see if the endPositions of the pieces of the other team == KingPosition
        ChessPosition kingPosition = squares.kingPosition(teamColor);
        Collection<ChessPosition> positions = squares.teamPositions(opponentColor);
        for(ChessPosition position : positions){
            ChessPiece piece = squares.getPiece(position);
            Collection<ChessMove> moves = piece.pieceMoves(squares, position);
            for(ChessMove move : moves){
                if(move.getEndPosition() == kingPosition){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor)){
            return false;
        }
        TeamColor opponentColor;
        if(teamColor == TeamColor.WHITE){
            opponentColor = TeamColor.BLACK;
        } else {
            opponentColor = TeamColor.WHITE;
        }
        Collection<ChessPosition> positions = squares.teamPositions(teamColor);
        for(ChessPosition position : positions){
            ChessPiece piece = squares.getPiece(position);
            Collection<ChessMove> moves = piece.pieceMoves(squares, position);
            for(ChessMove move : moves){
                ChessBoard potentialBoard = squares.makeDuplicate();
                potentialBoard.makeMove(move);
                if(!isInCheck(teamColor)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheck(teamColor)){
            return false;
        }
        Collection<ChessPosition> positions = squares.teamPositions(teamColor);
        for(ChessPosition position : positions){
            Collection<ChessMove> potentialMoves = validMoves(position);
            if(!potentialMoves.isEmpty()){
                return false;
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.squares = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return squares;
    }
}
