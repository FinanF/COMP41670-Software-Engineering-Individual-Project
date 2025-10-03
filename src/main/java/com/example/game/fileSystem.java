package com.example.game;

import com.example.pieces.*;

import java.io.*;

import static com.example.game.Board.board;

public class fileSystem {

    public static void saveBoard(File boardFile) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(boardFile))) {
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    Pieces piece = board[r][c];
                    if (piece == null) {
                        writer.print("**,null");
                    } else {
                        writer.print(piece.getSymbol() + "," + piece.getColour());
                    }
                    if (c < 7) writer.print(";");
                }
                writer.println();
            }
        }
    }

    public static void loadBoard(File boardFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(boardFile))) {
            String line;
            int r = 0;
            while ((line = reader.readLine()) != null && r < 8) {
                String[] cells = line.split(";");
                for (int c = 0; c < 8; c++) {
                    String[] parts = cells[c].split(",");
                    String symbol = parts[0];
                    String colourStr = parts[1];

                    if (symbol.equals("**")) {
                        assert board[r] != null;
                        board[r][c] = null;
                    } else {
                        boolean colour = Boolean.parseBoolean(colourStr);
                        assert board[r] != null;
                        board[r][c] = createPieceFromSymbol(symbol, colour);
                    }
                }
                r++;
            }
        }
    }

    // Helper to rebuild piece objects
    private static Pieces createPieceFromSymbol(String symbol, boolean colour) {
        return switch (symbol) {
            case "wP", "bP" -> new Pawn(colour);
            case "wR", "bR" -> new Rook(colour);
            case "wN", "bN", "wk", "bk" -> new Knight(colour);
            case "wB", "bB" -> new Bishop(colour);
            case "wQ", "bQ" -> new Queen(colour);
            case "wK", "bK" -> new King(colour);
            default -> null;
        };
    }

    public static void savePlayers(File playerFile, Player p1, Player p2) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(playerFile))) {
            writer.println(p1.name + "," + p1.colour);
            writer.println(p2.name + "," + p2.colour);
        }
    }

    public static Player[] loadPlayers(File playerFile) throws IOException {
        Player[] players = new Player[2];
        try (BufferedReader reader = new BufferedReader(new FileReader(playerFile))) {
            for (int i = 0; i < 2; i++) {
                String[] parts = reader.readLine().split(",");
                players[i] = new Player(parts[0], Boolean.parseBoolean(parts[1]));
            }
        }
        return players;
    }

}
