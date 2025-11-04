package com.GUI.Piece;

public class PiecesSession {
    // 使用 1-based 存放：有效行 1..10，有效列 1..9
    // 为了简单性，数组多留一行/列（索引 0 未使用）
    private Pieces[][] piecesArray = new Pieces[11][10];

    /**
     * 将棋子放入 session，索引为 1-based（row:1..10, col:1..9）。
     * 如果索引越界则忽略并打印错误。
     */
    public void setPiece(int row, int col, Pieces piece){
        if (row < 1 || row > 10 || col < 1 || col > 9) {
            System.err.println("PiecesSession.setPiece: invalid index row=" + row + ", col=" + col);
            return;
        }
        piecesArray[row][col] = piece;
    }

    /**
     * 获取棋子，使用 1-based 索引。越界返回 null。
     */
    public Pieces getPiece(int row, int col){
        if (row < 1 || row > 10 || col < 1 || col > 9) {
            return null;
        }
        return piecesArray[row][col];
    }
}
