package com.Model.InGame.playersAI;

import com.Model.InGame.playroom.*;

import java.util.*;

/*
We implement MVV_LVA and search banned moves and so on in this file to assist the HinatsuruAI's main search process.
*/

class checkMove {
    public Move move;
    public boolean isCheck;
    public boolean isAtePiece;

    public checkMove(Move move, boolean isCheck, boolean isAtePiece) {
        this.move = move;
        this.isCheck = isCheck;
        this.isAtePiece = isAtePiece;
    }
}

public class SearchFrame {
    TTtable tt;
    ArrayList<checkMove> History;
    HistoryTable history = new HistoryTable();
    int nodeCount;

    public SearchFrame() {
        tt = new TTtable();
        History = new ArrayList<>();
        history = new HistoryTable();
    }

    // using the addHistory and getBannedMove to prevent perpetual check
    public void addHistory(Board board, Move move) {
        char originalPiece = board.getPiece(move.getxf(), move.getyf());
        board.doMove(move);
        boolean side = board.getSide();
        int[] kingPos = PieceProtect.findKingPos(board, side);
        boolean isCheck = PieceProtect.isAttacked(board, kingPos[0], kingPos[1], !side);
        boolean isAtePiece = (originalPiece != '.');
        History.add(new checkMove(move, isCheck, isAtePiece));
        board.undoMove(move, originalPiece);
    }

    public Move getBannedMove() {
        int siz = History.size();
        Move bannedMove = null;
        if (siz >= 2) {
            checkMove top1 = History.get(siz - 1);
            checkMove top2 = History.get(siz - 2);
            if (top1.isCheck && top2.isCheck && !top1.isAtePiece && !top2.isAtePiece) {
                if (top1.move.equals(top2.move)) {
                    bannedMove = top1.move;
                }
            }
        }
        return bannedMove;
    }

    public ArrayList<Move> getRootLegalMoves(Board board) {
        ArrayList<Move> legalMoves = Legal.getAllLegalMoves(board);
        Move bannedMove = getBannedMove();
        if (bannedMove != null && legalMoves.size() > 1) {
            legalMoves.removeIf(m -> m.equals(bannedMove));
        }
        return legalMoves;
    }

    public int terminalSearch(Board board, int alpha, int beta) {
        // debug testing for node cutting
        nodeCount++;
        //
        int tryprobe = ttprobe(board, 0, alpha, beta);
        if (tryprobe != -1)
            return tryprobe;
        int orialpha = alpha, oribeta = beta;
        boolean side = board.getSide();

        // Stand-pat: 以静态评估为基线，做边界裁剪（加入小幅度 margin，避免过度收紧）
        int stand = Estimation.est(board);
        final int MARGIN = 30; // 轻度收紧幅度（按评估刻度可微调）
        if (!side) { // maximizing
            if (stand - MARGIN >= beta) {
                ttstore(board, 0, orialpha, oribeta, stand);
                return stand;
            }
            int tightened = stand - MARGIN;
            if (tightened > alpha)
                alpha = tightened;
        } else { // minimizing
            if (stand + MARGIN <= alpha) {
                ttstore(board, 0, orialpha, oribeta, stand);
                return stand;
            }
            int tightened = stand + MARGIN;
            if (tightened < beta)
                beta = tightened;
        }

        int ret = stand;
        ArrayList<Move> moves = board.getAllValidMoves();
        MoveOrder.MVVSort(board, moves);
        // 仅扩展吃子，不再需要 nextSearch 标记
        for (Move move : moves) {
            if (!Legal.fastCheckLegal(board, move)) {
                continue;
            }
            if (board.getPiece(move.getxf(), move.getyf()) == '.') {
                continue;
            }
            char originalPiece = board.getPiece(move.getxf(), move.getyf());
            board.doMove(move);
            int score = terminalSearch(board, alpha, beta);
            board.undoMove(move, originalPiece);
            if (side) { // minimizing
                if (score < ret)
                    ret = score;
                if (score < beta)
                    beta = score;
            } else { // maximizing
                if (score > ret)
                    ret = score;
                if (score > alpha)
                    alpha = score;
            }
            if (alpha >= beta) {
                break;
            }
        }
        // 若无可扩展捕捉，返回 stand-pat
        ttstore(board, 0, orialpha, oribeta, ret);
        return ret;
    }

    public int ttprobe(Board board, int depth, int alpha, int beta) {
        TTtable.Entry entry = tt.probe(board, depth);
        if (entry == null)
            return -1;
        else {
            if (entry.flag == TTtable.FLAG_EXACT) {
                return entry.score;
            } else if (entry.flag == TTtable.FLAG_LOWERBOUND) {
                alpha = Math.max(alpha, entry.score);
            } else if (entry.flag == TTtable.FLAG_UPPERBOUND) {
                beta = Math.min(beta, entry.score);
            }
            //
            if (alpha >= beta) {
                return entry.score;
            } else
                return -1;
        }
    }

    public void ttstore(Board board, int depth, int orialpha, int oribeta, int ret) {
        if (ret <= orialpha) {// this is a upper bound ??
            tt.store(board, depth, ret, TTtable.FLAG_UPPERBOUND, -1);
        } else if (ret >= oribeta) {// this is a lower bound ??
            tt.store(board, depth, ret, TTtable.FLAG_LOWERBOUND, -1);
        } else {// this is an exact value
            tt.store(board, depth, ret, TTtable.FLAG_EXACT, -1);
        }
    }
}
