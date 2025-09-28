package com.example.pieces;

import com.example.game.Board;

public class Bishop extends Pieces {
    public Bishop(boolean colour) {
        super(colour);
    }

    public Bishop() {
    }

    @Override
    public String getSymbol() {
        return colour ? "wB" : "bB";
    }

    @Override
    public boolean validMove(int x, int y, int newX, int newY) {
        // Must be within board
        if (x < 0 || x > 7 || y < 0 || y > 7 || newX < 0 || newX > 7 || newY < 0 || newY > 7)
            return false;

        // Must be diagonal
        if (Math.abs(newX - x) != Math.abs(newY - y)) return false;

        int stepX = (newX > x) ? 1 : -1;
        int stepY = (newY > y) ? 1 : -1;
        int currX = x + stepX;
        int currY = y + stepY;

        while (currX != newX && currY != newY) {
            // Bounds check for safety
            if (currX < 0 || currX > 7 || currY < 0 || currY > 7) return false;

            if (Board.getPieceAt(currY, currX) != null) return false;

            currX += stepX;
            currY += stepY;
        }

        Pieces dest = Board.getPieceAt(newY, newX);
        return dest == null || dest.getColour() != this.getColour();
    }
}
