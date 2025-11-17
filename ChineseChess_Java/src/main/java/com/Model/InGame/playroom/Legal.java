package com.Model.InGame.playroom;

import java.util.ArrayList;

/**
 * Helper class that produces possible moves for a piece (not fully
 * rule-complete). For now it
 * generates candidate destination squares and performs simple piece-independent
 * checks. You can
 * extend this with full piece movement rules later.
 */
public final class Legal extends PieceProtect {

    public static boolean isLegalMove(Board board, Move move) {
        // wait for implementation
        if (!ValidMove.isValidMove(board, move))
            return false;
        char originalPiece = board.getPiece(move.getxf(), move.getyf());
        board.doMove(move);
        ArrayList<Move> validMoves = board.getAllValidMoves();
        boolean flag = true;
        for (Move m : validMoves) {
            char piece = board.getPiece(m.getxf(), m.getyf());
            if (piece == 'K' || piece == 'k') {
                flag = false;
                break;
            }
        }
        board.undoMove(move, originalPiece);
        return flag;
    }

    public static ArrayList<Move> getAllLegalMoves(Board board) {
        // wait for implementation
        ArrayList<Move> validMoves = board.getAllValidMoves();
        ArrayList<Move> legalMoves = new ArrayList<>();
        for (Move m : validMoves) {
            if (board.isLegalMove(m)) {
                legalMoves.add(m);
            }
        }
        return legalMoves;
    }

    public static boolean fastCheckLegal(Board board, Move move) {
        // this is check in now side, if its legal to do this move
        boolean side = board.getSide();
        char originalPiece = board.getPiece(move.getxf(), move.getyf());
        board.doMove(move);
        int[] kingPos = findKingPos(board, side);
        boolean attacked = (isAttacked(board, kingPos[0], kingPos[1]) || kingFacing(board));
        board.undoMove(move, originalPiece);
        return !attacked;
    }

    public static boolean isInCheck(Board board) {
        int[] kingPos = findKingPos(board, board.getSide());
        return isAttacked(board, kingPos[0], kingPos[1]);
        // In check dont need to check kingfacing
    }
}