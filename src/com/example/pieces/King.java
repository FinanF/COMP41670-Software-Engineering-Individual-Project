package com.example.pieces;

import com.example.game.Board;

public class King extends Pieces {
    public King(boolean colour) {
        super(colour);
    }

    public King() {
    }

    @Override
    public String getSymbol() {
        return colour ? "wK" : "bK";
    }

    @Override
    public boolean validMove(int x, int y, int newX, int newY) {
        int destX = Math.abs(newX - x);
        int destY = Math.abs(newY - y);

        if ((destX == 0 && destY == 0) || destX > 1 || destY > 1) {
            return false;
        }

        Pieces dest = Board.getPieceAt(newY, newX);
        if (dest == null) {
            return true; // move into empty square
        } else {
            return dest.getColour() != this.getColour(); // capture only opponent
        }
    }
}
