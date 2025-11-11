package com.Controller.playroom;

import java.util.ArrayList;

/**
 * Helper class that produces possible moves for a piece (not fully
 * rule-complete). For now it
 * generates candidate destination squares and performs simple piece-independent
 * checks. You can
 * extend this with full piece movement rules later.
 */
public final class Possible {
    public static boolean isPossibleMove(Board board, Move move) {
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

    public static ArrayList<Move> getAllPossibleMoves(Board board) {
        // wait for implementation
        ArrayList<Move> validMoves = board.getAllValidMoves();
        ArrayList<Move> possibleMoves = new ArrayList<>();
        for (Move m : validMoves) {
            if (board.isPossibleMove(m)) {
                possibleMoves.add(m);
            }
        }
        return possibleMoves;
    }
}