package com.example.pieces;


public abstract class Pieces {
    public boolean colour;

    public Pieces(boolean colour) {
        this.colour = colour;
    }

    public Pieces() {
    }

    public boolean getColour() {
        return colour;
    }

    public abstract String getSymbol();

    public abstract boolean validMove(int x, int y, int newX, int newY);
}
