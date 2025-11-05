package com.GUI.Piece;

import com.Controller.InGameObjects;

public class RemovePiece {
    private PiecesSession piecesSession;

    public RemovePiece(PiecesSession piecesSession) {
        this.piecesSession = piecesSession;
    }

    /**
     * 移除指定位置的棋子，使用 1-based 索引（row:1..10, col:1..9）。
     * 
     * 使用示例 ：remove(row, col, isBlack)
     */
    public boolean remove(int row, int col, boolean isBlack) {
        Pieces piece = piecesSession.getPiece(row, col); // 先拿到棋子的对象引用
        if (piece != null) {
            int place = piecesSession.addRemovedPiece(piece, isBlack); // 拿到被移除的棋子在观战区的位置
            MovePiece movePiece = new MovePiece(InGameObjects.plate, InGameObjects.piecesSession);

            // 通过动画移动到一旁的观战区
            boolean success = false;
            if (isBlack) {
                if (place >= 0 && place <= 7) {
                    success = movePiece.moveByPixel(row, col, (755 + place * 40), 290);
                }
                if (place >= 8 && place <= 15) {
                    success = movePiece.moveByPixel(row, col, (755 + (place - 8) * 40), 220);
                }
            }
            else {
                if (place >= 0 && place <= 7) {
                    success = movePiece.moveByPixel(row, col, (755 + place * 40), 520);
                }
                if (place >= 8 && place <= 15) {
                    success = movePiece.moveByPixel(row, col, (755 + (place - 8) * 40), 590);
                }
            }
            
            return success;
        } 
        else {
            System.err.println("RemovePiece.remove: no piece at row=" + row + ", col=" + col);
            return false;
        }
    }
}
