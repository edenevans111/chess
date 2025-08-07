package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard squares;
    private boolean isOver = false;

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

    public boolean getIsOver(){
        return isOver;
    }

    public void setIsOverTrue(){
        isOver = true;
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
        ChessPiece piece = squares.getPiece(startPosition);
        ChessGame.TeamColor teamColor = piece.getTeamColor();
        // need to get all the potential moves of the piece
        Collection<ChessMove> potentiallyValid = piece.pieceMoves(squares, startPosition);
        if(potentiallyValid.isEmpty()){
            return actuallyValid;
        }
        // for each of the moves, check to see if they would result in a Check
        for(ChessMove potentialMove : potentiallyValid){
            ChessBoard potentialBoard = squares.makeDuplicate();
            // this line here is a problem somehow
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
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece piece = squares.getPiece(startPosition);
        if(piece == null){
            throw new InvalidMoveException("There is nothing at start position");
        }
        // then make sure that the piece at the startPosition is teamColor
        if(piece.getTeamColor() != teamTurn){
            throw new InvalidMoveException("Not your turn");
        }
        // if it is the right color: get all of the potentialValid moves it can make
        Collection<ChessMove> potentialMoves = validMoves(startPosition);
        if(!potentialMoves.contains(move)){
            throw new InvalidMoveException("Move is not valid");
        }
        boolean isPromotion = (piece.getPieceType() == ChessPiece.PieceType.PAWN) &&
                (move.getPromotionPiece() != null);
        if(isPromotion){
            // need to remove the pawn
            squares.removePiece(startPosition);
            ChessPiece promotedPiece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
            squares.addPiece(endPosition, promotedPiece);
        } else{
            squares.makeMove(move);
        }
        if(piece.getTeamColor() == TeamColor.WHITE){
            this.setTeamTurn(TeamColor.BLACK);
        } else{
            this.setTeamTurn(TeamColor.WHITE);
        }
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
            if(piece == null){
                continue;
            }
            Collection<ChessMove> moves = piece.pieceMoves(squares, position);
            for(ChessMove move : moves){
                if(move.getEndPosition().equals(kingPosition)){
                    // do I need something else here?
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
        Collection<ChessPosition> positions = squares.teamPositions(teamColor);
        for(ChessPosition position : positions){
            ChessPiece piece = squares.getPiece(position);

            Collection<ChessMove> movesOfPiece = piece.pieceMoves(squares, position);
            for(ChessMove move : movesOfPiece){
                ChessBoard potentialBoard = squares.makeDuplicate();
                potentialBoard.makeMove(move);
                ChessGame potentialGame = new ChessGame();
                potentialGame.setBoard(potentialBoard);
                if(!potentialGame.isInCheck(teamColor)){
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
        Collection<ChessPosition> teamPositions = squares.teamPositions(teamColor);
        for(ChessPosition position : teamPositions){
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(squares, chessGame.squares);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, squares);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "teamTurn=" + teamTurn +
                ", squares=" + squares +
                '}';
    }
}
