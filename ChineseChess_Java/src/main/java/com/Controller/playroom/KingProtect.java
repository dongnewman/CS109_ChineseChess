package com.Controller.playroom;

public class KingProtect {
    /**
     * Find king position for the given side on the provided board.
     * 
     * @param board   the board
     * @param isBlack true for black (lowercase 'k'), false for red (uppercase 'K')
     * @return int[]{x,y} or null if not found
     */
    public static int[] findKingPos(Board board, boolean isBlack) {
        char k = isBlack ? 'k' : 'K';
        for (int x = 1; x <= 10; x++) {
            for (int y = 1; y <= 9; y++) {
                if (board.getPiece(x, y) == k)
                    return new int[] { x, y };
            }
        }
        return null;
    }

    /**
     * Detect whether square (x,y) is attacked by side `byBlack` on the provided
     * board.
     * Implements common Xiangqi attack patterns (pawns, knights with leg, rooks,
     * cannons, king facing).
     * This is a conservative fast check; diagonal elephant attacks are omitted for
     * brevity but can be added.
     */
    public static boolean isSquareAttacked(Board board, int x, int y) {
        if (x < 1 || x > 10 || y < 1 || y > 9)
            return false;
        boolean attackerLower = board.getSide();

        // Pawn attacks
        if (attackerLower) {
            int px = x + 1;
            if (px >= 1 && px <= 10) {
                if (y - 1 >= 1 && board.getPiece(px, y - 1) == 'p')
                    return true;
                if (y + 1 <= 9 && board.getPiece(px, y + 1) == 'p')
                    return true;
            }
        } else {
            int px = x - 1;
            if (px >= 1 && px <= 10) {
                if (y - 1 >= 1 && board.getPiece(px, y - 1) == 'P')
                    return true;
                if (y + 1 <= 9 && board.getPiece(px, y + 1) == 'P')
                    return true;
            }
        }

        // Knight checks
        int[][] dkn = { { -2, -1 }, { -2, 1 }, { -1, -2 }, { -1, 2 }, { 1, -2 }, { 1, 2 }, { 2, -1 }, { 2, 1 } };
        for (int[] d : dkn) {
            int kx = x + d[0], ky = y + d[1];
            if (kx < 1 || kx > 10 || ky < 1 || ky > 9)
                continue;
            int lx = x + (d[0] / 2), ly = y + (d[1] / 2);
            if (lx < 1 || lx > 10 || ly < 1 || ly > 9)
                continue;
            if (board.getPiece(lx, ly) != '.')
                continue; // blocked leg
            char piece = board.getPiece(kx, ky);
            if (attackerLower) {
                if (piece == 'n')
                    return true;
            } else {
                if (piece == 'N')
                    return true;
            }
        }

        // Rook/king facing and cannon checks along ranks/files
        int[][] dirs = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        for (int[] d : dirs) {
            int cnt = 0;
            int nx = x + d[0], ny = y + d[1];
            while (nx >= 1 && nx <= 10 && ny >= 1 && ny <= 9) {
                char p = board.getPiece(nx, ny);
                if (p != '.') {
                    if (cnt == 0) {
                        if (attackerLower) {
                            if (p == 'r' || p == 'k')
                                return true;
                        } else {
                            if (p == 'R' || p == 'K')
                                return true;
                        }
                    } else if (cnt == 1) {
                        if (attackerLower) {
                            if (p == 'c')
                                return true;
                        } else {
                            if (p == 'C')
                                return true;
                        }
                    }
                    cnt++;
                }
                nx += d[0];
                ny += d[1];
            }
        }

        return false;
    }

}
