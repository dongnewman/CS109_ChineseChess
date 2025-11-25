package com.GUI.GameObjects.Piece;

public class PiecesSession {
    // 使用 1-based 存放：有效行 1..10，有效列 1..9
    // 为了简单性，数组多留一行/列（索引 0 未使用）
    private Pieces[][] piecesArray = new Pieces[11][10];

    // 被吃掉的棋子存放处
    private Pieces[] BlackRemovedPieces = new Pieces[16];
    private Pieces[] RedRemovedPieces = new Pieces[16];
    private int blackRemovedCount = 0;
    private int redRemovedCount = 0;

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

    // 记录被吃掉的棋子
    public int addRemovedPiece(Pieces piece, boolean isBlack) {
        if (isBlack) {
            if (blackRemovedCount >= 16) return -1;
            BlackRemovedPieces[blackRemovedCount++] = piece;
            return blackRemovedCount - 1;
        } else {
            if (redRemovedCount >= 16) return -1;
            RedRemovedPieces[redRemovedCount++] = piece;
            return redRemovedCount - 1;
        }
    }

    public Pieces[][] getPiecesArray() {
        return piecesArray;
    }

    public void setPiecesArray(Pieces[][] piecesArray) {
        this.piecesArray = piecesArray;
    }

    public Pieces[] getBlackRemovedPieces() {
        return BlackRemovedPieces;
    }

    public void setBlackRemovedPieces(Pieces[] blackRemovedPieces) {
        BlackRemovedPieces = blackRemovedPieces;
    }

    public Pieces[] getRedRemovedPieces() {
        return RedRemovedPieces;
    }

    public void setRedRemovedPieces(Pieces[] redRemovedPieces) {
        RedRemovedPieces = redRemovedPieces;
    }

    public int getBlackRemovedCount() {
        return blackRemovedCount;
    }

    public void setBlackRemovedCount(int blackRemovedCount) {
        this.blackRemovedCount = blackRemovedCount;
    }

    public int getRedRemovedCount() {
        return redRemovedCount;
    }

    public void setRedRemovedCount(int redRemovedCount) {
        this.redRemovedCount = redRemovedCount;
    }
}
