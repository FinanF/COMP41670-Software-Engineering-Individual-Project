package com.example.pieces;

import com.example.game.Board;

public class Rook extends Pieces {
    public Rook(boolean colour) {
        super(colour);
    }

    public Rook() {
    }

    @Override
    public String getSymbol() {
        return colour ? "wR" : "bR";
    }

    @Override
    public boolean validMove(int x, int y, int newX, int newY) {
        //Not in same row or column
        if (x != newX && y != newY) {
            return false;
        }

        int stepX = 0;
        int stepY = 0;

        if (newX > x) stepX = 1;
        if (newX < x) stepX = -1;
        if (newY > y) stepY = 1;
        if (newY < y) stepY = -1;

        int currX = x + stepX;
        int currY = y + stepY;

        //Going to destination
        while (currX != newX || currY != newY) {
            if (Board.getPieceAt(currY, currX) != null) {
                return false; // path blocked
            }
            currX += stepX;
            currY += stepY;
        }

        //Destination square
        Pieces dest = Board.getPieceAt(newY, newX);
        if (dest == null) {
            return true; // move to empty
        } else {
            return dest.getColour() != this.getColour(); // capture opponent
        }
    }
}
