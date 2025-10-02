package com.example.game;

import com.example.pieces.*;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static com.example.game.fileSystem.loadBoard;
import static com.example.game.fileSystem.loadPlayers;

public class Board {
    public static lastMove lastMove = null;
    public static Pieces[][] board = new Pieces[8][8];

    public static void main(String[] args) throws InterruptedException, IOException {
        Scanner input = new Scanner(System.in);



        File boardFile = new File("board.csv");
        File playerFile = new File("player.csv");

        Player player1, player2;

        // -------------------
        // Setup board pieces
        // -------------------
        System.out.print("1. Load game, 2. New game: ");
        String choice = input.nextLine().toLowerCase();
        if ("1. load".contains(choice)) {
            // Load existing game
            loadBoard(boardFile);
            Player[] pArray = loadPlayers(playerFile);
            player1 = pArray[0];
            player2 = pArray[1];
        } else {
            // New game
            System.out.print("Enter name 1 (white): ");
            player1 = new Player(input.nextLine(), true);  // true = white
            System.out.print("Enter name 2 (black): ");
            player2 = new Player(input.nextLine(), false); // false = black
            boardSet();
        }

        // -------------------
        // Game loop
        // -------------------
        boolean inPlay = true;
        boolean playing = player2.colour; // true = white's turn, false = black's turn
        while (inPlay) {
            printBoard();

            // Show whose turn
            if (playing) {
                System.out.println(player1.name + " " + "(" + player1.getColour() + ")" + " to move");
            } else {
                System.out.println(player2.name + " " + "(" + player2.getColour() + ")" + " to move");
            }

            // Ask for input
            System.out.println("Enter move (e.g. a2a4) or command (pip, save, last, help, exit):");
            String inputStr = input.nextLine().trim().toLowerCase();

            // -------------------
            // Commands
            // -------------------
            switch (inputStr) {
                case "pip":
                    if (playing) possibleMoves(player1);
                    else possibleMoves(player2);
                    continue;

                case "save":
                    fileSystem.saveBoard(boardFile);
                    if (playing) fileSystem.savePlayers(playerFile, player1, player2);
                    else fileSystem.savePlayers(playerFile, player2, player1);
                    System.out.println("Game saved.");
                    continue;

                case "last":
                    lastMoves();
                    continue;

                case "help":
                    System.out.println("Commands: pip | save | last | help | exit");
                    continue;

                case "exit":
                    System.out.println("Exiting game.");
                    inPlay = false;
                    continue;
            }

            // -------------------
            // Otherwise treat as move
            // -------------------
            if (inputStr.length() != 4) {
                System.out.println("Invalid input. Type command or move (e.g. a2a4).");
                continue;
            }

            String move = inputStr.toUpperCase();
            char file1 = move.charAt(0);
            char rank1 = move.charAt(1);
            char file2 = move.charAt(2);
            char rank2 = move.charAt(3);

            // Validate files and ranks
            if (file1 < 'A' || file1 > 'H' || file2 < 'A' || file2 > 'H' ||
                    rank1 < '1' || rank1 > '8' || rank2 < '1' || rank2 > '8') {
                System.out.println("Move out of bounds. Files A-H, ranks 1-8.");
                continue;
            }

            int x = file1 - 'A';                 // col 0..7
            int y = Character.getNumericValue(rank1); // rank 1..8
            int destX = file2 - 'A';
            int destY = Character.getNumericValue(rank2);

            Pieces temp = getPieceAt(y - 1, x); // board[y-1][x]

            // -------------------
            // Play turn
            // -------------------
            boolean[] result;
            if (playing) {
                result = isPlaying(player1, player2, temp, x, y, destX, destY);
                if (result[0]) playing = false; // valid move -> switch turn
            } else {
                result = isPlaying(player2, player1, temp, x, y, destX, destY);
                if (result[0]) playing = true; // valid move -> switch turn
            }

            if (!result[1]) { // game ended
                inPlay = false;
            }
        } // end while inPlay

        input.close();
    }

    // Safe getter
    public static Pieces getPieceAt(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8) return null;
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

    public static void boardSet() {
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
    }

