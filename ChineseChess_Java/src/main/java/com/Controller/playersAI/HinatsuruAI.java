package com.Controller.playersAI;

import com.Controller.playroom.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

    public static int pieceValue(char piece) {
        if (piece == '.')
            return 0;
        char p = Character.toUpperCase(piece);
        if (p == 'R')
            return R;
        if (p == 'N')
            return N;
        if (p == 'B')
            return B;
        if (p == 'A')
            return A;
        if (p == 'K')
            return K;
        if (p == 'C')
            return C;
        return P;
    }
}

public class HinatsuruAI {

    static int Min = -1000000, Max = 1000000;
    private Move bestMove = null;

    private int quiescence(Board board, int alpha, int beta) {
        int stand = Estimation.est(board);
        boolean side = board.getSide();
        if (!side) {
            if (stand >= beta)
                return stand;
            if (alpha < stand)
                alpha = stand;
        } else {
            if (stand <= alpha)
                return stand;
            if (beta > stand)
                beta = stand;
        }
        ArrayList<Move> moves = board.getAllValidMoves();
        ArrayList<Move> captures = new ArrayList<>();
        for (Move m : moves) {
            if (board.getPiece(m.getxf(), m.getyf()) != '.')
                captures.add(m);
        }
        // MVV-LVA: sort captures by victim value desc, attacker value asc
        Collections.sort(captures, new Comparator<Move>() {
            @Override
            public int compare(Move m1, Move m2) {
                char vic1 = board.getPiece(m1.getxf(), m1.getyf());
                char att1 = board.getPiece(m1.getxi(), m1.getyi());
                int score1 = Estimation.pieceValue(vic1) * 100 - Estimation.pieceValue(att1);

                char vic2 = board.getPiece(m2.getxf(), m2.getyf());
                char att2 = board.getPiece(m2.getxi(), m2.getyi());
                int score2 = Estimation.pieceValue(vic2) * 100 - Estimation.pieceValue(att2);

                return Integer.compare(score2, score1); // desc
            }
        });
        if (captures.isEmpty())
            return stand;
        int ret = stand;
        for (Move m : captures) {
            char captured = board.getPiece(m.getxf(), m.getyf());
            board.doMove(m);
            // quick king-safety check: skip captures that leave mover's king in check
            boolean movedSide = !board.getSide();
            int[] kp = KingProtect.findKingPos(board, movedSide);
            if (kp != null && KingProtect.isSquareAttacked(board, kp[0], kp[1]) && !KingProtect.kingFacing(board)) {
                board.undoMove(m, captured);
                continue;
            }
            try {
                int val = quiescence(board, alpha, beta);
                if (!side) {
                    if (val > ret)
                        ret = val;
                    if (ret > alpha)
                        alpha = ret;
                } else {
                    if (val < ret)
                        ret = val;
                    if (ret < beta)
                        beta = ret;
                }
            } finally {
                board.undoMove(m, captured);
            }
            if (alpha >= beta)
                break;
        }
        return ret;
    }

    private int minimax(Board board, int resdep, int alpha, int beta, boolean firstStep) {
        if (resdep == 0)
            return quiescence(board, alpha, beta);
        if (board.gameOver())
            return board.getSide() ? Max : Min;

        ArrayList<Move> moves = firstStep ? board.getAllPossibleMoves() : board.getAllValidMoves();
        boolean side = board.getSide();
        int ret = side ? Max : Min;
        if (moves.isEmpty())
            return ret;
        if (firstStep)
            bestMove = moves.get(0);

        // MVV-LVA ordering: sort moves so captures are searched first (victim high,
        // attacker low)
        Collections.sort(moves, new Comparator<Move>() {
            @Override
            public int compare(Move m1, Move m2) {
                char vic1 = board.getPiece(m1.getxf(), m1.getyf());
                char att1 = board.getPiece(m1.getxi(), m1.getyi());
                int score1 = Estimation.pieceValue(vic1) * 100 - Estimation.pieceValue(att1);

                char vic2 = board.getPiece(m2.getxf(), m2.getyf());
                char att2 = board.getPiece(m2.getxi(), m2.getyi());
                int score2 = Estimation.pieceValue(vic2) * 100 - Estimation.pieceValue(att2);

                return Integer.compare(score2, score1); // desc
            }
        });

        for (Move move : moves) {
            char originalPiece = board.getPiece(move.getxf(), move.getyf());
            board.doMove(move);
            // if non-root, quick king-safety check and skip if mover's king is attacked
            if (!firstStep) {
                boolean movedSide = !board.getSide();
                int[] kp = KingProtect.findKingPos(board, movedSide);
                if (kp != null && KingProtect.isSquareAttacked(board, kp[0], kp[1]) && !KingProtect.kingFacing(board)) {
                    board.undoMove(move, originalPiece);
                    continue;
                }
            }
            int val;
            try {
                val = minimax(board, resdep - 1, alpha, beta, false);
            } finally {
                board.undoMove(move, originalPiece);
            }
            if (!side) {
                if (firstStep && val > ret)
                    bestMove = move;
                ret = Math.max(val, ret);
                alpha = Math.max(alpha, ret);
            } else {
                if (firstStep && val < ret)
                    bestMove = move;
                ret = Math.min(val, ret);
                beta = Math.min(beta, ret);
            }
            if (alpha >= beta)
                break;
        }
        return ret;
    }

    public Move makeMove(Board board) {
        this.bestMove = null;
        Board copyBoard = new Board(board.getBoard(), board.getSide());
        minimax(copyBoard, 5, Min, Max, true);
        return bestMove;
    }
}
