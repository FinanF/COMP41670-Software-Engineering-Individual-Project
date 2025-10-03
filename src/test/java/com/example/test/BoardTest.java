package com.example.test;

import com.example.game.Board;
import com.example.pieces.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @BeforeEach
    void setupBoard() {
        Board.boardSet(); // reset board before each test
    }

    @Test
    @DisplayName("getPieceAt should return correct piece or null")
    void getPieceAt() {
        Pieces rook = Board.getPieceAt(0, 0);
        assertInstanceOf(Rook.class, rook, "A rook should be at A1");
        Pieces empty = Board.getPieceAt(4, 4);
        assertNull(empty, "Middle board square should be empty at start");
    }

    @Test
    @DisplayName("Board should be initialized correctly with 32 pieces")
    void boardSet() {
        int pieceCount = 0;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (Board.board[r][c] != null) pieceCount++;
            }
        }
        assertEquals(32, pieceCount, "Initial setup should contain 32 pieces");
    }

    @Test
    @DisplayName("Initial position: no king should be in check")
    void inCheck() {
        Pieces whiteKing = Board.getPieceAt(7, 4);
        assertFalse(Board.inCheck(whiteKing, 4, 7), "White king should not be in check at start");
        Pieces blackKing = Board.getPieceAt(0, 4);
        assertFalse(Board.inCheck(blackKing, 4, 0), "Black king should not be in check at start");
    }
}