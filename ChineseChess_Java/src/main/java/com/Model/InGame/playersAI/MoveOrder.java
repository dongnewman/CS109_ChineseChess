package com.Model.InGame.playersAI;

import com.Model.InGame.playroom.*;
import java.util.*;

class MoveOrder {
    static void MVVSort(Board board, ArrayList<Move> moves) {
        Collections.sort(moves, new Comparator<Move>() {
            @Override
            public int compare(Move m1, Move m2) {
                char vic1 = board.getPiece(m1.getxf(), m1.getyf());
                char att1 = board.getPiece(m1.getxi(), m1.getyi());
                int score1 = Estimation.pieceValue(vic1) * 100 - Estimation.pieceValue(att1);

                char vic2 = board.getPiece(m2.getxf(), m2.getyf());
                char att2 = board.getPiece(m2.getxi(), m2.getyi());
                int score2 = Estimation.pieceValue(vic2) * 100 - Estimation.pieceValue(att2);

                return Integer.compare(score2, score1);
            }
        });
    }

    static void complexSort(Board board, ArrayList<Move> moves, HistoryTable history) {
        final boolean side = board.getSide();
        moves.sort((m1, m2) -> {
            char v1 = board.getPiece(m1.getxf(), m1.getyf());
            char a1 = board.getPiece(m1.getxi(), m1.getyi());
            char v2 = board.getPiece(m2.getxf(), m2.getyf());
            char a2 = board.getPiece(m2.getxi(), m2.getyi());

            boolean c1 = v1 != '.';
            boolean c2 = v2 != '.';

            if (c1 && !c2)
                return -1;
            if (!c1 && c2)
                return 1;

            if (c1 && c2) {
                int s1 = Estimation.pieceValue(v1) * 100 - Estimation.pieceValue(a1);
                int s2 = Estimation.pieceValue(v2) * 100 - Estimation.pieceValue(a2);
                return Integer.compare(s2, s1);
            }

            int h1 = history.get(side, m1);
            int h2 = history.get(side, m2);
            return Integer.compare(h2, h1);
        });
    }
};