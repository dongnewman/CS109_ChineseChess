package com.Model.InGame.playersAI;

import com.Model.InGame.playroom.*;

import java.util.*;

/*
What we can use:

ZobristHash & TTtable

*/

public class HinatsuruAI extends SearchFrame {

    public HinatsuruAI() {
        super();
    }

    // 打包走法到整型，便于与 TT 最优着法对比/存储
    private static int packMove(Move m) {
        return (m.getxi() & 0xFF) << 24 | (m.getyi() & 0xFF) << 16 | (m.getxf() & 0xFF) << 8 | (m.getyf() & 0xFF);
    }

    private int minimax(Board board, int resdep, int alpha, int beta, boolean isRoot, Move firstMove) {
        nodeCount++;
        int tryprobe = ttprobe(board, resdep, alpha, beta);
        if (tryprobe != -1)
            return tryprobe;

        int orialpha = alpha, oribeta = beta;
        if (resdep == 0)
            return terminalSearch(board, alpha, beta);

        ArrayList<Move> moves = isRoot ? getRootLegalMoves(board) : board.getAllValidMoves();
        // 历史表 + MVV 排序
        MoveOrder.complexSort(board, moves, history);
        // 将 TT 最优着法置前尝试（若存在）
        TTtable.Entry hinted = tt.probe(board, resdep);
        if (hinted != null && hinted.movePacked != -1 && !moves.isEmpty()) {
            for (int i = 0; i < moves.size(); i++) {
                if (packMove(moves.get(i)) == hinted.movePacked) {
                    if (i != 0) {
                        Move tmp = moves.get(0);
                        moves.set(0, moves.get(i));
                        moves.set(i, tmp);
                    }
                    break;
                }
            }
        }

        boolean side = board.getSide();
        int ret = side ? Estimation.INF : -Estimation.INF;
        boolean anySearched = false;
        boolean firstTried = false;
        int bestPacked = -1;

        for (Move move : moves) {
            if (!Legal.fastCheckLegal(board, move))
                continue;
            char originalPiece = board.getPiece(move.getxf(), move.getyf());
            board.doMove(move);

            int score;
            if (!firstTried) {
                score = minimax(board, resdep - 1, alpha, beta, false, null);
            } else {
                if (!side) { // maximizing
                    score = minimax(board, resdep - 1, alpha, alpha + 1, false, null);
                    if (score > alpha && score < beta)
                        score = minimax(board, resdep - 1, alpha, beta, false, null);
                } else { // minimizing
                    score = minimax(board, resdep - 1, beta - 1, beta, false, null);
                    if (score < beta && score > alpha)
                        score = minimax(board, resdep - 1, alpha, beta, false, null);
                }
            }

            board.undoMove(move, originalPiece);
            anySearched = true;
            firstTried = true;

            if (side) { // minimizing
                if (score < ret) {
                    ret = score;
                    if (isRoot && firstMove != null)
                        firstMove.Copy(move);
                    bestPacked = packMove(move);
                }
                beta = Math.min(beta, ret);
            } else { // maximizing
                if (score > ret) {
                    ret = score;
                    if (isRoot && firstMove != null)
                        firstMove.Copy(move);
                    bestPacked = packMove(move);
                }
                alpha = Math.max(alpha, ret);
            }

            if (alpha >= beta) {
                if (originalPiece == '.') {
                    int bonus = (resdep + 1) * (resdep + 1);
                    history.add(board, move, bonus);
                }
                break;
            }
        }

        if (!anySearched)
            return terminalSearch(board, alpha, beta);

        byte flag;
        if (ret <= orialpha)
            flag = TTtable.FLAG_UPPERBOUND;
        else if (ret >= oribeta)
            flag = TTtable.FLAG_LOWERBOUND;
        else
            flag = TTtable.FLAG_EXACT;
        tt.store(board, resdep, ret, flag, bestPacked);
        return ret;
    }

    public Move makeMove(Board board) {
        nodeCount = 0;
        // 历史表轻度衰减，保持稳定性
        history.decay(1);
        Move bestMove = new Move(0, 0, 0, 0);
        int maxDepth = 6;
        int bestScore = 0;

        int lastScore = Estimation.est(board);
        final int INF = Estimation.INF;

        for (int depth = 1; depth <= maxDepth; depth++) {
            Move currentBest = new Move(0, 0, 0, 0);
            int alpha, beta;
            if (depth == 1) {
                alpha = -INF;
                beta = INF;
                lastScore = minimax(board, depth, alpha, beta, true, currentBest);
            } else {
                int delta = 50;
                alpha = Math.max(lastScore - delta, -INF);
                beta = Math.min(lastScore + delta, INF);
                while (true) {
                    int score = minimax(board, depth, alpha, beta, true, currentBest);
                    if (score <= alpha) {
                        if (alpha <= -INF + 1) {
                            lastScore = score;
                            break;
                        }
                        delta <<= 1;
                        alpha = Math.max(score - delta, -INF);
                    } else if (score >= beta) {
                        if (beta >= INF - 1) {
                            lastScore = score;
                            break;
                        }
                        delta <<= 1;
                        beta = Math.min(score + delta, INF);
                    } else {
                        lastScore = score;
                        break;
                    }
                }
            }

            if (!(currentBest.getxi() == 0 && currentBest.getyi() == 0 && currentBest.getxf() == 0
                    && currentBest.getyf() == 0)) {
                bestMove.Copy(currentBest);
                bestScore = lastScore;
            }
        }
        System.out.println("Best score: " + bestScore + ", Nodes searched: " + nodeCount);
        return bestMove;
    }
}
