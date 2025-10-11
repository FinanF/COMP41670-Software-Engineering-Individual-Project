package com.example.game;

import com.example.pieces.Pieces;

public class Player {
    public String name;
    public boolean colour;

    public Player(String name, boolean colour) {
        this.name = name;
        this.colour = colour;
    }

    public String getColour() {
        return colour ? "White" : "Black";
    }

    public boolean makeMove(Pieces piece, int x, int y, int newX, int newY) {
        if (piece == null) {
            return false;
        } else if (piece.getColour() == colour) {
            return piece.validMove(x, y, newX, newY);
        }
        return false;
    }
}
