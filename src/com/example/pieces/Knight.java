package com.example.pieces;

import com.example.game.Board;

public class Knight extends Pieces {
    public Knight(boolean colour) {
        super(colour);
    }

    public Knight() {
    }

    @Override
    public String getSymbol() {
        return colour ? "wk" : "bk";
    }

    @Override
    public boolean validMove(int x, int y, int newX, int newY) {
        int destX = Math.abs(newX - x);
        int destY = Math.abs(newY - y);

        if (!((destX == 2 && destY == 1) || (destX == 1 && destY == 2))) {
            return false;
        }

        //Destination square
        Pieces dest = Board.getPieceAt(newY, newX);
        if (dest == null) {
            return true; // empty square
        } else {
            return dest.getColour() != this.getColour(); // capture only opponent
        }
    }
}
