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
        Player player1 = new Player(input.nextLine(), true);  // true = white
        System.out.print("Enter name 2: ");
        Player player2 = new Player(input.nextLine(), false); // false = black

        // -------------------
        // Setup board pieces
        // -------------------
        for (int j = 0; j < 8; j++) {
            switch (j) {
                case 0, 7 -> board[0][j] = new Rook(false);
                case 1, 6 -> board[0][j] = new Knight(false);
                case 2, 5 -> board[0][j] = new Bishop(false);
                case 3 -> board[0][j] = new Queen(false);
                case 4 -> board[0][j] = new King(false);
            }
            board[1][j] = new Pawn(false);

            board[6][j] = new Pawn(true);
            switch (j) {
                case 0, 7 -> board[7][j] = new Rook(true);
                case 1, 6 -> board[7][j] = new Knight(true);
                case 2, 5 -> board[7][j] = new Bishop(true);
                case 3 -> board[7][j] = new Queen(true);
                case 4 -> board[7][j] = new King(true);
            }
        }

        // -------------------
        // Game loop
        // -------------------
        while (inPlay) {
            printBoard();

            System.out.println(playing ? "White's turn: " + player1.name : "Black's turn: " + player2.name);

            System.out.println("Command: ");
            String command = input.next();
            if (command.equals("pip")) {
                if (playing) {
                    possibleMoves(player1);
                } else {
                    possibleMoves(player2);
                }
            }

            System.out.print("Enter piece letter: ");

            int x = input.next().toUpperCase().charAt(0) - 'A';

            System.out.print("Enter piece number: ");
            int y = input.nextInt();

            Pieces temp = board[y - 1][x]; // row=y-1, col=x

            System.out.print("Enter destination letter: ");
            int destX = input.next().toUpperCase().charAt(0) - 'A';

            System.out.print("Enter destination number: ");
            int destY = input.nextInt();

            boolean[] b;
            if (playing) {
                b = isPlaying(player1, player2, temp, x, y, destX, destY);
                if (b[0]) {
                    playing = false;
                }
            } else {
                b = isPlaying(player2, player1, temp, x, y, destX, destY);
                if (b[0]) {
                    playing = true;
                }
            }
            if (!b[1]) {
                inPlay = false;
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

    public static boolean[] isPlaying(Player currentPlayer, Player oppPlayer, Pieces temp, int x, int y, int destX, int destY) throws InterruptedException {
        if (temp == null || temp.getColour() != currentPlayer.colour) {
            System.out.println("Not your piece!");
            System.out.println();
            Thread.sleep(2000);
            return new boolean[]{false, true};
        }

        if (currentPlayer.makeMove(temp, x, y - 1, destX, destY - 1)) {
            // Make move
            board[destY - 1][destX] = temp;
            board[y - 1][x] = null;
            System.out.println();

            // Find current player's king
            int kingRow = -1, kingCol = -1;
            Pieces king = null;
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    if (board[r][c] instanceof King && board[r][c].getColour() == currentPlayer.colour) {
                        king = board[r][c];
                        kingRow = r;
                        kingCol = c;
                    }
                }
            }
            if (king != null && inCheck(king, kingCol, kingRow)) {
                System.out.println("Illegal move! Your king would be in check.");
                // Undo move
                board[y - 1][x] = temp;
                board[destY - 1][destX] = null;
                return new boolean[]{false, true};
            }

            // Check opponent's king
            Pieces oppKing = null;
            int oppKingRow = -1, oppKingCol = -1;
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    if (board[r][c] instanceof King && board[r][c].getColour() == oppPlayer.colour) {
                        oppKing = board[r][c];
                        oppKingRow = r;
                        oppKingCol = c;
                    }
                }
            }

            if (oppKing != null && inCheck(oppKing, oppKingCol, oppKingRow)) {
                System.out.println(oppPlayer.name + " is in CHECK!");
                if (!hasEscape(oppPlayer)) {
                    System.out.println("CHECKMATE! " + currentPlayer.name + " wins!");
                    return new boolean[]{false, false};

                }
            }

            return new boolean[]{true, true};
        } else {
            System.out.println("Invalid move! Try again.");
            System.out.println();
            Thread.sleep(2000);
            return new boolean[]{false, true};
        }
    }

    public static boolean inCheck(Pieces king, int kingX, int kingY) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Pieces p = board[r][c];
                if (p != null && p.getColour() != king.getColour()) {
                    if (p.validMove(c, r, kingX, kingY)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean hasEscape(Player player) {
        // Find player's king
        int kingRow = -1, kingCol = -1;
        Pieces king = null;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (board[r][c] instanceof King && board[r][c].getColour() == player.colour) {
                    king = board[r][c];
                    kingRow = r;
                    kingCol = c;
                }
            }
        }

        if (king == null) return false;

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Pieces piece = board[r][c];
                if (piece != null && piece.getColour() == player.colour) {
                    for (int newR = 0; newR < 8; newR++) {
                        for (int newC = 0; newC < 8; newC++) {
                            if (piece.validMove(c, r, newC, newR)) { // column,row order
                                // Tentatively move
                                Pieces captured = board[newR][newC];
                                board[newR][newC] = piece;
                                board[r][c] = null;

                                // Update king position if piece is king
                                int tempKingRow = (piece instanceof King) ? newR : kingRow;
                                int tempKingCol = (piece instanceof King) ? newC : kingCol;

                                boolean stillInCheck = inCheck(king, tempKingCol, tempKingRow);

                                // Undo move
                                board[r][c] = piece;
                                board[newR][newC] = captured;

                                if (!stillInCheck) return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static void possibleMoves(Player player) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Pieces p = board[r][c];
                if (p != null && p.getColour() == player.colour) {
                    for (int newR = 0; newR < 8; newR++) {
                        for (int newC = 0; newC < 8; newC++) {
                            if (p.validMove(c, r, newC, newR)) {
                                System.out.println(p.getSymbol() + " " + ((char) ('A' + c)) + "" + (r + 1) + " to " + ((char) ('A' + newC)) + (newR + 1));

                            }
                        }
                    }
                }
            }
        }
    }

}
