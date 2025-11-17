package com.Model.InGame.playersAI;

import com.Model.InGame.playroom.*;

class Estimation {
    public static final int INF = 1000000;
    public static final int R = 10000, N = 5000, B = 2000, A = 1500, K = 1000000, C = 5500, P = 500;
    // positional constants
    public static final int PAWN_RIVER = 30, PAWN_CENTER = 30, KNIGHT_CENTER = 12, KNIGHT_ADVANCE = 10,
            ROOK_ADJ_EMPTY = 5, ADVISOR_PAIR = 20, ELEPHANT_PAIR = 20;

    public static int est(Board board) {
        int score = 0;
        int redAdvisors = 0, redElephants = 0, blackAdvisors = 0, blackElephants = 0;
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 9; j++) {
                char piece = board.getPiece(i, j);
                if (piece == '.')
                    continue;
                int sideFactor = Character.isUpperCase(piece) ? 1 : -1;
                int base;
                switch (Character.toUpperCase(piece)) {
                    case 'R':
                        base = R;
                        break;
                    case 'N':
                        base = N;
                        break;
                    case 'B':
                        base = B;
                        break;
                    case 'A':
                        base = A;
                        break;
                    case 'K':
                        base = K;
                        break;
                    case 'C':
                        base = C;
                        break;
                    default:
                        base = P;
                        break;
                }
                int positional = 0;
                switch (piece) {
                    case 'P':
                        if (i >= 6)
                            positional += PAWN_RIVER;
                        if (j >= 4 && j <= 6)
                            positional += PAWN_CENTER;
                        break;
                    case 'p':
                        if (i <= 5)
                            positional += PAWN_RIVER;
                        if (j >= 4 && j <= 6)
                            positional += PAWN_CENTER;
                        break;
                    case 'N':
                        if (j >= 3 && j <= 7)
                            positional += KNIGHT_CENTER;
                        if (i >= 4)
                            positional += KNIGHT_ADVANCE;
                        break;
                    case 'n':
                        if (j >= 3 && j <= 7)
                            positional += KNIGHT_CENTER;
                        if (i <= 7)
                            positional += KNIGHT_ADVANCE;
                        break;
                    case 'R':
                    case 'r':
                        int adj = 0;
                        if (i + 1 <= 10 && board.getPiece(i + 1, j) == '.')
                            adj++;
                        if (i - 1 >= 1 && board.getPiece(i - 1, j) == '.')
                            adj++;
                        if (j + 1 <= 9 && board.getPiece(i, j + 1) == '.')
                            adj++;
                        if (j - 1 >= 1 && board.getPiece(i, j - 1) == '.')
                            adj++;
                        positional += adj * ROOK_ADJ_EMPTY;
                        break;
                    default:
                        break;
                }
                score += sideFactor * (base + positional);
                if (piece == 'A')
                    redAdvisors++;
                else if (piece == 'a')
                    blackAdvisors++;
                else if (piece == 'B')
                    redElephants++;
                else if (piece == 'b')
                    blackElephants++;
            }
        }
        if (redAdvisors >= 2)
            score += ADVISOR_PAIR;
        if (blackAdvisors >= 2)
            score -= ADVISOR_PAIR;
        if (redElephants >= 2)
            score += ELEPHANT_PAIR;
        if (blackElephants >= 2)
            score -= ELEPHANT_PAIR;
        return score;
    }

    public static int pieceValue(char piece) {
        if (piece == '.')
            return 0;
        char p = Character.toUpperCase(piece);
        switch (p) {
            case 'R':
                return R;
            case 'N':
                return N;
            case 'B':
                return B;
            case 'A':
                return A;
            case 'K':
                return K;
            case 'C':
                return C;
            default:
                return P;
        }
    }
}