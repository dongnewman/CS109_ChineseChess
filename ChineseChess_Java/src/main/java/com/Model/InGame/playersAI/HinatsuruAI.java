package com.Model.InGame.playersAI;

import com.Model.InGame.playroom.*;

import java.util.*;

/*
What we can use:

ZobristHash & TTtable

*/

/**
 * HinatsuruAI (雏鹤 AI)
 * 实现了基于 Alpha-Beta 剪枝的 Minimax 算法的中国象棋 AI。
 * 
 * 主要特性：
 * 1. Alpha-Beta 剪枝：减少搜索节点数。
 * 2. 置换表 (Transposition Table, TT)：缓存已搜索局面的结果，避免重复计算。
 * 3. 历史启发 (History Heuristic)：对走法进行排序，优先搜索历史上表现好的走法，提高剪枝效率。
 * 4. 迭代加深 (Iterative Deepening)：在 SearchFrame 中实现（本类继承自 SearchFrame）。
 */
public class HinatsuruAI extends SearchFrame {

    public HinatsuruAI() {
        super();
    }

    // 打包走法到整型，便于与 TT 最优着法对比/存储
    private static int packMove(Move m) {
        return (m.getxi() & 0xFF) << 24 | (m.getyi() & 0xFF) << 16 | (m.getxf() & 0xFF) << 8 | (m.getyf() & 0xFF);
    }

    /**
     * Minimax 搜索算法核心实现
     * @param board 当前棋盘状态
     * @param resdep 剩余搜索深度
     * @param alpha Alpha 值（当前层最大下界）
     * @param beta Beta 值（当前层最小上界）
     * @param isRoot 是否为根节点
     * @param firstMove 用于回传根节点的最佳走法
     * @return 当前局面的评分
     */
    private int minimax(Board board, int resdep, int alpha, int beta, boolean isRoot, Move firstMove) {
        nodeCount++;
        // 尝试查询置换表
        int tryprobe = ttprobe(board, resdep, alpha, beta);
        if (tryprobe != -1)
            return tryprobe;

        int orialpha = alpha, oribeta = beta;
        // 到达叶子节点或搜索深度耗尽，进行静态局面评估
        if (resdep == 0)
            return terminalSearch(board, alpha, beta);

        // 生成所有合法走法
        ArrayList<Move> moves = isRoot ? getRootLegalMoves(board) : board.getAllValidMoves();
        // 历史表 + MVV 排序，优化搜索顺序
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
            // PVS (Principal Variation Search) 逻辑：
            // 对第一个节点进行全窗口搜索，后续节点先进行零窗口搜索 (Null Window Search)
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
