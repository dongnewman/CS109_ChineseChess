package com.Model.InGame.playroom;

import java.util.ArrayList;

import com.Controller.InGameObjects;

public class Board {
    private char board[][];
    boolean side;
    private long zobristKey;

    // constructor: copy from given array (expects at least 11x10)
    public Board(char b[][], boolean side) {
        this.side = side;
        board = new char[11][10];
        for (int i = 0; i <= 10; i++) {
            for (int j = 0; j <= 9; j++) {
                board[i][j] = b[i][j];
            }
        }
        // initialize zobrist key
        zobristKey = com.Model.InGame.playersAI.Zobrist.computeKey(this);
        InGameObjects.board = this;
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
        // initialize zobrist key
        zobristKey = com.Model.InGame.playersAI.Zobrist.computeKey(this);
        InGameObjects.board = this;
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

    public long getZobristKey() {
        return zobristKey;
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
        int xi = move.getxi(), yi = move.getyi();
        int xf = move.getxf(), yf = move.getyf();
        char piece = getPiece(xi, yi);
        char captured = getPiece(xf, yf);
        // incremental zobrist: remove piece from source, remove captured at dest, add
        // piece at dest, flip side
        zobristKey ^= com.Model.InGame.playersAI.Zobrist.keyFor(piece, xi, yi);
        if (captured != '.') {
            zobristKey ^= com.Model.InGame.playersAI.Zobrist.keyFor(captured, xf, yf);
        }
        zobristKey ^= com.Model.InGame.playersAI.Zobrist.keyFor(piece, xf, yf);
        // apply move on board
        setPiece(xf, yf, piece);
        setPiece(xi, yi, '.');
        // toggle side and hash
        side = !side;
        zobristKey ^= com.Model.InGame.playersAI.Zobrist.sideKey();
    }

    public void undoMove(Move move, char capturedPiece) {
        int xi = move.getxi(), yi = move.getyi();
        int xf = move.getxf(), yf = move.getyf();
        char moving = getPiece(xf, yf);
        // toggle side and hash (reverse of doMove)
        side = !side;
        zobristKey ^= com.Model.InGame.playersAI.Zobrist.sideKey();
        // remove moving piece from dest, restore captured if any, add moving back to
        // source
        zobristKey ^= com.Model.InGame.playersAI.Zobrist.keyFor(moving, xf, yf);
        if (capturedPiece != '.') {
            zobristKey ^= com.Model.InGame.playersAI.Zobrist.keyFor(capturedPiece, xf, yf);
        }
        zobristKey ^= com.Model.InGame.playersAI.Zobrist.keyFor(moving, xi, yi);
        // apply board state
        setPiece(xi, yi, moving);
        setPiece(xf, yf, capturedPiece);
    }

    public boolean gameOver() {
        ArrayList<Move> possibleMoves = getAllPossibleMoves();
        return possibleMoves.isEmpty();
    }

}
