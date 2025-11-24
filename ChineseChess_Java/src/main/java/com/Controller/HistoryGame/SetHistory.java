package com.Controller.HistoryGame;

import com.Model.Account.*;
import com.Model.InGame.playroom.*;
import com.Model.InGame.playersAI.Zobrist;

public class SetHistory {
    /**
     * 将 HistoryInfo 序列化为字符串，格式为：
     * <zobristKey>;<side>;<gameType>;<90个格子字符>
     * - zobristKey: long 值（十进制）
     * - side: 0 = red (false), 1 = black (true)
     * - gameType: 整数，表示游戏类型（由调用者定义，例如 0 = P2P, 1 = AI 等）
     * - 90个字符按 Zobrist.computeKey 中相同的遍历顺序（x=1..10, y=1..9）拼接
     */
    public static String BoardtoString(HistoryInfo info) {
        if (info == null)
            return null;
        Board board = info.getBoard();
        int gameType = info.getType();
        if (board == null)
            return null;
        long key = Zobrist.computeKey(board);
        StringBuilder sb = new StringBuilder();
        sb.append(key).append(';');
        sb.append(board.getSide() ? '1' : '0').append(';');
        sb.append(Integer.toString(gameType)).append(';');
        for (int x = 1; x <= 10; x++) {
            for (int y = 1; y <= 9; y++) {
                char pc = board.getPiece(x, y);
                if (pc == 0)
                    pc = '.';
                sb.append(pc);
            }
        }
        return sb.toString();
    }
}
