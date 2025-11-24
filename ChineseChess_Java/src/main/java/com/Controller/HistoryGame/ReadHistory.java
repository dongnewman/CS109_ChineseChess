package com.Controller.HistoryGame;

import com.Model.Account.*;
import com.Model.InGame.playroom.*;
import com.Model.InGame.playersAI.Zobrist;

public class ReadHistory {
    /**
     * 从 SetHistory.BoardtoString 生成的字符串中恢复 HistoryInfo（包含 Board 与 gameType）。
     * 格式：<zobristKey>;<side>;<gameType>;<90chars>
     * 若解析失败或 Zobrist 校验不通过，返回 null。
     */
    public static HistoryInfo StringtoBoard(String historyString) {
        if (historyString == null)
            return null;
        String[] parts = historyString.split(";", 4);
        if (parts.length != 4) {
            System.err.println("ReadHistory: history string format invalid");
            return null;
        }
        long providedKey;
        try {
            providedKey = Long.parseLong(parts[0]);
        } catch (NumberFormatException e) {
            System.err.println("ReadHistory: invalid zobrist key: " + parts[0]);
            return null;
        }
        boolean side = "1".equals(parts[1]);
        int gameType = 0;
        try {
            gameType = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            System.err.println("ReadHistory: invalid gameType: " + parts[2]);
            return null;
        }
        String boardChars = parts[3];
        if (boardChars.length() < 90) {
            System.err.println("ReadHistory: board data too short: " + boardChars.length());
            return null;
        }
        char[][] b = new char[11][10];
        for (int i = 0; i <= 10; i++) {
            for (int j = 0; j <= 9; j++)
                b[i][j] = '.';
        }
        int idx = 0;
        for (int x = 1; x <= 10; x++) {
            for (int y = 1; y <= 9; y++) {
                b[x][y] = boardChars.charAt(idx++);
            }
        }
        Board board = new Board(b, side);
        long computed = Zobrist.computeKey(board);
        if (computed != providedKey) {
            System.err.println("ReadHistory: Zobrist key mismatch: provided=" + providedKey + ", computed=" + computed);
            return null;
        }
        return new HistoryInfo(board, gameType);
    }

    /**
     * 宽松解析：只根据字符串恢复 Board 与 gameType，但不执行 Zobrist 校验。
     * 用于在 Zobrist 不可复现或校验失败时仍能查看/提示历史内容。
     */
    public static HistoryInfo StringtoBoardLoose(String historyString) {
        if (historyString == null)
            return null;
        String[] parts = historyString.split(";", 4);
        if (parts.length != 4) {
            System.err.println("ReadHistory.loose: history string format invalid");
            return null;
        }
        boolean side = "1".equals(parts[1]);
        int gameType = 0;
        try {
            gameType = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            System.err.println("ReadHistory.loose: invalid gameType: " + parts[2]);
            return null;
        }
        String boardChars = parts[3];
        if (boardChars.length() < 90) {
            System.err.println("ReadHistory.loose: board data too short: " + boardChars.length());
            return null;
        }
        char[][] b = new char[11][10];
        for (int i = 0; i <= 10; i++) {
            for (int j = 0; j <= 9; j++)
                b[i][j] = '.';
        }
        int idx = 0;
        for (int x = 1; x <= 10; x++) {
            for (int y = 1; y <= 9; y++) {
                b[x][y] = boardChars.charAt(idx++);
            }
        }
        Board board = new Board(b, side);
        return new HistoryInfo(board, gameType);
    }
}
