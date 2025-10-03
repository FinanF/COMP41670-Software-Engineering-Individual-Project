package com.example.pieces;

public class Queen extends Pieces {
    public Queen(boolean colour) {
        super(colour);
    }

    @Override
    public String getSymbol() {
        return colour ? "wQ" : "bQ";
    }

    @Override
    public boolean validMove(int x, int y, int newX, int newY) {
        Rook rook = new Rook(this.getColour());
        Bishop bishop = new Bishop(this.getColour());

        return rook.validMove(x, y, newX, newY) || bishop.validMove(x, y, newX, newY);
    }
}
