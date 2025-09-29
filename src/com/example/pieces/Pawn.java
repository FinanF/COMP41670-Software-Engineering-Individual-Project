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

        // Capture
        if (newY == y + direction && (newX == x + 1 || newX == x - 1)) {
            Pieces dest = Board.getPieceAt(newY, newX);
            return dest != null && dest.getColour() != this.getColour();
        }

        // Not valid
        return false;
    }

}
