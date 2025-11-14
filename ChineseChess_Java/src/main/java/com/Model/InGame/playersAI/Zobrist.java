package com.Model.InGame.playersAI;

import java.util.Random;

import com.Model.InGame.playroom.Board;

/**
 * Simple Zobrist hashing for the Board state.
 *
 * - Uses random 64-bit keys for each (piece, square) and a side-to-move key.
 * - Board coordinates are 1..10 for rows (x) and 1..9 for columns (y).
 * - Empty squares ('.' or char 0) are ignored.
 */
public final class Zobrist {

    private static final char[] PIECES = {
            'R', 'N', 'B', 'A', 'K', 'C', 'P',
            'r', 'n', 'b', 'a', 'k', 'c', 'p'
    };
    private static final int PIECE_CNT = PIECES.length;

    // pieceKeys[pieceIndex][x][y], x in [0..10], y in [0..9]; we index 1..10/1..9
    private static long[][][] pieceKeys = new long[PIECE_CNT][11][10];
    private static long sideKey;
    private static boolean initialized = false;

    private Zobrist() {
    }

    public static synchronized void init() {
        init(System.nanoTime());
    }

    public static synchronized void init(long seed) {
        Random rnd = new Random(seed);
        for (int p = 0; p < PIECE_CNT; p++) {
            for (int x = 0; x <= 10; x++) {
                for (int y = 0; y <= 9; y++) {
                    pieceKeys[p][x][y] = rnd.nextLong();
                }
            }
        }
        sideKey = rnd.nextLong();
        initialized = true;
    }

    private static void ensureInit() {
        if (!initialized)
            init();
    }

    private static int pieceIndex(char p) {
        for (int i = 0; i < PIECE_CNT; i++) {
            if (PIECES[i] == p)
                return i;
        }
        return -1;
    }

    /**
     * Compute a Zobrist key for the given board from scratch.
     * Includes side-to-move: if board.getSide() is true, the side key is XORed.
     */
    public static long computeKey(Board board) {
        ensureInit();
        long key = 0L;
        for (int x = 1; x <= 10; x++) {
            for (int y = 1; y <= 9; y++) {
                char pc = board.getPiece(x, y);
                if (pc == '.' || pc == 0)
                    continue;
                int idx = pieceIndex(pc);
                if (idx >= 0) {
                    key ^= pieceKeys[idx][x][y];
                }
            }
        }
        if (board.getSide()) {
            key ^= sideKey;
        }
        return key;
    }

    // Expose piece key for incremental hashing
    public static long keyFor(char piece, int x, int y) {
        ensureInit();
        if (piece == '.' || piece == 0)
            return 0L;
        if (x < 0 || x > 10 || y < 0 || y > 9)
            return 0L;
        int idx = pieceIndex(piece);
        if (idx < 0)
            return 0L;
        return pieceKeys[idx][x][y];
    }

    // Expose side key for incremental hashing
    public static long sideKey() {
        ensureInit();
        return sideKey;
    }
}
