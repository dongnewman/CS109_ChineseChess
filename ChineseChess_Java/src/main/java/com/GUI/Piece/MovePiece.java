package com.GUI.Piece;

public class MovePiece {
    
    public boolean Move(PiecesSession session, int fromRow, int fromCol, int toRow, int toCol) {
        Pieces piece = null;
        // 先找到棋子的对象
        try{
            piece = session.getPiece(fromRow, fromCol);
            if(piece == null){
                throw new Exception("No piece at the source position.");
            }
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
        // 确保找到了

        // 将棋子移动到目标位置
        


    }

}
