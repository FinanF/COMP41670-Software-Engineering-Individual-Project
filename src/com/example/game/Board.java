package com.example.game;

import com.example.pieces.*;

public class Board {
    public static void main(String[] args) {
        Pieces board[][] = new Pieces[8][8];
        board[0][0] = new Rook();
        board[0][1] = new Knight();
        board[0][2] = new Bishop();
        board[0][3] = new Queen();
        board[0][4] = new King();
        board[0][5] = new Bishop();
        board[0][6] = new Knight();
        board[0][7] = new Rook();
        for (int i = 0; i < 8; i++) {
            switch (i) {
                case 0:
                    board[0][i] = new Rook();
                case 1:
                    board[0][i] = new Knight();
                case 2:
                    board[0][i] = new Bishop();
                case 3:
                    board[0][i] = new Queen();
                case 4:
                    board[0][i] = new King();
                case 5:
                    board[0][i] = new Bishop();
                case 6:
                    board[0][i] = new Knight();
                case 7:
                    board[0][i] = new Rook();
            }
        }
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn();
        }

        for (int i = 0; i < 8; i++) {
            board[6][i] = new Pawn();
        }

        for (int i = 0; i < 8; i++) {
            switch (i) {
                case 0:
                    board[7][i] = new Rook();
                case 1:
                    board[7][i] = new Knight();
                case 2:
                    board[7][i] = new Bishop();
                case 3:
                    board[7][i] = new Queen();
                case 4:
                    board[7][i] = new King();
                case 5:
                    board[7][i] = new Bishop();
                case 6:
                    board[7][i] = new Knight();
                case 7:
                    board[7][i] = new Rook();
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == null) {
                    System.out.print("[**] ");
                } else if (board[i][j] instanceof Pawn) {
                    System.out.print("[P] ");
                } else if (board[i][j] instanceof Rook) {
                    System.out.print("[R] ");
                } else if (board[i][j] instanceof Knight) {
                    System.out.print("[k] ");
                } else if (board[i][j] instanceof Bishop) {
                    System.out.print("[B] ");
                } else if (board[i][j] instanceof King) {
                    System.out.print("[K] ");
                } else if (board[i][j] instanceof Queen) {
                    System.out.print("[Q] ");
                }
            }
        }
    }
}
