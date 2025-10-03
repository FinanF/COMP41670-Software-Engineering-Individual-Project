package com.example.pieces;

import com.example.game.Board;

public class King extends Pieces {
    public boolean hasMoved = false; // track if king has moved

    public King(boolean colour) {
        super(colour);
    }

    @Override
    public String getSymbol() {
        return colour ? "wK" : "bK";
    }

    @Override
    public boolean validMove(int x, int y, int newX, int newY) {
        int dx = Math.abs(newX - x);
        int dy = Math.abs(newY - y);

        // --- Standard king move (1 square in any direction) ---
        if ((dx <= 1 && dy <= 1) && !(dx == 0 && dy == 0)) {
            Pieces dest = Board.getPieceAt(newY, newX);
            return dest == null || dest.getColour() != this.getColour();
        }

        // --- Castling attempt (move exactly 2 squares horizontally, same row) ---
        // Castling validation happens in Board.isPlaying(),
        // we only signal that "yes, this could be castling".
        return dy == 0 && dx == 2 && !hasMoved;// invalid otherwise
    }
}
