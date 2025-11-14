package com.Model.InGame.playersAI;

import com.Model.InGame.playroom.Board;

public class TTtable {

    public static final class Entry {
        public final long key; // 完整 Zobrist key，用于消除同槽冲突
        public final int depth; // 存储时的搜索深度（剩余深度）
        public final int score; // 评估分数
        public final byte flag; // 0=EXACT, 1=LOWERBOUND, 2=UPPERBOUND
        // Despite i dont know where i will use it, i write it anyway
        public final int movePacked; // 可选：将最佳着法打包为整型，-1 表示未知

        public Entry(long key, int depth, int score, byte flag, int movePacked) {
            this.key = key;
            this.depth = depth;
            this.score = score;
            this.flag = flag;
            this.movePacked = movePacked;
        }
    }

    public static final byte FLAG_EXACT = 0;
    public static final byte FLAG_LOWERBOUND = 1;
    public static final byte FLAG_UPPERBOUND = 2;

    private final Entry[] table;
    private final int mask;

    private static final int DEFAULT_CAPACITY = 1 << 20; // 可按需调整

    // 使用默认容量
    public TTtable() {
        this(DEFAULT_CAPACITY);
    }

    // 容量将被提升到 >= capacity 的最小 2 的幂
    public TTtable(int capacity) {
        int cap = nextPowerOfTwo(Math.max(1, capacity));
        this.table = new Entry[cap];
        this.mask = cap - 1;
    }

    // 正确写入：由调用方传入 flag（EXACT/LOWERBOUND/UPPERBOUND）与 movePacked
    public void store(Board board, int dep, int score, byte flag, int movePacked) {
        long key = board.getZobristKey();
        int idx = index(key);
        Entry cur = table[idx];

        if (cur == null) {
            table[idx] = new Entry(key, dep, score, flag, movePacked);
            return;
        }

        // key 冲突：用更“有价值”的条目替换（更深的优先）
        if (cur.key != key) {
            table[idx] = (dep >= cur.depth) ? new Entry(key, dep, score, flag, movePacked) : cur;
            return;
        }

        // 同 key：当新 dep 更大时覆盖，否则保留现条目
        if (dep >= cur.depth) {
            table[idx] = new Entry(key, dep, score, flag, movePacked);
        }
        // 否则保持现有条目
    }

    // 调用 (board, dep)；若未命中或保存的 dep < 需要的 dep，返回 null
    public Entry probe(Board board, int dep) {
        long key = board.getZobristKey();
        int idx = index(key);
        Entry cur = table[idx];
        if (cur == null)
            return null;
        if (cur.key != key)
            return null;
        if (cur.depth < dep)
            return null;
        return cur;
    }

    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
    }

    private int index(long key) {
        // 混合高低位，降低低位相关性
        int mix = (int) (key ^ (key >>> 32));
        return mix & mask;
    }

    private static int nextPowerOfTwo(int x) {
        int v = x - 1;
        v |= v >> 1;
        v |= v >> 2;
        v |= v >> 4;
        v |= v >> 8;
        v |= v >> 16;
        return v + 1;
    }
}
