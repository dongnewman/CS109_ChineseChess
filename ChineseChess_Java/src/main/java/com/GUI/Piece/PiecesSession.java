package com.GUI.Piece;

public class PiecesSession {
    private Object[][] piecesArray = new Object[10][9];

    public void setPiece(int row, int col, Object piece){
        piecesArray[row][col] = piece;
    }

    public Object getPiece(int row, int col){
        return piecesArray[row][col];
    }
}
