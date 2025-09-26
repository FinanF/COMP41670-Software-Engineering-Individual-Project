package com.example.game;

import com.example.pieces.*;

import java.util.Scanner;

public class Board {
    public static Pieces[][] board = new Pieces[8][8];

    public static void main(String[] args) throws InterruptedException {
        boolean inPlay = true;
        boolean playing = true;

        Scanner input = new Scanner(System.in);
        System.out.print("Enter name 1: ");
        Player player1 = new Player(input.nextLine(), true);
        System.out.print("Enter name 2: ");
        Player player2 = new Player(input.nextLine(), false);

        // -------------------
        // Setup board pieces
        // -------------------
        for (int j = 0; j < 8; j++) {
            switch (j) {
                case 0:
                case 7:
                    board[0][j] = new Rook(false);
                    break;
                case 1:
                case 6:
                    board[0][j] = new Knight(false);
                    break;
                case 2:
                case 5:
                    board[0][j] = new Bishop(false);
                    break;
                case 3:
                    board[0][j] = new Queen(false);
                    break;
                case 4:
                    board[0][j] = new King(false);
                    break;
            }
            board[1][j] = new Pawn(false);

            board[6][j] = new Pawn(true);
            switch (j) {
                case 0:
                case 7:
                    board[7][j] = new Rook(true);
                    break;
                case 1:
                case 6:
                    board[7][j] = new Knight(true);
                    break;
                case 2:
                case 5:
                    board[7][j] = new Bishop(true);
                    break;
                case 3:
                    board[7][j] = new Queen(true);
                    break;
                case 4:
                    board[7][j] = new King(true);
                    break;
            }
        }

        // -------------------
        // Game loop
        // -------------------
        while (inPlay) {
            printBoard();

            System.out.println(playing ? "White's turn: " + player1.name : "Black's turn: " + player2.name);

            System.out.print("Enter piece letter: ");
            int x = input.next().toUpperCase().charAt(0) - 'A';

            System.out.print("Enter piece number: ");
            int y = input.nextInt();

            Pieces temp = board[y - 1][x]; // row=y-1, col=x

            System.out.print("Enter destination letter: ");
            int destX = input.next().toUpperCase().charAt(0) - 'A';

            System.out.print("Enter destination number: ");
            int destY = input.nextInt();

            if (playing) {
                if (isPlaying(player1, temp, x, y, destX, destY)) {
                    playing = false;
                }
            } else {
                if (isPlaying(player2, temp, x, y, destX, destY)) {
                    playing = true;
                }
            }
        }
    }

    public static Pieces getPieceAt(int row, int col) {
        return board[row][col];
    }

    public static void printBoard() {
        for (int i = 0; i < 8; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == null) {
                    System.out.print("[**]");
                } else {
                    System.out.print("[" + board[i][j].getSymbol() + "]");
                }
            }
            System.out.println();
        }
        System.out.print("   ");
        for (int k = 0; k < 8; k++) {
            char boardX = (char) ('A' + k);
            System.out.print(" " + boardX + "  ");
        }
        System.out.println();
    }

    public static boolean isPlaying(Player player, Pieces temp, int x, int y, int destX, int destY) throws InterruptedException {
        if (temp.getColour() == player.colour) {
            if (player.makeMove(temp, x, y - 1, destX, destY - 1)) {
                board[destY - 1][destX] = temp;
                board[y - 1][x] = null;
                System.out.println();
                return true;
            } else {
                System.out.println("Invalid move! Try again.");
                System.out.println();
                Thread.sleep(2000);
                return false;
            }
        } else {
            System.out.println("Not your piece!");
            System.out.println();
            Thread.sleep(2000);
            return false;
        }
    }

    public static boolean inCheck(Player player, Pieces king, int kingCol, int kingRow) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Pieces p = board[r][c];
                if (p != null && p.getColour() != king.getColour()) {
                    // If opponent piece can move to the king's square
                    if (p.validMove(c, r, kingCol, kingRow)) {
                        return true;
                    }
                }
            }
        }
        return false;

    }
}
