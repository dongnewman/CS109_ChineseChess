package com.Controller.playroom;

import java.util.ArrayList;

public class Board {
    private char board[][];
    boolean side;

    // constructor: copy from given array (expects at least 11x10)
    public Board(char b[][], boolean side) {
        this.side = side;
        board = new char[11][10];
        for (int i = 0; i <= 10; i++) {
            for (int j = 0; j <= 9; j++) {
                board[i][j] = b[i][j];
            }
        }
    }

    public Board() {
        board = new char[11][10];
        // initialize empty board using '.' as empty
        for (int i = 0; i <= 10; i++) {
            for (int j = 0; j <= 9; j++) {
                board[i][j] = '.';
            }
        }
        // default initial placement (kept from original file)
        board[1][1] = 'R';
        board[1][2] = 'N';
        board[1][3] = 'B';
        board[1][4] = 'A';
        board[1][5] = 'K';
        board[1][6] = 'A';
        board[1][7] = 'B';
        board[1][8] = 'N';
        board[1][9] = 'R';
        board[3][2] = 'C';
        board[3][8] = 'C';
        board[4][1] = 'P';
        board[4][3] = 'P';
        board[4][5] = 'P';
        board[4][7] = 'P';
        board[4][9] = 'P';
        board[10][1] = 'r';
        board[10][2] = 'n';
        board[10][3] = 'b';
        board[10][4] = 'a';
        board[10][5] = 'k';
        board[10][6] = 'a';
        board[10][7] = 'b';
        board[10][8] = 'n';
        board[10][9] = 'r';
        board[8][2] = 'c';
        board[8][8] = 'c';
        board[7][1] = 'p';
        board[7][3] = 'p';
        board[7][5] = 'p';
        board[7][7] = 'p';
        board[7][9] = 'p';
        side = false;// false for red, true for black
    }

    // getter
    public char getPiece(int r, int c) {
        if (r < 0 || r > 10 || c < 0 || c > 9)
            return 0;
        return board[r][c];
    }

    public char[][] getBoard() {
        char[][] b = new char[11][10];// deep copy
        for (int i = 0; i <= 10; i++) {
            for (int j = 0; j <= 9; j++) {
                b[i][j] = board[i][j];
            }
        }
        return b;
    }

    public boolean getSide() {
        return side;
    }

    // setter
    public void setPiece(int r, int c, char p) {
        if (r < 0 || r > 10 || c < 0 || c > 9)
            return;
        board[r][c] = p;
    }

    // other methods
    public void printboard() {// for test
        System.out.print("  ");
        for (int i = 1; i <= 9; i++)
            System.out.print(" " + i);
        System.out.println();
        for (int i = 10; i >= 1; i--) {
            if (i != 10)
                System.out.print(" ");
            System.out.print(i + " ");
            for (int j = 1; j <= 9; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    // delegate to ValidMove and Possible helper classes
    public boolean isValidMove(Move move) {
        return ValidMove.isValidMove(this, move);
    }

    public ArrayList<Move> getAllValidMoves() {
        return ValidMove.getAllValidMoves(this);
    }

    public boolean isPossibleMove(Move move) {
        return Possible.isPossibleMove(this, move);
    }

    public ArrayList<Move> getAllPossibleMoves() {
        return Possible.getAllPossibleMoves(this);
    }

    public void doMove(Move move) {
        char piece = getPiece(move.getxi(), move.getyi());
        setPiece(move.getxf(), move.getyf(), piece);
        setPiece(move.getxi(), move.getyi(), '.');
        side = !side;
    }

    public void undoMove(Move move, char capturedPiece) {
        setPiece(move.getxi(), move.getyi(), getPiece(move.getxf(), move.getyf()));
        setPiece(move.getxf(), move.getyf(), capturedPiece);
        side = !side;
    }

    public boolean gameOver() {
        ArrayList<Move> possibleMoves = getAllPossibleMoves();
        return possibleMoves.isEmpty();
    }

}
