package com.Controller.playersAI;

import com.Controller.playroom.*;

import java.util.ArrayList;

class Estimation {
    static final int R = 1000, N = 500, B = 200, A = 150, K = 10000, C = 510, P = 50;

    public static int est(Board board) {
        int score = 0;
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 9; j++) {
                char piece = board.getPiece(i, j);
                if (piece == '.')
                    continue;
                int value = 0;
                if (piece == 'R' || piece == 'r')
                    value = R;
                else if (piece == 'N' || piece == 'n')
                    value = N;
                else if (piece == 'B' || piece == 'b')
                    value = B;
                else if (piece == 'A' || piece == 'a')
                    value = A;
                else if (piece == 'K' || piece == 'k')
                    value = K;
                else if (piece == 'C' || piece == 'c')
                    value = C;
                else
                    value = P;
                if (Character.isUpperCase(piece)) {
                    score += value;
                } else {
                    score -= value;
                }
            }
        }
        return score;
    }
}

public class HinatsuruAI {

    static int Min = -1000000, Max = 1000000;
    static Move bestMove = null;

    private int minimax(Board board, int resdep, int alpha, int beta, boolean firstStep) {
        if (resdep == 0) {
            return Estimation.est(board);
        }
        if (Math.abs(Estimation.est(board)) > 900000) {// check for checkmate
            return board.getSide() ? Max : Min;
        }
        ArrayList<Move> moves;
        if (firstStep)
            moves = board.getAllPossibleMoves();
        else
            moves = board.getAllValidMoves();
        boolean side = board.getSide();
        int ret = side ? Max : Min;
        if (firstStep)
            bestMove = moves.get(0);
        for (Move move : moves) {
            char originalPiece = board.getPiece(move.getxf(), move.getyf());
            board.doMove(move);
            int val = minimax(board, resdep - 1, alpha, beta, false);
            board.undoMove(move, originalPiece);
            if (!side) {
                if (firstStep && val > ret) {
                    bestMove = move;
                }
                ret = Math.max(val, ret);
                alpha = Math.max(alpha, ret);
            } else {
                if (firstStep && val < ret) {
                    bestMove = move;
                }
                ret = Math.min(val, ret);
                beta = Math.min(beta, ret);
            }
            if (alpha > beta)
                break;
        }
        return ret;
    }

    public Move makeMove(Board board) {
        Board copyBoard = new Board(board.getBoard(), board.getSide());// prevent modifying original board
        minimax(copyBoard, 5, Min, Max, true);
        return bestMove;
    }
}