    /**
     * Attempt move for currentPlayer. Returns {moveSucceeded, continueGame}.
     * x,y,destX,destY: x,destX are columns 0..7 (A..H), y,destY are ranks 1..8 (user input).
     */
    public static boolean[] isPlaying(Player currentPlayer, Player oppPlayer,
                                      Pieces temp, int x, int y, int destX, int destY) throws InterruptedException {
        int fromRow = y - 1;
        int toRow = destY - 1;

        // =========================
        // Bounds check
        // =========================
        if (x < 0 || x > 7 || destX < 0 || destX > 7 ||
                y < 1 || y > 8 || destY < 1 || destY > 8) {
            System.out.println("Move out of bounds.");
            Thread.sleep(1000);
            return new boolean[]{false, true};
        }

        // =========================
        // Piece ownership check
        // =========================
        if (temp == null || temp.getColour() != currentPlayer.colour) {
            System.out.println("Not your piece!");
            Thread.sleep(1000);
            return new boolean[]{false, true};
        }

        // =====================================================
        // Special Move: Castling (check BEFORE tentative move)
        // =====================================================
        if (temp instanceof King kingPiece) {
            if (!kingPiece.hasMoved && y == destY && Math.abs(destX - x) == 2) {

                // King-side castling
                if (destX == x + 2) {
                    Pieces rook = board[fromRow][7];
                    if (rook instanceof Rook r && !r.hasMoved) {
                        if (board[fromRow][5] == null && board[fromRow][6] == null) {
                            if (!inCheck(kingPiece, x, fromRow) &&
                                    !inCheck(kingPiece, x + 1, fromRow) &&
                                    !inCheck(kingPiece, x + 2, fromRow)) {

                                // Perform castling
                                board[fromRow][x] = null;
                                board[fromRow][7] = null;
                                board[fromRow][destX] = kingPiece;
                                board[fromRow][5] = r;

                                kingPiece.hasMoved = true;
                                r.hasMoved = true;

                                saveLastMove(kingPiece, x, fromRow, destX, fromRow);
                                return new boolean[]{true, true};
                            }
                        }
                    }
                }

                // Queen-side castling
                if (destX == x - 2) {
                    Pieces rook = board[fromRow][0];
                    if (rook instanceof Rook r && !r.hasMoved) {
                        if (board[fromRow][1] == null && board[fromRow][2] == null && board[fromRow][3] == null) {
                            if (!inCheck(kingPiece, x, fromRow) &&
                                    !inCheck(kingPiece, x - 1, fromRow) &&
                                    !inCheck(kingPiece, x - 2, fromRow)) {

                                // Perform castling
                                board[fromRow][x] = null;
                                board[fromRow][0] = null;
                                board[fromRow][destX] = kingPiece;
                                board[fromRow][3] = r;

                                kingPiece.hasMoved = true;
                                r.hasMoved = true;

                                saveLastMove(kingPiece, x, fromRow, destX, fromRow);
                                return new boolean[]{true, true};
                            }
                        }
                    }
                }
            }
        }

        // =====================================================
        // Normal Move Validation
        // =====================================================
        if (!currentPlayer.makeMove(temp, x, fromRow, destX, toRow)) {
            System.out.println("Invalid move! Try again.");
            Thread.sleep(1000);
            return new boolean[]{false, true};
        }

        // =========================
        // Tentative move
        // =========================
        Pieces captured = board[toRow][destX];
        board[toRow][destX] = temp;
        board[fromRow][x] = null;

        //===========================
        // Special case: en passant
        //===========================
        boolean enPassant = false;
        Pieces enPassantCaptured = null;
        if (temp instanceof Pawn && x != destX && captured == null) {
            enPassant = true;
            enPassantCaptured = board[fromRow][destX]; // pawn to capture
            board[fromRow][destX] = null;
        }

        // =========================
        // King safety check
        // =========================
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

            // Undo
            board[fromRow][x] = temp;
            board[toRow][destX] = captured;
            if (enPassant) board[fromRow][destX] = enPassantCaptured;

            Thread.sleep(1000);
            return new boolean[]{false, true};
        }

        // =========================
        // Opponent check/checkmate
        // =========================
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

        // =========================
        // Final updates
        // =========================
        if (temp instanceof Pawn) {
            ((Pawn) temp).firstMove = false;
        }
        if (temp instanceof King) {
            ((King) temp).hasMoved = true;
        }
        if (temp instanceof Rook) {
            ((Rook) temp).hasMoved = true;
        }

        saveLastMove(temp, x, fromRow, destX, toRow);
        return new boolean[]{true, true};
    }

    private static void saveLastMove(Pieces piece, int x, int y, int destX, int destY) {
        lastMove = new lastMove();
        lastMove.put(piece, x, y, destX, destY);
    }

    public static boolean inCheck(Pieces king, int kingX, int kingY) {
        // kingX = col, kingY = row
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Pieces p = board[r][c];
                if (p != null && p.getColour() != king.getColour()) {
                    // p.validMove expects (fromCol, fromRow, toCol, toRow)
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

        // Try every possible move
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Pieces piece = board[r][c];
                if (piece != null && piece.getColour() == player.colour) {
                    for (int newR = 0; newR < 8; newR++) {
                        for (int newC = 0; newC < 8; newC++) {
                            if (piece.validMove(c, r, newC, newR)) { // fromCol, fromRow, toCol, toRow
                                // Tentatively move
                                Pieces captured = board[newR][newC];
                                board[newR][newC] = piece;
                                board[r][c] = null;

                                // Update king position if piece is king
                                int tempKingRow = (piece instanceof King) ? newR : kingRow;
                                int tempKingCol = (piece instanceof King) ? newC : kingCol;
                                Pieces kingRef = (piece instanceof King) ? piece : king;

                                boolean stillInCheck = inCheck(kingRef, tempKingCol, tempKingRow);

                                // Undo
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

    public static void lastMoves() {
        if (lastMove == null) {
            System.out.println("No moves yet.");
            return;
        }
        char fromFile = (char) ('A' + lastMove.fromX);
        char toFile = (char) ('A' + lastMove.toX);
        System.out.println(lastMove.piece.getSymbol() + " " + fromFile + (lastMove.fromY + 1) +
                " -> " + toFile + (lastMove.toY + 1));
    }

    public static class lastMove {
        public Pieces piece;
        public int fromX, fromY, toX, toY; // fromY/toY stored as board rows (0..7)

        public void put(Pieces piece, int fromX, int fromY, int toX, int toY) {
            this.piece = piece;
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
        }
    }
}
