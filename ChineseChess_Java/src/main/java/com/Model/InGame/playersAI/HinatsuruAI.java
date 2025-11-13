package com.Model.InGame.playersAI;

import com.Model.InGame.playroom.*;

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

class MVVSort {
    static void Sort(Board board, ArrayList<Move> moves) {
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
    }
};

class CCheck {
    static boolean check(Board board) {
        /*
         * consider the board after oneside did the move, and the side now is the
         * attackside
         */
        boolean movedSide = !board.getSide();
        int[] kp = KingProtect.findKingPos(board, movedSide);
        if (kp != null && KingProtect.isSquareAttacked(board, kp[0], kp[1]) || KingProtect.kingFacing(board))
            return false;
        else
            return true;
    };
}

public class HinatsuruAI {

    static int Min = -1000000, Max = 1000000;
    private Move bestMove = null;
    // private TTable tt = null;

    private int quiescence(Board board, int alpha, int beta) {
        int stand = Estimation.est(board);
        boolean side = board.getSide();
        if (!side && alpha < stand)
            alpha = stand;
        if (side && beta > stand)
            beta = stand;
        if (alpha >= beta)
            return stand;
        ArrayList<Move> moves = board.getAllValidMoves();
        ArrayList<Move> captures = new ArrayList<>();
        for (Move m : moves) {
            if (board.getPiece(m.getxf(), m.getyf()) != '.')
                captures.add(m);
        }
        MVVSort.Sort(board, captures);
        if (captures.isEmpty())
            return stand;
        int ret = stand;
        for (Move move : captures) {
            char originalPiece = board.getPiece(move.getxf(), move.getyf());
            board.doMove(move);
            if (!CCheck.check(board)) {
                board.undoMove(move, originalPiece);
                continue;
            }
            int val = quiescence(board, alpha, beta);
            board.undoMove(move, originalPiece);
            if (!side) {
                ret = Math.max(ret, val);
                alpha = Math.max(alpha, val);
            } else {
                ret = Math.min(ret, val);
                beta = Math.min(beta, val);
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
        boolean side = board.getSide();
        ArrayList<Move> moves = firstStep ? board.getAllPossibleMoves() : board.getAllValidMoves();
        int ret = side ? Max : Min;
        if (firstStep)
            bestMove = moves.get(0);
        // MVV-LVA ordering: sort moves so captures are searched first (victim high,
        // attacker low)
        MVVSort.Sort(board, moves);
        for (Move move : moves) {
            char originalPiece = board.getPiece(move.getxf(), move.getyf());
            board.doMove(move);
            if (!firstStep) {
                if (!CCheck.check(board)) {
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
