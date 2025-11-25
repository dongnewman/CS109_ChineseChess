package com.Model.InGame.playroom;

public class PieceProtect {
    /**
     * Find king position for the given side on the provided board.
     * 
     * @param board   the board
     * @param isBlack true for black (lowercase 'k'), false for red (uppercase 'K')
     * @return int[]{x,y} or null if not found
     */
    private static int dirc1[][] = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
    private static int dirc2[][] = { { 2, 1 }, { 2, -1 }, { -2, 1 }, { -2, -1 }, { 1, 2 }, { 1, -2 }, { -1, 2 },
            { -1, -2 } };

    public static int[] findKingPos(Board board, boolean isBlack) {
        char k = isBlack ? 'k' : 'K';
        if (!isBlack) {
            for (int x = 1; x <= 3; x++) {
                for (int y = 4; y <= 6; y++) {
                    if (board.getPiece(x, y) == k)
                        return new int[] { x, y };
                }
            }
        } else {
            for (int x = 8; x <= 10; x++) {
                for (int y = 4; y <= 6; y++) {
                    if (board.getPiece(x, y) == k)
                        return new int[] { x, y };
                }
            }
        }
        board.printboard();
        return null;
    }

    public static boolean isAttacked(Board board, int x, int y) {
        return isAttacked(board, x, y, board.getSide());
    }

    public static boolean isAttacked(Board board, int x, int y, boolean attacker) {
        // We dont consider the king facing attack here
        if (x < 1 || x > 10 || y < 1 || y > 9)
            return false;
        // pawn attacks
        if (!attacker) {
            if (x > 1 && board.getPiece(x - 1, y) == 'P')
                return true;
            if (x >= 6
                    && (y > 1 && board.getPiece(x, y - 1) == 'P' || (y < 9 && board.getPiece(x, y + 1) == 'P')))
                return true;
        } else {
            if (x < 10 && board.getPiece(x + 1, y) == 'p')
                return true;
            if (x <= 5
                    && (y > 1 && board.getPiece(x, y - 1) == 'p' || (y < 9 && board.getPiece(x, y + 1) == 'p')))
                return true;
        }
        // cannon and rook attacks
        for (int[] dir : dirc1) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            int cnt = 0;
            while (nx >= 1 && nx <= 10 && ny >= 1 && ny <= 9) {
                char piece = board.getPiece(nx, ny);
                if (piece == '.') {
                    nx += dir[0];
                    ny += dir[1];
                    continue;
                }
                if (piece == (attacker ? 'r' : 'R')) {
                    if (cnt == 0)
                        return true;
                } else if (piece == (attacker ? 'c' : 'C')) {
                    if (cnt == 1)
                        return true;
                }
                cnt++;
                if (cnt > 1)
                    break;
                nx += dir[0];
                ny += dir[1];
            }
        }
        // knight attacks
        for (int[] dir : dirc2) {
            int nx = x - dir[0];
            int ny = y - dir[1];
            int legX = nx + (dir[0] / 2);
            int legY = ny + (dir[1] / 2);
            if (nx < 1 || nx > 10 || ny < 1 || ny > 9)
                continue;
            if (board.getPiece(legX, legY) != '.')
                continue;
            char piece = board.getPiece(nx, ny);
            if (piece == (attacker ? 'n' : 'N'))
                return true;
        }
        return false;
    }

    public static boolean kingFacing(Board board) {
        int[] redKingPos = findKingPos(board, false);
        int[] blackKingPos = findKingPos(board, true);
        if (redKingPos == null || blackKingPos == null)
            return false;
        if (redKingPos[1] != blackKingPos[1])
            return false;
        for (int x = redKingPos[0] + 1; x < blackKingPos[0]; x++) {
            if (board.getPiece(x, redKingPos[1]) != '.')
                return false;
        }
        return true;
    }
}
