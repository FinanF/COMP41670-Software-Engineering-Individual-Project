package com.example.pieces;

import com.example.game.Board;

public class Pawn extends Pieces {
    public boolean firstMove;

    public Pawn(boolean colour) {
        super(colour);
        firstMove = true;
    }

    @Override
    public String getSymbol() {
        return colour ? "wP" : "bP";
    }

    @Override
    public boolean validMove(int x, int y, int newX, int newY) {
        int direction = this.getColour() ? -1 : 1; // white moves up (-1), black moves down (+1)

        // Normal move (1 step forward)
        if (newX == x && newY == y + direction && Board.getPieceAt(newY, newX) == null) {
            return true;
        }

        // First move (1 or 2 steps forward)
        if (firstMove && newX == x && (newY == y + direction || newY == y + 2 * direction)) {
            // Both squares must be empty if moving 2 steps
            if (Board.getPieceAt(y + direction, x) == null &&
                    (newY == y + direction || Board.getPieceAt(newY, newX) == null)) {
                return true;
            }
        }

        // Normal capture
        if (newY == y + direction && (newX == x + 1 || newX == x - 1)) {
            Pieces dest = Board.getPieceAt(newY, newX);
            if (dest != null && dest.getColour() != this.getColour()) {
                return true;
            }

            // En passant capture
            if (Board.lastMove != null && Board.lastMove.piece instanceof Pawn) {
                // The enemy pawn must have just moved 2 steps and landed next to this pawn
                return Board.lastMove.toY == y && Math.abs(Board.lastMove.toX - x) == 1 &&
                        newX == Board.lastMove.toX && newY == y + direction &&
                        Math.abs(Board.lastMove.toY - Board.lastMove.fromY) == 2;
            }
        }

        // Not valid
        return false;
    }
}
