package com.Model.InGame.playersAI;

import com.Model.InGame.playroom.Board;
import com.Model.InGame.playroom.Move;
import java.util.Arrays;

/**
 * HistoryTable 为中国象棋搜索中的“历史启发”提供一个可复用的数据结构。
 *
 * 功能要点：
 * - 双侧分离：为先后手各维护一张历史表，避免相互干扰。
 * - 键空间：以 from(0..89) 与 to(0..89) 组合成索引（from*90 + to）。
 * - 基本操作：add(增量写入)、get(读取)、clear(清空)、decay(衰减防饱和)。
 * - 便捷封装：支持直接传入 Move 或 Board 以读取当前 side。
 *
 * 典型用法：
 * 1) 在非吃子导致的 beta 截断时：history.add(side, move, bonus)，常用 bonus = depth^2。
 * 2) 排序阶段：对静着使用 history.get(side, move) 作为次级排序关键字（低于 MVV、高于普通静着）。
 */
public class HistoryTable {
    public static final int BOARD_FILES = 9;
    public static final int BOARD_RANKS = 10;
    public static final int BOARD_SQUARES = BOARD_FILES * BOARD_RANKS; // 90
    private static final int MAX_CLAMP = 1_000_000_000;

    // table[side][from*90 + to]
    private final int[][] table;

    public HistoryTable() {
        this.table = new int[2][BOARD_SQUARES * BOARD_SQUARES];
    }

    /**
     * 将棋盘坐标 (x,y) 映射为 0..89 的格索引。
     * 注意：当前项目的棋盘坐标是 1-based（x:1..9, y:1..10），
     * 这里需转换为 0-based 再线性化。
     */
    public static int sq(int x, int y) {
        return (y - 1) * BOARD_FILES + (x - 1);
    }

    /** 计算表索引。 */
    private static int index(int fromSq, int toSq) {
        return fromSq * BOARD_SQUARES + toSq;
    }

    /** 清空整张历史表。 */
    public void clear() {
        for (int s = 0; s < 2; s++) {
            Arrays.fill(table[s], 0);
        }
    }

    /**
     * 衰减整表，避免长期饱和导致的排序失真。
     * 建议每回合或若干回合调用一次，例如 shift=1。
     */
    public void decay(int shift) {
        if (shift <= 0)
            return;
        for (int s = 0; s < 2; s++) {
            int[] arr = table[s];
            for (int i = 0; i < arr.length; i++) {
                arr[i] >>= shift;
            }
        }
    }

    /** 按 side / fromSq / toSq 增加历史分（带饱和）。 */
    public void add(boolean side, int fromSq, int toSq, int bonus) {
        int s = side ? 1 : 0;
        int idx = index(fromSq, toSq);
        if (idx < 0 || idx >= table[s].length) {
            return; // 越界保护
        }
        int val = table[s][idx] + bonus;
        if (val > MAX_CLAMP)
            val = MAX_CLAMP;
        if (val < -MAX_CLAMP)
            val = -MAX_CLAMP;
        table[s][idx] = val;
    }

    /** 通过 Move 增加历史分。 */
    public void add(boolean side, Move m, int bonus) {
        add(side, sq(m.getxi(), m.getyi()), sq(m.getxf(), m.getyf()), bonus);
    }

    /** 基于当前棋面一手方（board.getSide()）增加历史分。 */
    public void add(Board board, Move m, int bonus) {
        add(board.getSide(), m, bonus);
    }

    /** 读取历史分。 */
    public int get(boolean side, int fromSq, int toSq) {
        int s = side ? 1 : 0;
        int idx = index(fromSq, toSq);
        if (idx < 0 || idx >= table[s].length) {
            return 0; // 越界视为无历史
        }
        return table[s][idx];
    }

    /** 通过 Move 读取历史分。 */
    public int get(boolean side, Move m) {
        return get(side, sq(m.getxi(), m.getyi()), sq(m.getxf(), m.getyf()));
    }

    /** 基于当前棋面一手方读取历史分。 */
    public int get(Board board, Move m) {
        return get(board.getSide(), m);
    }
}
