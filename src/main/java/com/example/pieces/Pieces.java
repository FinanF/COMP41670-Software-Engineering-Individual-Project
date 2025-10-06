package com.example.pieces;

/**
 * Pieces class that has abstract methods for the pieces to override
 */
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

    /**
     * Returns the piece symbol for the board given the piece colour
     *
     * @return
     */
    public abstract String getSymbol();

    /**
     * Abstract method for each piece to apply their own move logic
     * @param x
     * @param y
     * @param newX
     * @param newY
     * @return
     */
    public abstract boolean validMove(int x, int y, int newX, int newY);
}
