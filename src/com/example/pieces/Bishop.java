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
        if (Math.abs(newX - x) != Math.abs(newY - y)) {
            return false;
        }
        int stepX = (newX > x) ? 1 : -1;
        int stepY = (newY > y) ? 1 : -1;
        int currX = x + stepX;
        int currY = y + stepY;
        while (currX != newX && currY != newY) {
            if (Board.getPieceAt(currY, currX) != null) {
                return false;
            }
            currX += stepX;
            currY += stepY;
        }

        Pieces dest = Board.getPieceAt(newY, newX);

        if (dest == null) {
            return true;
        } else {
            return dest.getColour() != this.getColour();
        }
    }
}
